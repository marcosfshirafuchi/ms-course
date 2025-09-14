package com.marcosfshirafuchi.hrpayroll.resources.exceptions;

import java.io.Serializable;
import java.time.Instant;

public record StandardError(Instant timestamp, Integer status, String error, String message, String path) implements Serializable {
    // O record já implementa construtor, getters, equals, hashCode e toString.
}