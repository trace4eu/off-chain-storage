package com.trace4eu.offchain.restservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public record RestOut(ObjectNode data) {
}
