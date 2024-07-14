package org.bigBrotherBooks.configModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleBooksItemInfo {

    private GoogleBooksVolumeInfo volumeInfo;

    public GoogleBooksVolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(GoogleBooksVolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }
}

