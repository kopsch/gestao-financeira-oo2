package com.gestaofinanceira.exception;

public class ContaNaoEncontradaException extends RuntimeException {
    public ContaNaoEncontradaException() {
        super("A conta informada não foi encontrada no sistema.");
    }
}