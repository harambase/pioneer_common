package com.harambase.pioneer.common.support.util;

import com.harambase.pioneer.common.Config;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.logging.Logger;

public class FileUtil {

    @Deprecated
    public static void downloadFile(String fileName, String logicalPath, HttpServletResponse response) throws Exception {

        //中文支持
        fileName = new String(fileName.getBytes(), "ISO-8859-1");
        String filePath = Config.TEMP_FILE_PATH + logicalPath;

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        FileInputStream fileInputStream = new FileInputStream(filePath);
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        byte[] bytes = new byte[2048];
        int length;
        while ((length = fileInputStream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, length);
        }
        fileInputStream.close();
        outputStream.flush();
        outputStream.close();
    }

    @Deprecated
    public static String uploadFileToPath(MultipartFile file, String dirPath) throws Exception {

        String fileName = file.getOriginalFilename();

        // 源文件目录
        String dirName = FileWriterUtil.getSecondPathByHashCode(Config.TEMP_FILE_PATH + dirPath, fileName);

        // 获取当前文件存放路径
        String filePath = FileWriterUtil.getSingleFileDirName(fileName, dirName);

        // 根据文件名生成唯一文件路径
        File imgFile = new File(filePath);

        // 写入文件到实际路径
        String imgPath = filePath.substring(Config.TEMP_FILE_PATH.length(), filePath.length());
        file.transferTo(imgFile);

        return imgPath;
    }

    public static String uploadFileToFtpServer(MultipartFile file, String server, String username, String password) {

        String fileName = file.getName();
        FTPClient ftpClient = new FTPClient();

        try {
            int reply;

            ftpClient.connect(server);
            ftpClient.login(username, password);

            System.out.println("Connected to " + server + ".");
            System.out.print(ftpClient.getReplyString());

            // After connection attempt, you should check the reply code to verify
            // success.
            reply = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                System.err.println("FTP server refused connection.");
                return null;
            }

            InputStream fis = file.getInputStream();
            ftpClient.storeFile(fileName, fis);

            ftpClient.logout();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }

        }
        return fileName;
    }

    public static void downloadFileFromFtpServer(String fileName, String logicalPath,
                                                 String server, String username, String password,
                                                 HttpServletResponse response) throws Exception {
        fileName = new String(fileName.getBytes(), "ISO-8859-1");
        String filePath = Config.TEMP_FILE_PATH + logicalPath;
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        FTPClient ftpClient = new FTPClient();

        try {
            int reply;

            ftpClient.connect(server);
            ftpClient.login(username, password);

            System.out.println("Connected to " + server + ".");
            System.out.print(ftpClient.getReplyString());

            // After connection attempt, you should check the reply code to verify
            // success.
            reply = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                System.err.println("FTP server refused connection.");
                return;
            }

            ftpClient.retrieveFile(filePath, outputStream);
            ftpClient.logout();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }

        }

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        FileInputStream fileInputStream = new FileInputStream(filePath);

        byte[] bytes = new byte[2048];
        int length;
        while ((length = fileInputStream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, length);
        }
        fileInputStream.close();
        outputStream.flush();
        outputStream.close();
    }
}
