package com.trace4eu.offchain;

import org.springframework.boot.ApplicationArguments;

public class ArgumentService {
    private final ApplicationArguments args;

    public ArgumentService(ApplicationArguments args) {
        this.args = args;
    }

    public ApplicationArguments getArgs() {
        return args;
    }
}
