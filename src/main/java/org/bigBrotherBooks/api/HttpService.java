package org.bigBrotherBooks.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import org.bigBrotherBooks.configModels.GoogleBooksItemInfo;
import org.bigBrotherBooks.configModels.GoogleBooksResponse;
import org.bigBrotherBooks.configModels.GoogleBooksVolumeInfo;
import org.bigBrotherBooks.logger.Logger;
import org.bigBrotherBooks.logger.LoggerFactory;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Singleton
public class HttpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpService.class);
    @RestClient
    HttpClient httpClient;

    public GoogleBooksVolumeInfo getBookById(String isbn, String key) throws Exception {
        String query = "isbn:" + isbn;
        String apiResponse = httpClient.getBooks(query, key);
        ObjectMapper mapper = new ObjectMapper();
        GoogleBooksResponse googleBooksResponse = mapper.readValue(apiResponse, GoogleBooksResponse.class);
        if (googleBooksResponse.getItems() != null && !googleBooksResponse.getItems().isEmpty()) {
            GoogleBooksItemInfo googleBooksItemInfo = googleBooksResponse.getItems().get(0);
            return googleBooksItemInfo.getVolumeInfo();
        }
        return null;
    }
}
