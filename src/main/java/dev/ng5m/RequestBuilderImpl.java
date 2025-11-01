package dev.ng5m;

import java.util.HashMap;
import java.util.Map;

public class RequestBuilderImpl implements RequestBuilder {
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> params = new HashMap<>();
    private byte[] body = new byte[0];

    @Override
    public RequestBuilder header(String header, String value) {
        headers.put(header, value);
        return this;
    }

    @Override
    public Map<String, String> headers() {
        return headers;
    }

    @Override
    public RequestBuilder param(String param, String value) {
        params.put(param, value);
        return this;
    }

    @Override
    public Map<String, String> params() {
        return params;
    }

    @Override
    public RequestBuilder body(byte[] body) {
        this.body = body;
        return this;
    }

    @Override
    public byte[] body() {
        return body;
    }
}
