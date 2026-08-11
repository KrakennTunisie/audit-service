package com.kerp.auditservice.domain.model;

import java.util.List;

public record Actor(
        String userId,
        String firstName,
        String lastName,
        List<String> roles)
{}
