package com.udacity.jwdnd.course1.cloudstorage.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Integer id, Class<?> entity) {
        super("The " + entity.getSimpleName().toLowerCase() + " with id '" + id + "' does not exist in our records");
    }
}
