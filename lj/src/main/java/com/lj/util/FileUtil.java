package com.lj.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author LiuJian:
 * @version 创建时间：2017-5-24 下午1:46:14 类说明
 */
public class FileUtil {
    /**
     * 创建文件
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static boolean createFile(String dir, String fileName)
            throws IOException {
        if (!fileIsExist(dir)) {
            if (!createDir(dir))
                return false;
        }
        if (fileIsExsit(dir, fileName)) {
            return true;
        }

        return createNewFile(dir, fileName);
    }

    public static void writeStrToFile(String dir, String fileName, String data,
                                      boolean mode) throws Exception {
        dir = formatFilePath(dir);
        writeStrToFile(dir + fileName, data, mode);
    }

    /**
     * @param filePath 文本文件全路径
     * @param data     要写入的数据
     * @param mode     是否覆盖写
     * @throws Exception
     */
    public static void writeStrToFile(String filePath, String data, boolean mode)
            throws Exception {
        File file = new File(filePath);
        writeStrToFile(file, data, mode);
    }

    /**
     * 将字符串写入文件中
     *
     * @throws Exception
     */
    public static void writeStrToFile(File file, String data, boolean mode)
            throws Exception {
        // TODO Auto-generated method stub
        if (file == null) {
            throw new Exception("writeStrToFile file is null");
        }
        if (!file.exists()) {
            throw new Exception("file not exists!");
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, mode));
        writer.write(data);
        writer.close();

    }

    /**
     * 打开文件并返回BufferedWriter
     *
     * @param dir
     * @param fileName
     * @return
     */
    public static BufferedWriter openFileBufferedWriter(String dir,
                                                        String fileName) {
        dir = formatFilePath(dir);
        checkFile(dir, fileName);
        File file = new File(dir, fileName);
        if (file.exists()) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file,
                        true));
                return writer;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 读取文本文件
     *
     * @return
     * @throws Exception
     */
    public static String readStrFromFile(String filePath) throws Exception {
        File file = new File(filePath);
        return readStrFromFile(file);
    }

    /**
     * 读取文本文件
     *
     * @param dir
     * @param fileName
     * @return
     * @throws Exception
     */
    public static String readStrFromFile(String dir, String fileName)
            throws Exception {
        dir = formatFilePath(dir);
        return readStrFromFile(dir + fileName);
    }

    /**
     * 读取文本文件
     */
    public static String readStrFromFile(File file) throws Exception {
        // TODO Auto-generated method stub
        if (file == null) {
            throw new Exception("readStrFromFile file is null");
        }
        if (!file.exists()) {
            throw new Exception("file not exists!");
        }

        BufferedReader br = new BufferedReader(new FileReader(file));
        String temp = "";
        StringBuffer sb = new StringBuffer();
        while ((temp = br.readLine()) != null) {
            sb.append(temp);
        }
        br.close();
        return sb.toString();
    }

    /**
     * 创建文件
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    private static boolean createNewFile(String dir, String fileName)
            throws IOException {
        // TODO Auto-generated method stub
        File file = new File(formatFilePath(dir) + fileName);
        return file.createNewFile();
    }

    /**
     * 判断文件是否存在
     *
     * @param filepath
     * @param fileName
     * @return
     */
    public static boolean fileIsExsit(String filepath, String fileName) {
        // TODO Auto-generated method stub
        filepath = formatFilePath(filepath);
        return fileIsExist(filepath + fileName);
    }

    public static String formatFilePath(String dir) {
        // TODO Auto-generated method stub
        if (!dir.trim().endsWith("/")) {
            dir = dir.trim() + "/";
        }
        return dir;
    }

    /**
     * 创建文件目录
     *
     * @return
     */
    public static boolean createDir(String dir) {
        // TODO Auto-generated method stub
        return (new File(dir)).mkdirs();
    }

    public static boolean fileIsExist(String filepath) {
        // TODO Auto-generated method stub
        return (new File(filepath)).exists();
    }

    /**
     * 检查文件路径，并生成文件
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public static File checkFile(String filePath, String fileName) {
        // TODO Auto-generated method stub
        try {
            if (!FileUtil.createFile(filePath, fileName)) {
                return null;
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new File(FileUtil.formatFilePath(filePath) + fileName);
    }

    public static boolean deleteDir(String dir) {
        // // 删除子文件
        // deleteFile(dir);
        // 删除根文件
        return deleteFile(dir);
    }

    /*
     * 删除文件或者文件夹下的所有文件，不包括本文件夹
     */
    public static boolean deleteFile(String srcFilePath) {
        File fileDir = new File(srcFilePath);
        if (!fileDir.exists()) {
            return true;
        }
        //删除fileDir文件或者fileDir文件夹下的所有文件
        if (fileDir.isDirectory() && fileDir.listFiles().length > 0) {
            File[] files = fileDir.listFiles();
            for (File file : files) {
                // 删除子节点
                deleteFile(file.getAbsolutePath());
                // 删除根节点
                deleteFile(file.getAbsolutePath());
            }
        } else {
            try {
                System.out.println("删除文件：" + fileDir.getAbsolutePath());
                fileDir.delete();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return fileDir.delete();
    }

    /**
     * 删除单独的文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteSingleFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return true;
        }
        return file.delete();
    }

    /**
     * @author 刘建
     * @time 2018/12/5  5:10 PM
     * @describe 写入文件中
     */
    public static int writeToFile(String dir, String name, byte[] buffer, boolean b) {
        File f = checkFile(dir, name);
        if (f == null) {
            return -1;
        }
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(f);
            os.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return -2;
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }
}
