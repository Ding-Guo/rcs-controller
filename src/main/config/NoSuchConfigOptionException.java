package main.config;

public class NoSuchConfigOptionException extends RuntimeException {
    public NoSuchConfigOptionException(String key) {
        super(key);
    }
}