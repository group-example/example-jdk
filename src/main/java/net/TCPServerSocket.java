package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


/*
 * 演示：创建使用TCP协议的服务端.
 */
public class TCPServerSocket {

    public static void main(String[] args) throws IOException {
        serve();
    }

    static void serve() throws IOException {

        System.out.println("服务端2启动.....");
        // 创建tcp服务端socket 明确端口。
        ServerSocket ss = new ServerSocket(10004);
        while (true) {
            // 获取客户端对象。
            Socket s = ss.accept();
            System.out.println(s.getInetAddress().getHostAddress()
                    + ".....connected");
            // 读取客户端的发送过来的数据
            InputStream in = s.getInputStream();
            byte[] buf = new byte[1024];
            int len = in.read(buf);
            String text = new String(buf, 0, len);
            System.out.println(text);
            // 给客户端回馈数据。
            OutputStream out = s.getOutputStream();
            out.write("客户端，我已到收到，哦耶！".getBytes());
            // 关闭客户端
            s.close();
        }
        // 关闭服务端。如果不断的获取客户端，不用关闭服务端。
//        ss.close();
    }
}
