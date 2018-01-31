package com.harambase.pioneer.common;

import java.util.ResourceBundle;

public class Config {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("server");

    public final static String TEMP_FILE_PATH = resourceBundle.getString("tempFile.path");
    public final static String SERVER_IP = resourceBundle.getString("server.ip");
    public final static int SERVER_PORT = Integer.parseInt(resourceBundle.getString("server.port"));

}
