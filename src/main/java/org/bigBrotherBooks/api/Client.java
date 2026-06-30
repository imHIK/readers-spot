package org.bigBrotherBooks.api;

import org.bigBrotherBooks.model.Request;
import org.bigBrotherBooks.model.Response;

public interface Client {
    <T> Response<T> send(Request request);
}
