package com.nenu.dsms.util;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;

// 文件工具类
public class FileUtil {

    public static String saveFile(MultipartFile uploadFile, String addr) {
        File dir = new File(addr);
        if (!dir.isDirectory()) {
            boolean setWritable = dir.setWritable(true, false);
            boolean mkdirs = dir.mkdirs();
        }

        String originalFilename = uploadFile.getOriginalFilename();
        assert originalFilename != null;
        File file = new File(dir, originalFilename);
        try {
            FileCopyUtils.copy(uploadFile.getInputStream(), Files.newOutputStream(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    public static File multipartFileToFile(MultipartFile m) throws IOException {
        if (m == null || m.getSize() == 0) {
            return null;
        }
        File file = new File(Objects.requireNonNull(m.getOriginalFilename()));
        InputStream inputStream = m.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(file);

        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
        inputStream.close();

        return file;
    }
}
