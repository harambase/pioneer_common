package com.harambase.pioneer.common.support.util;

import com.harambase.pioneer.common.Config;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class FileUtil {

    private final static Logger logger = LoggerFactory.getLogger("FileUtil");

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

    public static void downloadFileFromFtpServer(HttpServletResponse response, String fileName, String filePath, String server, String username, String password) throws Exception {

        byte[] bytes = fileByteFromFtp(filePath, server, username, password);
        fileName = new String(fileName.getBytes(), "ISO-8859-1");

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.write(bytes);
        out.flush();
        out.close();
    }

    public static String uploadFileToFtpServer(MultipartFile file, String dir, String server, String username, String password) {

        String fileName = file.getOriginalFilename();
        String dirName = FileWriterUtil.getSecondPathByHashCode(dir, fileName);
        String filePath = FileWriterUtil.getSingleFileDirName(fileName, dirName);
        String fileLogicalName = getFileLogicalName(filePath);

        FTPClient ftpClient = new FTPClient();

        try {
            int reply;

            ftpClient.connect(server);
            ftpClient.login(username, password);

            /*
             二进制方式，首先二进制方式保证了文件内容所有数据位都是重要的。经过验证在传输文件类型为非文本内容的文件，使用ASCII传输方式造成copy的文件已经损坏。
             */
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE); //大坑！！！！！

            logger.info("FTP: Connected to " + server + ".");
            logger.info("FTP: " + ftpClient.getReplyString());

            reply = ftpClient.getReplyCode();

            //Success: reply >= 200 && reply < 300;
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                logger.error("FTP: FTP server refused connection.");
                return null;
            }

            ftpClient.makeDirectory(dirName);
            ftpClient.changeWorkingDirectory(dirName);
            InputStream fis = file.getInputStream();
            ftpClient.storeFile(fileLogicalName, fis);

            ftpClient.logout();

            return filePath;

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;

        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }

        }

    }

    public static boolean deleteFileFromFTP(String filePath, String server, String username, String password) {

        FTPClient ftpClient = new FTPClient();
        boolean error;

        try {
            int reply;

            ftpClient.connect(server);
            ftpClient.login(username, password);

            logger.info("FTP: Connected to " + server + ".");
            logger.info("FTP: " + ftpClient.getReplyString());

            reply = ftpClient.getReplyCode();

            //Success: reply >= 200 && reply < 300;
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                logger.error("FTP: FTP server refused connection.");
                return false;
            }

            error = ftpClient.deleteFile(filePath);
            if (error) {
                logger.error("FTP: FTP file deletion failed. File path: " + filePath);
                return false;
            }

            ftpClient.logout();
            return true;

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }

        }

    }

    private static String getFileLogicalName(String filePath) {
        String[] paths = filePath.split("/");
        return paths[paths.length - 1];
    }

    private static String getFileDirPath(String filePath) {
        String[] paths = filePath.split("/");
        String dirPath = "/";
        for (int i = 1; i < paths.length - 1; i++) {
            dirPath += paths[i] + "/";
        }
        return dirPath;
    }

    private static byte[] fileByteFromFtp(String filePath, String server, String username, String password) {

        byte[] bytes = {};
        FTPClient ftpClient = new FTPClient();

        try {
            int reply;

            ftpClient.connect(server);
            ftpClient.login(username, password);
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.setBufferSize(1024);

            logger.info("FTP: Connected to " + server + ".");
            logger.info("FTP: " + ftpClient.getReplyString());

            // After connection attempt, you should check the reply code to verify
            // success.
            reply = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                logger.error("FTP: FTP server refused connection.");
                return bytes;
            }

            String fileLogicalName = getFileLogicalName(filePath);
            String dirPath = getFileDirPath(filePath);
            ftpClient.changeWorkingDirectory(dirPath);
            logger.info("FTP: FTP current working directory = " + ftpClient.printWorkingDirectory());

            InputStream ftpInputStream = ftpClient.retrieveFileStream(fileLogicalName);
            bytes = IOUtils.toByteArray(ftpInputStream);

            ftpInputStream.close();
            ftpClient.logout();

            return bytes;

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return bytes;
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }

        }
    }

}
