package dev.ng5m;

public interface ResponseHandler<T> {

    T handle(int code, String text);

    static <T> ResponseHandler<T> gsonConverterHandler(Class<T> clazz) {
        return (code, text) -> Util.GSON.fromJson(text, clazz);
    }

}
