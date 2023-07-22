package com.coutrim.bibilhoteca_rest_service.exception;

import javax.transaction.Status;
import javax.ws.rs.core.Response;

public class BibliotecaException extends RuntimeException{

    private Response.Status status;

    private String message;

    public BibliotecaException(){

    }

    public BibliotecaException(String message, Response.Status status){
        super(message);
        this.status = status;
    }
}
