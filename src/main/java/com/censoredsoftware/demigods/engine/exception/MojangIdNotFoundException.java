package com.censoredsoftware.demigods.engine.exception;

public class MojangIdNotFoundException extends NullPointerException {
    public MojangIdNotFoundException(String playerName) {
        super("A Mojang Id for \"" + playerName + "\" could not be found.");
    }
}
