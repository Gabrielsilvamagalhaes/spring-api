package com.gabriel.course.projectapi2.exceptions;

public class CodeUniqueViolationException extends RuntimeException{

    public  CodeUniqueViolationException(String msg) {
        super(msg);
    }
}
