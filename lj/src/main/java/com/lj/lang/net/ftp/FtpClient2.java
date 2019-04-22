//package com.lj.lang.net.ftp;
//
//
//import com.lj.lang.develop.log.LjLogger;
//
//import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPClientConfig;
//import org.apache.commons.net.ftp.FTPFile;
//import org.apache.commons.net.ftp.FTPReply;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.List;
//
///**
// * @author 刘建
// * @date 2018-3-7
// * @copyright lj
// * @email 1187502892@qq.com
// */
//public class FtpClient2 implements IFtpClient {
//    private static final String TAG = "FtpClient2";
//    /**
//     * 服务器名.
//     */
//    private String hostName;
//
//    /**
//     * 端口号
//     */
//    private int serverPort;
//
//    /**
//     * 用户名.
//     */
//    private String userName;
//
//    /**
//     * 密码.
//     */
//    private String password;
//    /**
//     * FTP连接.
//     */
//    private FTPClient ftpClient;
//
//    public FtpClient2(String userName, String password, String host, int port) {
//        // TODO Auto-generated constructor stub
//        this.userName = userName;
//        this.password = password;
//        this.hostName = host;
//        this.serverPort = port;
//        ftpClient = new FTPClient();
//    }
//
//    @Override
//    public String startDownloadFile(String fileName, String filePath,
//                                    String ftpFileDir) {
//        // TODO Auto-generated method stub
//        String res = "";
//        try {
//            LjLogger.instance.log(TAG, "fileName " + fileName + ",localPath " + filePath + ",ftpFileDir " + ftpFileDir);
//            res = downloadSingleFile(ftpFileDir, filePath, fileName);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            res = "Exception :" + e.getMessage();
//        }
//        return res;
//    }
//
//    // -------------------------------------------------------文件下载方法------------------------------------------------
//
//    /**
//     * 下载单个文件，可实现断点下载.
//     *
//     * @param serverPath Ftp目录及文件路径
//     * @param localPath  本地目录
//     * @param fileName   下载之后的文件名称
//     * @throws IOException
//     */
//    private String downloadSingleFile(String serverPath, String localPath,
//                                      String fileName) throws Exception {
//        String result = "123";
//        // 打开FTP服务
//        try {
//            this.openConnect();
//            // listener.onFtpProgress(Constant.FTP_CONNECT_SUCCESS, 0, null);
//            result = "ftp openConnect sucess";
//
//
//        } catch (IOException e1) {
//            e1.printStackTrace();
//            // listener.onFtpProgress(Constant.FTP_CONNECT_FAIL, 0, null);
//            result = "ftp openConnect faild";
//        }
//        LjLogger.instance.log(TAG, result);
//
//        // 先判断服务器文件是否存在
//        FTPFile[] files = ftpClient.listFiles(serverPath);
//        if (files.length == 0) {
//            // listener.onFtpProgress(Constant.FTP_FILE_NOTEXISTS, 0, null);
//            result = "ftp files not exists";
//        } else {
//            result = "ftp files  exists";
//        }
//        LjLogger.instance.log(TAG, result);
//
//
//        // 创建本地文件夹
//        File mkFile = new File(localPath);
//        if (!mkFile.exists()) {
//            mkFile.mkdirs();
//        }
//
//        if (!localPath.endsWith("/")) {
//            localPath += "/";
//        }
//        localPath = localPath + fileName;
//
//        // 接着判断下载的文件是否能断点下载
//        long serverSize = files[0].getSize(); // 获取远程文件的长度
//        File localFile = new File(localPath);
//        if (localFile.exists()) {
//            // localSize = localFile.length(); // 如果本地文件存在，获取本地文件的长度
//            // if (localSize >= serverSize) {
//            File file = new File(localPath);
//            file.delete();
//            // }
//        }
//
//        // 进度
//        long step = serverSize / 100;
//        long process = 0;
//        long currentSize = 0;
//        // 开始准备下载文件
//        OutputStream out = new FileOutputStream(localFile, false);
//        long localSize = 0;
//        ftpClient.setRestartOffset(localSize);
//        if (!serverPath.endsWith("/")) {
//            serverPath += "/";
//        }
//        serverPath = serverPath + fileName;
//        InputStream input = ftpClient.retrieveFileStream(serverPath);
//        LjLogger.instance.log(TAG, "openFileStream " + input + " , serverPath " + serverPath);
//        byte[] b = new byte[1024];
//        int length = 0;
//        while ((length = input.read(b)) != -1) {
//            out.write(b, 0, length);
//            currentSize = currentSize + length;
//            if (currentSize / step != process) {
//                process = currentSize / step;
//                if (process % 5 == 0) { // 每隔%5的进度返回一次
//                    LjLogger.instance.log(TAG, "downLoad progress " + process + "%");
//                }
//            }
//        }
//        out.flush();
//        out.close();
//        input.close();
//
//        // 此方法是来确保流处理完毕，如果没有此方法，可能会造成现程序死掉
//        if (ftpClient.completePendingCommand()) {
//            // listener.onFtpProgress(Constant.FTP_DOWN_SUCCESS, 0, new File(
//            // localPath));
//            result = "ok";
//
//        }
//
//        // 下载完成之后关闭连接
//        this.closeConnect();
//        // listener.onFtpProgress(Constant.FTP_DISCONNECT_SUCCESS, 0, null);
//        LjLogger.instance.log(TAG, "downLoad result " + result);
//        return result;
//    }
//
//    // -------------------------------------------------------打开关闭连接------------------------------------------------
//
//    /**
//     * 打开FTP服务.
//     *
//     * @throws IOException
//     */
//    private void openConnect() throws IOException {
//        // 中文转码
//        ftpClient.setControlEncoding("UTF-8");
//        int reply; // 服务器响应值
//        // 连接至服务器
//        ftpClient.connect(hostName, serverPort);
//        // 获取响应值
//        reply = ftpClient.getReplyCode();
//        if (!FTPReply.isPositiveCompletion(reply)) {
//            // 断开连接
//            ftpClient.disconnect();
//            throw new IOException("connect fail: " + reply);
//        }
//        // 登录到服务器
//        ftpClient.login(userName, password);
//        // 获取响应值
//        reply = ftpClient.getReplyCode();
//        if (!FTPReply.isPositiveCompletion(reply)) {
//            // 断开连接
//            ftpClient.disconnect();
//            throw new IOException("connect fail: " + reply);
//        } else {
//            // 获取登录信息
//            FTPClientConfig config = new FTPClientConfig(ftpClient
//                    .getSystemType().split(" ")[0]);
//            config.setServerLanguageCode("zh");
//            ftpClient.configure(config);
//            // 使用被动模式设为默认
//            ftpClient.enterLocalPassiveMode();
//            // 二进制文件支持
//            ftpClient
//                    .setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
//        }
//    }
//
//    /**
//     * 关闭FTP服务.
//     *
//     * @throws IOException
//     */
//    private void closeConnect() throws IOException {
//        if (ftpClient != null) {
//            // 退出FTP
//            ftpClient.logout();
//            // 断开连接
//            ftpClient.disconnect();
//        }
//    }
//
//    // ---------------------------------------------------上传下载进度、删除、获取文件监听---------------------------------------------
//
//    /*
//     * 进度监听器
//     */
//    public interface FtpProgressListener {
//        /**
//         * @param currentStatus 当前FTP状态
//         * @param process       当前进度
//         * @param targetFile    目标文件
//         * @Description TODO FTP 文件长传下载进度触发
//         */
//        public void onFtpProgress(int currentStatus, long process,
//                                  File targetFile);
//    }
//
//    /*
//     * 文件删除监听
//     */
//    public interface FtpDeleteFileListener {
//        /**
//         * @param currentStatus 当前FTP状态
//         * @Description TODO 删除FTP文件
//         */
//        public void onFtpDelete(int currentStatus);
//    }
//
//    /*
//     * 获取文件监听
//     */
//    public interface FtpListFileListener {
//        /**
//         * @param currentStatus 当前FTP状态
//         * @param ftpFileList   获取的List<FTPFile>
//         * @Description TODO 列出FTP文件
//         */
//        public void onFtpListFile(int currentStatus, List<FTPFile> ftpFileList);
//    }
//
//}
