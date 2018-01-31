package com.harambase.pioneer.common.support.util;

import com.harambase.pioneer.common.Config;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class FileUtil {

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
}
