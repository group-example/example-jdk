package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/*
* 演示一：创建使用TCP协议的客户端.
*/
public class TCPSocketDemo1 {

    public static void main(String[] args) throws IOException {
        client();
    }

    static void client() throws IOException {

        System.out.println("客户端 启动.......");
//        创建客户端socket对象。明确服务端地址和端口。
        Socket s = new Socket("127.0.0.1", 10004);
//        发送数据，通过socket输出流完成。
        OutputStream out = s.getOutputStream();
        out.write("服务端，我来了".getBytes());
//        读取服务端返回的数据，通过socket输入流
        InputStream in = s.getInputStream();
        byte[] buf = new byte[1024];
        int len = in.read(buf);
        String text = new String(buf, 0, len);
        System.out.println(text);
//        关闭资源。
        s.close();
    }
}
//总结：
// 1、创建TCP协议的【客户端】：使用Socket带参构造方法（指定服务端地址和端口）。
// 2、给服务端发送数据：先使用客户端对象的getOutputStream()方法获得输出流，然后写出。
// 3、获取服务端返回的数据：先使用客户端对象的getInputStream()方法输入流，然后读入。