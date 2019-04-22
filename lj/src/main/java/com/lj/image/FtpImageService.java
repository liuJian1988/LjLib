package com.lj.image;

import com.lj.app.AppExecutors;
import com.lj.inter.ITask;
import com.lj.lang.net.ftp.FtpClient;
import com.lj.lang.net.ftp.IFtpClient;

/**
 * 作者：liujian on 2019/3/11 10:40
 * 邮箱：15313727484@163.com
 * 采用的Builder方式，目的是为了采用链式调用，可以便对FtpImageService的扩张，参数的设置。
 */
public class FtpImageService implements ImageService {

    private IFtpClient ftpClient;
    private String baseSdCard;

    public FtpImageService(Builder builder) {
        this.ftpClient = builder.ftpClient;
        this.baseSdCard = builder.baseSdCard;
    }

    @Override
    public void getImage(final String ftpDir, final CallBack callBack) {
        //1、采用异步下载的方式开始下载
        //2、下载完毕调用CallBack方法返回结果

        AppExecutors.instance.networkIO().execute(new ITask() {
            private String ftpPath = ftpDir;

            @Override
            public void run() {
                ftpPath = ftpPath.replace("\\", "/");
                //1、ftp_path带有文件名，我们把文件名和路径切割出来
                String fileName = "";
                String ftp_path = "";
                ftp_path = ftpPath.substring(0, ftpPath.lastIndexOf("/"));
                fileName = ftpPath.substring(ftpPath.lastIndexOf("/") + 1, ftpPath.length());


                //下载图片
                String result = ftpClient.startDownloadFile(fileName, baseSdCard + "/" + ftp_path, ftp_path);
                if (result.equals("ok")) {
                    //下载完毕
                    callBack.onSuccess(baseSdCard + "/" + ftpPath);
                } else {
                    //下载失败
                    callBack.onFailed(result);
                }
            }
        });
    }

    public static final class Builder {
        private String host;
        private String userName;
        private String passWord;
        private int port;
        private String baseSdCard;
        private IFtpClient ftpClient;


        public Builder() {
        }

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setPassWord(String passWord) {
            this.passWord = passWord;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setClient(FtpClient ftpClient) {
            this.ftpClient = ftpClient;
            return this;
        }

        public Builder setBaseSdCardPath(String baseSdCard) {
            this.baseSdCard = baseSdCard;
            return this;
        }

        public FtpImageService build() {

            if (ftpClient == null) {
                ftpClient = new FtpClient(userName, passWord, host, port);
            }

            return new FtpImageService(this);
        }


    }
}
