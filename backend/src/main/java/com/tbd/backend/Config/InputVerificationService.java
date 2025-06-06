package com.tbd.backend.Config;

import org.springframework.stereotype.Repository;

@Repository
public class InputVerificationService{
    //Valida caracteres que pueden ser maliciosos para la BD
    public static Boolean validateInput(String input) {
        return !input.contains(";") && !input.contains("--") &&
                !input.contains("`") && !input.contains("'") &&
                !input.contains("\"") && !input.contains("\\") &&
                !input.contains("/");
    }
}
