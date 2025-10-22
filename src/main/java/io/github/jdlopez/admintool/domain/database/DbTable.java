package io.github.jdlopez.admintool.domain.database;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DbTable {
    private String name;
    private String schema;
    private String catalog;
    private String type;
    private List<DbColumn> columns;

}
