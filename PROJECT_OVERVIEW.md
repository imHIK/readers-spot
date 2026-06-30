# Readers Spot — Project Overview

Readers Spot (internal package name `org.bigBrotherBooks`) is the backend for a
book‑lovers' platform that combines three ideas in one service:

1. **A book "pedia"** — a catalogue of books and authors that users can browse,
   rate and review, enriched with metadata pulled from the Google Books API.
2. **A social layer** — users can follow each other, mark favourite books and
   authors, and share reviews.
3. **A physical rental system** — books are physically stocked in **warehouses**;
   users raise **rent requests**, a copy is **issued** from a nearby warehouse,
   and later **returned** (with condition tracking and pricing).

This document explains what the project is, how it is structured, and how the
pieces fit together. For the roadmap and known gaps, see
[`FUTURE_IMPROVEMENTS.md`](./FUTURE_IMPROVEMENTS.md).

> Status: early stage. The core CRUD, auth and rental flows exist; several
> features described in the original README (waiting queue, payments, groups)
> are still to come.

---

## 1. Technology stack

| Concern              | Choice                                                            |
|----------------------|-------------------------------------------------------------------|
| Language / runtime   | Java 21                                                           |
| Framework            | [Quarkus](https://quarkus.io) 3.11                               |
| HTTP / REST          | Quarkus REST (JAX‑RS) + Jackson                                  |
| Persistence          | Hibernate ORM with **Panache** (repository pattern)             |
| Database             | MySQL                                                             |
| Auth                 | SmallRye JWT (RS256), Quarkus Security, Elytron BCrypt           |
| External API client  | MicroProfile REST Client + a hand‑rolled `java.net.http` client  |
| Build                | Gradle (Quarkus plugin)                                          |
| Tests                | JUnit 5, REST‑Assured                                            |

---

## 2. High‑level architecture

The service follows a classic layered architecture:

```
HTTP request
    │
    ▼
resource/   ──►  JAX‑RS endpoints (thin controllers, build HTTP responses)
    │
    ▼
service/    ──►  business logic, transactions, DTO ⇄ entity mapping
    │
    ▼
repository/ ──►  Panache repositories (data access)
    │
    ▼
model/      ──►  JPA entities  ◄── persisted in MySQL
```

Cross‑cutting concerns live in their own packages: `auth/` (JWT filter),
`exception/` (error mapping), `logger/` (logging facade), `infra/` (generic
utilities and data structures), `api/` (outbound HTTP), `dto/` and
`configModels/` (transport / config types), `constants/` and `contexts/`.

### Request lifecycle
1. A request hits a `*RestApi` class in `resource/`.
2. `JwtAuthenticationFilter` runs first: public auth routes pass through;
   everything else needs a valid `Bearer` JWT.
3. The resource delegates to a `*Service`, which opens a transaction
   (`@Transactional`) when it mutates data.
4. The service uses a `*Repository` to load/save entities and maps between
   entities and DTOs.
5. The resource turns the result into a JAX‑RS `Response`; uncaught errors are
   converted to a JSON `ApiError` by the mappers in `exception/`.

---

## 3. Package‑by‑package guide

- **`resource/`** — JAX‑RS controllers, one per domain area: `AuthResource`,
  `UserRestApi`, `BookRestApi`, `AuthorRestApi`, `ReviewRestApi`,
  `WarehouseRestApi`, `RequestRestApi` (rentals), plus two catalogue endpoints
  (`ResourceRestApi`, `ResourceRestApiV2`).
- **`service/`** — business logic: `AuthService`, `TokenService`, `UserService`,
  `BookService`, `AuthorService`, `ReviewService`, `WarehouseService`,
  `RequestService`, `ResourceService`.
- **`repository/`** — Panache repositories (`PanacheRepositoryBase`), one per
  aggregate root, with a few custom finder queries.
- **`model/`** — JPA entities (`User`, `Book`, `Author`, `Review`, `Warehouse`,
  `Stock`, `RentRequest`) and plain request/response models (`LoginRequest`,
  `HttpRequest`, `HttpResponse`, …).
- **`dto/`** — data‑transfer objects exposed over the API, keeping entities out
  of the wire contract and hiding sensitive fields (e.g. password hash).
- **`configModels/`** — enums and helper types: `Genre`, `Status`,
  `BookCondition`, `Star`, the Google Books response models, and `CustomMap`.
- **`auth/`** — `JwtAuthenticationFilter` (deny‑by‑default request filter) and
  `AuthPathMatcher` (whitelist of public routes).
- **`exception/`** — `ApiError` envelope and the JAX‑RS `ExceptionMapper`s.
- **`api/`** — outbound HTTP: a generic, retrying `HttpClient` plus a
  MicroProfile `HttpClientV2`/`HttpServiceV2` pair for Google Books.
- **`infra/`** — reusable utilities (`JsonUtils`, `HttpUtils`, `StringUtils`,
  `CollectionUtils`, `TemplateUtils`) and data structures (`DoublyLinkedList`,
  `Heap`, `LRUCache`, `LFUCache`, `Pair`, `Node`).
- **`logger/`** — a thin SLF4J facade (`Logger`, `LoggerFactory`, `LogType`).
- **`constants/`, `contexts/`** — global constants and the per‑request
  `UserContext`.

---

## 4. Domain model

### Entities and keys

| Entity        | Primary key                                  | Notes                                             |
|---------------|----------------------------------------------|---------------------------------------------------|
| `User`        | `userName` (String)                          | roles, profile, social graph                      |
| `Book`        | `bookId` (auto int)                          | metadata, genres, rating, stock                   |
| `Author`      | `authorId` (auto int)                        | one author → many books                           |
| `Review`      | composite `(userName, bookId)`               | `@EmbeddedId` + `@MapsId`                          |
| `Warehouse`   | `warehouseId` (auto int)                     | physical location holding stock                   |
| `Stock`       | composite `(warehouseId, bookId, condition)` | quantity of a book in a condition at a warehouse  |
| `RentRequest` | `reqId` (auto int)                           | the rental lifecycle record                       |

### Relationships

- **User ⇄ Book** (favourites) — many‑to‑many via `favorite_books`.
- **User ⇄ Author** (favourites) — many‑to‑many via `favorite_authors`.
- **User ⇄ User** (social) — many‑to‑many self join (`following` / `followedBy`).
- **Author → Book** — one‑to‑many (`author_id` on book).
- **Book → Review ← User** — `Review` is the join with its own fields
  (text, star rating, likes, timestamp).
- **Warehouse → Stock ← Book** — `Stock` is the join carrying `quantity` and
  `BookCondition`.
- **User → RentRequest ← Warehouse/Book** — a rent request ties a user, a book
  and a warehouse together with timing, conditions and price.

### Key enums (`configModels/`)
- `Status` — rental lifecycle: `REQUESTED → APPROVED/REJECTED/CANCELLED →
  ISSUED → RETURNED`.
- `BookCondition` — `NEW, GOOD, MODERATE, POOR` (stock is tracked per condition).
- `Star` — `ZERO…FIVE` review rating.
- `Genre` — a large fixed list of book genres.

---

## 5. REST API surface

All routes are JSON. Every route except the three public auth routes requires an
`Authorization: Bearer <jwt>` header.

**Auth — `/auth`**
- `POST /auth/register` — create a user (public).
- `POST /auth/login` — returns the JWT in the `Authorization` response header (public).
- `POST /auth/refresh` — placeholder, not yet implemented (public).

**Users — `/user`**
- `GET /user/{user_name}`, `POST /user/save`, `POST /user/update`,
  `DELETE /user/{user_name}`, `GET /user/list?user_names=…`,
  `GET /user/all` *(ADMIN only)*, `GET /user/detailed/{user_name}`.
- Social: `POST /user/follow/{user_name}?from=…`,
  `POST /user/unfollow/{user_name}?from=…`,
  `POST /user/followers/{user_name}`, `POST /user/following/{user_name}`.
- Favourites: `POST /user/addFavoriteBook|removeFavoriteBook/{user_name}?book_id=…`,
  `POST /user/addFavoriteAuthor|removeFavoriteAuthor/{user_name}?author_id=…`.
- `POST /user/updateProfile/{user_name}`, `GET /user/reviews/{user_name}`.

**Books — `/book`**
- `POST /book/save`, `GET /book/{id}`, `PUT /book/update`, `DELETE /book/{id}`,
  `GET /book/all`, `GET /book/detailed/{book_id}`, `GET /book/reviews/{book_id}`.

**Authors — `/author`**
- `POST /author/save`, `GET /author/{id}`, `PUT /author/update`,
  `DELETE /author/{id}`, `GET /author/all`,
  `POST /author/publish/{author_id}/{book_id}`,
  `GET /author/detailed/{author_id}`.

**Reviews — `/review`**
- `POST|GET|PUT|DELETE /review/{user_name}/{book_id}`.

**Warehouses — `/warehouse`**
- `POST /warehouse/`, `GET /warehouse/{warehouse_id}`, `PUT /warehouse/`,
  `DELETE /warehouse/{warehouse_id}`, `GET /warehouse/all`,
  `GET|POST|DELETE /warehouse/stock/{warehouse_id}`.

**Rentals — `/rent`**
- `POST /rent/request/{user_name}` — raise a request.
- `POST /rent/process/{request_id}` — issue a copy (decrements stock).
- `POST /rent/return/{request_id}` — return a copy (restocks, prices it).
- `GET|PUT|DELETE /rent/{request_id}`.

**Catalogue (external) — `/resource`, `/catalog`**
- `GET /catalog/book/{isbn}` — fetch metadata from Google Books by ISBN.
- `POST /resource/{resourceType}/{resourceId}` — generic, config‑driven external
  fetch (see §7).

A Postman collection (`Readers-spot.postman_collection.json`) and the ERD
(`ERD_ReadersSpot.eddx`) accompany the repo.

---

## 6. Authentication & authorization

- **Registration** (`AuthService.encryptPassword`) stores a **BCrypt** hash.
- **Login** verifies the password and, via `TokenService`, issues an **RS256
  JWT** signed with `privateKey.pem`, carrying the username (`sub`/`upn`) and
  roles (`groups`). Tokens are valid for 30 days (`GlobalConstants`).
- **Verification** happens two ways:
  - `JwtAuthenticationFilter` enforces *deny‑by‑default*: any non‑public path
    must present a valid, unexpired, correctly‑issued Bearer token, otherwise it
    is rejected with `401`. It also populates a per‑request `UserContext`.
  - Quarkus SmallRye JWT validates the same token against `publicKey.pem`, which
    is what makes method‑level `@RolesAllowed("ADMIN")` (e.g. `GET /user/all`)
    work.
- **Roles** are sanitised on save against a whitelist (`USER`, `ADMIN`).

---

## 7. External catalogue integration (Google Books)

There are two outbound‑HTTP styles in the codebase:

1. **`HttpClientV2` + `HttpServiceV2`** (used today) — a MicroProfile REST Client
   interface that calls `…/books/v1/volumes?q=isbn:<isbn>` and maps the response
   into `GoogleBooksVolumeInfo`. This backs `GET /catalog/book/{isbn}`.
2. **`ResourceService` + the generic `HttpClient`** — a config‑driven engine that
   loads a JSON request template from `resources/configs/<type>/<id>.json`,
   resolves `{{ placeholders }}` from the request body using `TemplateUtils`, and
   executes it with a retrying `java.net.http` client. This backs the generic
   `POST /resource/{resourceType}/{resourceId}` endpoint.

Both require a Google Books **API key**, supplied via the `google-cloud-api-key`
configuration property.

---

## 8. Configuration & running locally

Configuration lives in `src/main/resources/application.properties`, which is
**git‑ignored**. Copy the template and fill in values:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

You will need:
- A running **MySQL** database and matching `quarkus.datasource.*` settings.
- An **RSA key pair** as `privateKey.pem` / `publicKey.pem` on the classpath
  (also git‑ignored).
- A **Google Books API key** in `google-cloud-api-key`.

Run in dev mode (live reload):

```bash
./gradlew quarkusDev
```

Build / test:

```bash
./gradlew build      # compile, run tests, package
./gradlew test       # tests only
```

> Requires JDK 21 and network access to Maven Central for the first build.

---

## 9. Testing

Unit tests live under `src/test/java`. They are intentionally
**framework‑light** — services are exercised with small in‑memory repository
fakes (overriding Panache's `findById`), so the suite runs without a database or
CDI container. Coverage includes:
- the utility layer (`StringUtils`, `CollectionUtils`, `TemplateUtils`, `HttpUtils`),
- the infra data structures (`DoublyLinkedList`, `Heap`, `LRUCache`, `LFUCache`, `Pair`),
- `CustomMap`, `ApiError`, `AuthPathMatcher`,
- and the recently‑fixed service behaviours (null handling in `UserService`/
  `BookService`, stock guards in `WarehouseService`, password‑hash hiding).

Broader `@QuarkusTest` integration tests (real HTTP + database) are a planned
addition — see the roadmap.

---

## 10. Recent correctness fixes

This pass addressed a set of bugs that would prevent the app from booting or
behaving correctly:
- **JPA mappings** — `Book.genres` (`List<Genre>`) and `User.roles`
  (`Set<String>`) were mapped with a plain `@Column`, which Hibernate cannot
  persist; they are now `@ElementCollection`s. `RentRequest.status` and
  `Review.rating` enums are now `@Enumerated(STRING)`.
- **NullPointerExceptions** — `UserService.modifyFavoriteBook/Author` and
  `followUser` dereferenced a possibly‑null user before checking it;
  `BookService`/`UserService` review/lookup paths could NPE on missing rows.
  These now return cleanly and the endpoints answer `404` instead of `500`.
- **Auth** — role sanitisation always forced `USER`, so `ADMIN` could never be
  granted (locking everyone out of admin endpoints); the JWT filter could NPE on
  tokens without a groups claim.
- **Rental flow** — requests now default to `REQUESTED`, issuing resolves an
  available stock condition, returns are guarded by status, the return condition
  is persisted, stock can no longer go negative, and a basic rent price is
  computed.
- **Error handling** — added a global `ExceptionMapper` and a validation mapper
  that return a consistent JSON `ApiError`.

See `FUTURE_IMPROVEMENTS.md` for what is still open.
