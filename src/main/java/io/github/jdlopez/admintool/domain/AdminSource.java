package io.github.jdlopez.admintool.domain;

import lombok.Data;

import java.util.Map;

/**
 * maybe DbSource ??
 */
@Data
public class AdminSource {
    private String name;
    private String description;
    private String driver;
    private String url;
    private String username;
    private String password;
    private Map<String, String> properties;
}
