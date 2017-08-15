package net.socket;

import java.io.*;
import java.lang.annotation.Annotation;
import java.net.ServerSocket;
import java.net.Socket;

/*
* TCP Socket 演示二：模拟浏览器的请求和响应.
*/
public class TCPSocketDemo2 {

    public static void main(String[] args) throws Exception {

        // 创建服务器端对象
        ServerSocket ss = new ServerSocket(9999);

        // ===========================获取：友好类和包内访问常量==============
        new MyPackageMethod().myPackageMethod();
        System.out.println(MyPackageConst.PACKAGE_STRING);

        // ===========================获取：包上注解=========================
        Package pkg = Package.getPackage("net.socket");
        for (Annotation annotation : pkg.getAnnotations()) {
            System.out.println(annotation.annotationType().getName());
        }

        while (true) {
            // 获取客户端对象
            Socket s = ss.accept();
            // 一个客户端启用一个线程
            new Thread(new TCPServer2(s)).start();
        }
    }
}

// 服务端
class TCPServer2 implements Runnable {
    Socket s = null;

    public TCPServer2(Socket s) {
        this.s = s;
    }

    public void run() {
        try {
            // 接收浏览器发送来的数据
            byte[] buf = new byte[1024];
            int len = s.getInputStream().read(buf);
            String text = new String(buf, 0, len);
            System.out.println(text);
            s.getOutputStream().write("<font color='red' size='7'>欢迎光临</font>".getBytes());
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// 客户端
class TCPClient2 {

    // 服务端地址
    static String ip = "127.0.0.1";

    // 服务端端口
    static int port = 9999;

    public static void main(String[] args) throws Exception {
        send();
    }

    static void send() throws IOException {

        // 创建socket。
        Socket s = new Socket(ip, port);
        // 获得socket输出流。
        OutputStream out = s.getOutputStream();

        PrintWriter writer = new PrintWriter(out, true);
        writer.println("GET /myweb/1.html HTTP/1.1");
        writer.println("Accept: */*");
        writer.println("Host: 127.0.0.1:8080");
        writer.println("Connection: close");
        writer.println();//空行。

        // 告诉服务器端图片数据发送完毕，不要等着读了。
        s.shutdownOutput();
        // 获取返回信息。
        InputStream in = s.getInputStream();
        byte[] bufIn = new byte[1024];
        int lenIn = in.read(bufIn);
        System.out.println(new String(bufIn, 0, lenIn));
        // 关闭。
        s.close();
    }

}

//【客户端】总结：
// 1、创建TCP协议的【客户端】：使用Socket带参构造方法（指定服务端地址和端口）。
// 2、给服务端发送数据：先使用客户端对象的getOutputStream()方法获得输出流，然后写出。
// 3、获取服务端返回的数据：先使用客户端对象的getInputStream()方法输入流，然后读入。

//【服务端】总结：
// 1、创建TCP协议的【服务端】：使用ServerSocket带参构造方法（指定端口）。
// 2、获取接收到的客户端对象（Socket）：使用ServerSocket的accept方法。
// 3、获取客户端发来的数据：先使用客户端对象的getInputStream()方法获得输入流，然后读入。
// 4、给客户端返回数据：先使用客户端对象的getOutputStream()方法获得输出流，然后写出。