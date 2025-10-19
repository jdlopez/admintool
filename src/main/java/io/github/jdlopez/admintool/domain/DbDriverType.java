package io.github.jdlopez.admintool.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DbDriverType {
    private String driverName;
    private List<String> propertyNames;
    private String urlMask;

}
