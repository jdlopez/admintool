package io.github.jdlopez.admintool.domain.database;

import lombok.Data;

@Data
public class DbColumn {
    private String name;
    private int dataType;
    private String typeName;
    private boolean nullable;
    private int size;
    private boolean autoincrement;
    private String remarks;
    // para pintado:
    private String displayLabel;

}
