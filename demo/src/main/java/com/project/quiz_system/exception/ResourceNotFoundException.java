package com.project.quiz_system.exception;

public class ResourceNotFoundException
        extends RuntimeException{

    public ResourceNotFoundException(String message){
        super(message);
    }

}