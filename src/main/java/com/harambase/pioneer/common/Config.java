package com.harambase.pioneer.common;

import org.springframework.util.ResourceUtils;

import java.util.ResourceBundle;

public class Config {

    public static String serverPath = "";
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("server");
    public final static String TEMP_FILE_PATH = resourceBundle.getString("tempFile.path");

    public final static String SERVER_IP = resourceBundle.getString("server.ip");
    public final static int SERVER_PORT = Integer.parseInt(resourceBundle.getString("server.port"));

    public final static String FTP_PATH = resourceBundle.getString("ftp.path");
    public final static String FTP_SERVER   = resourceBundle.getString("ftp.server");
    public final static String FTP_USERNAME = resourceBundle.getString("ftp.user");
    public final static String FTP_PASSWORD = resourceBundle.getString("ftp.password");

    static {
        try {
            serverPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}