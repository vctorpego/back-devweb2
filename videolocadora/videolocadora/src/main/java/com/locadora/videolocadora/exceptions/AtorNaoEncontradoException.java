package com.locadora.videolocadora.exceptions;

public class AtorNaoEncontradoException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public AtorNaoEncontradoException (String msg){
        super(msg);
    }
}
