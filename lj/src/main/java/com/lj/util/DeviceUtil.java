package com.lj.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Liujian on 2018/4/19 0019.
 *
 * @link http://blog.csdn.net/liujian8654562
 */

public class DeviceUtil {
    //执行普通命令
    public static String exec(String cmd) {
        if (cmd.startsWith("su") | cmd.startsWith("ping")) {
            return "不允许执行的命令";
        }
        String result = "";
        Process ps = null;

        ProcessBuilder pb = new ProcessBuilder(cmd);
        InputStream es = null;
        InputStream is = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            ps = pb.start();
            es = ps.getErrorStream();
            while ((read = es.read()) != -1) {
                baos.write(read);

            }
            baos.write('\n');
            is = ps.getInputStream();
            while ((read = is.read()) != -1) {
                baos.write(read);

            }
            byte[] data = baos.toByteArray();
            result = new String(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {


                if (es != null) {
                    es.close();
                }

                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ps != null) {
                ps.destroy();
            }
        }
        return result;
    }

    //执行root命令
    public static String rootExec(String cmd) {
        if (cmd.startsWith("su") | cmd.startsWith("ping")) {
            return "不允许执行的命令";
        }
        String result = "";
        DataOutputStream dos = null;
        DataInputStream dis = null;
        try {
            Process ps = Runtime.getRuntime().exec("sh");
            ps.waitFor();
            dos = new DataOutputStream(ps.getOutputStream());
            dis = new DataInputStream(ps.getInputStream());
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line = null;
            while ((line = dis.readLine()) != null) {
                result += "\n" + line;
                //   Message ms = new Message();
                //   ms.obj = line;
                //   handler.sendMessageDelayed(ms,1000);
            }

            ps.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {


                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {


                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
        return result;
    }
}
