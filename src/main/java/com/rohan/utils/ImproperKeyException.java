package com.rohan.utils;

/**
 * Custom Exception class for indicating an incorrect key
 * 
 * @author piyush
 *
 */
public class ImproperKeyException extends Exception {

    String message = null;

    private static final long serialVersionUID = 5427721028775303056L;

    public ImproperKeyException(String key, String message) {
        this.message = message + ", Key: " + key;
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
