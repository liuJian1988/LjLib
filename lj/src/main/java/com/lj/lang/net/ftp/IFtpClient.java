package com.lj.lang.net.ftp;

/**
 * Created by Liujian on 2018/3/7 0007.
 *
 * @link http://blog.csdn.net/liujian8654562
 */

public interface IFtpClient {
    String startDownloadFile(String fileName, String localFileDir, String ftpFileDir);
}
