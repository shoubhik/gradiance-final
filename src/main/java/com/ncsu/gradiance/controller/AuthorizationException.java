package com.ncsu.gradiance.controller;

public class AuthorizationException extends RuntimeException{

    public AuthorizationException(){
        super();
    }

    public AuthorizationException(String msg){
        super(msg);
    }

}
