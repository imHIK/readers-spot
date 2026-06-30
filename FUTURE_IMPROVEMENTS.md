# Readers Spot — Future Improvements

A prioritised, actionable list of improvements for the project. Items at the top
are the highest‑value / lowest‑risk; lower sections are larger investments.
See [`PROJECT_OVERVIEW.md`](./PROJECT_OVERVIEW.md) for context.

---

## 1. Security (do first)

- **Rotate the Google Books API key.** A real key currently sits in your local
  `src/main/resources/application.properties`. That file is git‑ignored (good),
  but the key has been on disk in plaintext — rotate it in the Google Cloud
  console and treat the old value as compromised.
- **Move secrets out of properties files.** Inject the API key, DB password and
  JWT key locations from environment variables (`${GOOGLE_BOOKS_API_KEY}`) or a
  secrets manager (Vault, AWS/GCP secret manager). Never commit secrets.
- **Consolidate the two auth mechanisms.** The custom `JwtAuthenticationFilter`
  and Quarkus's built‑in SmallRye JWT both validate the token. Pick one:
  prefer Quarkus security with `@RolesAllowed` / `quarkus.http.auth.*` policies
  and a small `@PermitAll` set for public routes, and drop the hand‑rolled
  filter (or keep only the deny‑by‑default policy). Right now they overlap and
  the `UserContext` the filter populates is never read.
- **Password policy & lockout.** Enforce minimum length/complexity on register,
  and add throttling / lockout to `/auth/login` to resist brute force.
- **Refresh tokens.** Implement `/auth/refresh` with short‑lived access tokens
  and rotating refresh tokens; 30‑day access tokens are long‑lived.
- **Transport & CORS.** Require HTTPS in production and configure CORS explicitly
  for the intended front‑ends.

## 2. Persistence & data integrity

- **Adopt schema migrations.** Replace `hibernate-orm.database.generation=update`
  with **Flyway** or **Liquibase** so schema changes are versioned and
  reviewable, rather than inferred at boot.
- **Add optimistic locking** (`@Version`) to mutable entities, especially
  `Stock` and `RentRequest`, to make concurrent issue/return safe.
- **Index hot columns** — `Warehouse.city`, `Book.isbn13`, foreign keys used by
  finders — and add uniqueness where appropriate (e.g. ISBN).
- **Honour soft delete.** `User.isDeleted` exists but `deleteUser` hard‑deletes
  and queries don't filter deleted users. Decide on hard vs soft delete and
  apply it consistently (the resource even has a `// TODO: hard and soft delete`).
- **Pagination.** `getAllBooks`, `getAllUsers`, `getAllAuthors`, etc. return
  whole tables; add Panache paging (`page(...)`) and expose `page`/`size` params.
- **Avoid N+1 / lazy issues.** `detailed/*` endpoints and DTO mapping touch lazy
  collections; use fetch joins or projection queries, and consider a mapping
  library (MapStruct) instead of hand‑written mappers.

## 3. The rental system (core product feature)

- **Waiting queue.** The README's headline feature: when no copy is available,
  queue the user and notify/assign when one is returned. The `infra` `Heap` /
  `DoublyLinkedList` were clearly added with this in mind.
- **A real pricing & payments model.** The current `calculateRentPrice` is a
  placeholder (10% of list price + flat late fee). Define rental tariffs,
  deposits, damage charges per `BookCondition`, and integrate a payment
  provider. Represent money with `BigDecimal`/minor units, not `Long`.
- **Enforce the status state machine.** Validate transitions
  (`REQUESTED → APPROVED → ISSUED → RETURNED`, with `REJECTED`/`CANCELLED`),
  and reject illegal jumps with clear errors. Add `APPROVED`/`REJECTED` flows.
- **Deadlines & reminders.** Use `RENT_DEADLINE` to schedule due‑date reminders
  and automatic overdue handling.
- **Ownership checks.** A user should only be able to act on their own requests
  (or an admin); today any caller can process/return any `request_id`.

## 4. API design & contract

- **Return the login token in the body**, not only the `Authorization` header,
  and include token type/expiry — most clients expect a JSON body.
- **Validate all inputs.** Only `UserProfileUpdateDTO`/`WarehouseDTO` carry
  Bean‑Validation constraints; add them to `BookDTO`, `RentRequestDTO`,
  `StockDTO`, `ReviewDTO`, `UserDTO` (and validate path/query params).
- **Consistent response envelope.** Apply the new `ApiError` shape for errors
  and consider a matching success envelope; replace ad‑hoc plaintext messages.
- **Publish OpenAPI / Swagger UI** via `quarkus-smallrye-openapi` so the
  contract is discoverable and testable.
- **RESTful conventions.** Some mutations use `POST` where `PUT`/`PATCH` fit
  (e.g. follow/favourite/profile); align verbs and status codes
  (`201 Created` + `Location` on create).

## 5. Architecture & code health

- **Unify the two HTTP clients.** Decide between the generic
  config‑driven `ResourceService`/`HttpClient` engine and the MicroProfile
  `HttpClientV2` approach; maintaining both for one Google Books call is
  redundant. Note `HttpClient.send` also logs at `ERROR` level for normal
  requests — lower it.
- **Replace `CustomMap` with typed DTOs.** The `detailed/*` endpoints return a
  `CustomMap` keyed by class simple‑name, which is fragile (collisions, no
  schema). `FullUserDTO` already exists and is unused — use real DTOs.
- **Remove dead/placeholder code.** Empty `Request` base class, unused
  `ResourceConfig` paths, duplicated `configs/externalCatalog/googleBooksApi.json`
  under both `resources/` and `java/`, and `getDummyAuthor`.
- **Centralise transactions.** Transaction boundaries are split between
  class‑level `@Transactional` on repositories and method‑level annotations on
  services; standardise on the service layer.

## 6. Observability & operations

- **Health & readiness** via `quarkus-smallrye-health`; **metrics** via
  Micrometer/Prometheus.
- **Structured logging** with correlation IDs (tie into the existing
  `UserContext`), and sensible per‑package log levels; disable
  `hibernate-orm.log.sql` in production.
- **Containerisation** is scaffolded (`src/main/docker/*`); add a CI pipeline
  that builds, tests, and produces the JVM (and optionally native) image.

## 7. Testing

- **`@QuarkusTest` integration tests** with **REST‑Assured** for each endpoint,
  including auth (401/403) and validation (400) paths.
- **Testcontainers MySQL** for repository‑level tests against a real database,
  validating the JPA mappings end‑to‑end.
- **Coverage reporting** (JaCoCo) with a CI threshold.
- Note: the native IT `ResourceRestApiIT` currently just extends a unit test;
  give it real packaged‑mode assertions.

## 8. Product features on the roadmap (from the README)

- **Discussion groups** for book lovers.
- **"Book‑pedia"** browsing experience with richer search (by title, author,
  genre, ISBN) backed by the catalogue + Google Books enrichment.
- **Nearest‑warehouse availability** lookups for a user's location.
