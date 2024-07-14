# Books Api

we have used google books api in this code for fetching book info. the relevant request formats used are as follows:

1. Fetching Book Info by ISBN

```curl
curl -X GET 'https://www.googleapis.com/books/v1/volumes?q=isbn:9781451648546' \
--header 'key: [your_key]'
```

2. Fetching Book Info by Title

```curl 
curl -X GET "https://www.googleapis.com/books/v1/volumes?q=intitle:the%20shining" \
--header 'key: [your_key]' 
```

### Note:

- here we have used **Api Key** for connecting to the google books api. you can get your own api key by following the
  link below.
- any user using this project must set their own api key in the `application.properties` file.
- The request by title returns multiple books with similar titles, so its better to use isbn or other params given in
  the docs.

> [Google Books API DOC](https://developers.google.com/books/docs/v1/using) \
> [Api Key Doc](https://support.google.com/googleapi/answer/6158862)