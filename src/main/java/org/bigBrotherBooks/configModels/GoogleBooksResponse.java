package org.bigBrotherBooks.configModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleBooksResponse {
    private List<GoogleBooksItemInfo> items;

    public List<GoogleBooksItemInfo> getItems() {
        return items;
    }

    public void setItems(List<GoogleBooksItemInfo> items) {
        this.items = items;
    }
}
