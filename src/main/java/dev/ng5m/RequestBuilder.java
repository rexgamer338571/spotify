package dev.ng5m;

import java.util.Map;

public interface RequestBuilder {

    RequestBuilder header(String header, String value);
    Map<String, String> headers();

    RequestBuilder param(String param, String value);
    Map<String, String> params();

    RequestBuilder body(byte[] body);
    byte[] body();

    static RequestBuilder newBuilder() {
        return new RequestBuilderImpl();
    }



}
