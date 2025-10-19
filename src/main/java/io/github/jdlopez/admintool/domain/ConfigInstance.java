package io.github.jdlopez.admintool.domain;

import lombok.Data;

import java.util.List;

@Data
public class ConfigInstance {
    private String _last;
    private String title;
    private String description;
    private List<AdminSource> sources;
    // TBD: security and staff
}
