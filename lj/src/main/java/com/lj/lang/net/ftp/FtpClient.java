package com.lj.lang.net.ftp;


import com.lj.lang.develop.log.LjLogger;
import com.lj.util.FileUtil;
import com.lj.util.LjStringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class FtpClient implements IFtpClient {
//	public static final String SERVER_IP = "192.168.8.202";
//	public static final String SERVER_IP = "192.168.10.47";

//	public static final int PORT = 21;

    //    String SERVER_IP = "192.168.10.29";
//    int PORT = 2121;
    private static String Tag = FtpClient.class.getSimpleName();

    StringBuffer serverIp = new StringBuffer();

    private String userName;
    private String password;
    private String host;
    private int port;
    private int dataPort;

    public FtpClient(String userName, String password, String host, int port) {
        // TODO Auto-generated constructor stub
        this.userName = userName;
        this.password = password;
        this.host = host;
        this.port = port;
    }


    public String startDownloadFile(String fileName, String localFilePath, String ftpFileDir) {
        Socket s = null;
        String result = "";
        try {
            InetSocketAddress iad = new InetSocketAddress(host, port);
            s = new Socket();
            s.connect(iad, 15000);
            result = doFtp(s, fileName, localFilePath, ftpFileDir);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //与ftp服务器断开连接
                goodbye(s);
                if (s != null) s.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }
        LjLogger.instance.log(Tag, result);
        return result;
    }

    private String doFtp(Socket s, String fileName, String filePath, String ftpFileDir) throws IOException {
        // TODO Auto-generated method stub
        String strCmdRespons = null;
        LjLogger.instance.log(Tag, "tcp login");

        strCmdRespons = waiteResponse(s.getInputStream());
        if (!strCmdRespons.split(" ")[0].equals("220")) {
//            goodbye(s);
            return strCmdRespons;
        }
        String userNameCmd = "USER " + userName + "\r\n";
        strCmdRespons = getFtpServerFTPResponse(s, userNameCmd);
        if (!strCmdRespons.split(" ")[0].equals("331")) {//331 Send password
//            goodbye(s);
            return strCmdRespons;
        }

        String userPasswordCMD = "PASS " + password + "\r\n";
        strCmdRespons = getFtpServerFTPResponse(s, userPasswordCMD);
        if (!strCmdRespons.split(" ")[0].equals("230")) {//331 Send password
//            goodbye(s);
            return strCmdRespons;
        }

        LjLogger.instance.log(Tag, "tcp login success");

        String pwdCMD = "PWD" + "\r\n";
        strCmdRespons = getFtpServerFTPResponse(s, pwdCMD);
        if (!strCmdRespons.split(" ")[0].equals("257")) {//257 "/"
//            goodbye(s);
            return strCmdRespons;
        }

//        String cwd = "CWD " + "\\DeskCard\r\n";
        String cwd = "CWD " + ftpFileDir + "\r\n";
        strCmdRespons = getFtpServerFTPResponse(s, cwd);
        if (!strCmdRespons.split(" ")[0].equals("250"))    //250 CWD command successful.
        {
//            goodbye(s);
            return strCmdRespons;
        }

        // 获取文件大小
        String size = "SIZE " + fileName + "\r\n";
        strCmdRespons = getFtpServerFTPResponse(s, size);

        LjLogger.instance.log(Tag, "tcp fileSize " + strCmdRespons);
        LjLogger.instance.log(Tag, "tcp filePath " + filePath);
        LjLogger.instance.log(Tag, "tcp fileName " + fileName);

        String[] fileSizeResult = strCmdRespons.split(" ");

        //获取文件大小是否正确，如果不正确直接返回
        if (fileSizeResult.length < 2) {
            return strCmdRespons;
        }

        String serverCode = fileSizeResult[0];//获取文件大小返回码

        //下载文件
        //如果能获取到文件大小，我们就下载文件，否则不下载
        if (serverCode.equals("213"))    //213 1442273
        {
            //获取文件大小
            String sizeStr = LjStringUtil.formatNumString(fileSizeResult[1]);
            long remoteFileSize = Long.parseLong(sizeStr.equals("") ? "0" : sizeStr);//文件大小

            pasvServerDataSocketIp(s);
            LjLogger.instance.log(Tag, "tcp downLoadFile");
            String downState = downLoadData(s, serverIp.toString(), dataPort, filePath, fileName);
            LjLogger.instance.log(Tag, "tcp downLoadFile result " + downState);

            File downLoadFile = new File(filePath, fileName);
            //检查文件是否下载成功
            if (!downState.equals("ok") || downLoadFile.length() != remoteFileSize) {//文件未下载成功。直接退出方法，并删除文件。
                FileUtil.deleteSingleFile(downLoadFile.getAbsolutePath());
                return "download file failed " + downState;
            }

        } else {
            return fileSizeResult[1];
        }


        return "ok";
    }

    private void goodbye(Socket s) throws IOException {
        String close = "QUIT" + "\r\n";
        getFtpServerFTPResponse(s, close);
//        if (!strCmdRespons.split(" ")[0].equals("221"))    //221 Goodbye.
//        {
//            return strCmdRespons;
//        }
//        return "ok";
    }


    private String pasvServerDataSocketIp(Socket s) throws IOException {
        // TODO Auto-generated method stub
        String strCmdRespons = null;
        String mode = "PASV" + "\r\n";
        strCmdRespons = getFtpServerFTPResponse(s, mode);
        if (!strCmdRespons.split(" ")[0].equals("227")) {//227 Entering Passive Mode (192,168,10,47,208,134).
            return strCmdRespons;
        } else {
            String[] serverDateArrayTemp = null;

            serverDateArrayTemp = strCmdRespons.split(" ");
            String serverDateSocketIPArrayStr = serverDateArrayTemp[serverDateArrayTemp.length - 1];
            serverDateArrayTemp = serverDateSocketIPArrayStr.substring(serverDateSocketIPArrayStr.indexOf("(") + 1, serverDateSocketIPArrayStr.indexOf(")")).split(",");
            for (int i = 0; i < 4; i++) {
                serverIp.append(serverDateArrayTemp[i] + ".");
            }
            serverIp.replace(serverIp.length() - 1, serverIp.length(), "");
            dataPort = Integer.parseInt(serverDateArrayTemp[4]) * 256 + Integer.parseInt(serverDateArrayTemp[5]);
        }
        return "ok";
    }

    private String getFtpServerFTPResponse(Socket s, String cmd) throws IOException {
        // TODO Auto-generated method stub
        OutputStream os = s.getOutputStream();
        InputStream is = s.getInputStream();

        os.write((cmd).getBytes("GBK"));
        os.flush();
        return waiteResponse(is);

    }

    private String waiteResponse(InputStream is) {
        // TODO Auto-generated method stub
        byte[] buffer = new byte[1024];
        StringBuffer sb = new StringBuffer();
        int cursor = 0;
        try {
            cursor = is.read(buffer);
            sb.append(new String(buffer, 0, cursor));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String downLoadData(Socket s, String host, int port, String filePath, String fileName) throws IOException {
        // TODO Auto-generated method stub
        MyHandleFTPDataClient mMyHandleFTPDataClient = new MyHandleFTPDataClient(host, port);

        mMyHandleFTPDataClient.connectServer();

        String strCmdRespons = excRETR(s, fileName);

        if (!strCmdRespons.equals("ok")) {
            return strCmdRespons;
        }

        String downLoadState = mMyHandleFTPDataClient.downLoadFile(filePath, fileName);
        if (!downLoadState.equals("ok")) {
            return downLoadState;
        }
        strCmdRespons = waiteResponse(s.getInputStream());
        if (!strCmdRespons.split(" ")[0].equals("226")) {
            return strCmdRespons;
        }

        return "ok";
    }

    private String excRETR(Socket s, String fileName) throws IOException {
        // TODO Auto-generated method stub
        String strCmdRespons = null;
        String downLoadCmd = "RETR " + fileName + "\r\n";
        strCmdRespons = getFtpServerFTPResponse(s, downLoadCmd);
//		if(!strCmdRespons.split(" ")[0].equals("125")){//125 ���������Ѿ���.
//			return strCmdRespons ;
//		}
        return "ok";
    }


}

class MyHandleFTPDataClient {
    private Socket s = null;
    private String host;
    private int port;
    private byte[] buffer = new byte[1024];

    public MyHandleFTPDataClient(String host, int port) {
        // TODO Auto-generated constructor stub
        this.host = host;
        this.port = port;
    }

    public void connectServer() throws UnknownHostException, IOException {
        s = new Socket(host, port);
    }

    public String downLoadFile(String filePath, String fileName) {
        // TODO Auto-generated method stub\
        File targetFile = FileUtil.checkFile(filePath, fileName);
        OutputStream file_os = null;
        InputStream is = null;
        OutputStream os = null;
        try {

            file_os = new FileOutputStream(targetFile, false);

            is = s.getInputStream();
            os = s.getOutputStream();

            int cusor = 0;
            while ((cusor = is.read(buffer)) != -1) {
                file_os.write(buffer, 0, cusor);
            }
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "UnknownHostException";

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "IOException";
        } finally {
            try {
                if (is != null) is.close();
                if (os != null) os.close();
                if (s != null) s.close();
                if (file_os != null) file_os.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return "ok";
    }


}
