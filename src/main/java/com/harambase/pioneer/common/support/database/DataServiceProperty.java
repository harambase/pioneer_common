package com.harambase.pioneer.common.support.database;

import java.util.ResourceBundle;

class DataServiceProperty {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("application");
    final static String DRIVER = resourceBundle.getString("spring.datasource.driverClassName");
    final static String URL = resourceBundle.getString("spring.datasource.url");
    final static String USERNAME = resourceBundle.getString("spring.datasource.username");
    final static String PASSWORD = resourceBundle.getString("spring.datasource.password");

}
