package com.trace4eu.offchain;

import org.springframework.boot.ApplicationArguments;

public class MyService {
    private final ApplicationArguments args;

    public MyService(ApplicationArguments args) {
        this.args = args;
    }

    public ApplicationArguments getArgs() {
        return args;
    }
}
