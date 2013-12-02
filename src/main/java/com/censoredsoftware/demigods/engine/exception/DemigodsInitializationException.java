package com.censoredsoftware.demigods.engine.exception;

public class DemigodsInitializationException extends ExceptionInInitializerError {
    public DemigodsInitializationException() {
        super("Odds must be between 1 and 100.");
    }
}
