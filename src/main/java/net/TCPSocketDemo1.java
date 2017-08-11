package net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/*
* TCP Socket 演示一：文件上传.
*/
public class TCPSocketDemo1 {

    public static void main(String[] args) throws Exception {

        // 创建服务器端对象
        ServerSocket ss = new ServerSocket(9999);

        while (true) {
            // 获取客户端对象
            Socket s = ss.accept();
            // 一个客户端启用一个线程
            new Thread(new TCPServer(s)).start();
        }
    }
}

// 服务端
class TCPServer implements Runnable {
    Socket s = null;

    public TCPServer(Socket s) {
        this.s = s;
    }

    public void run() {
        try {
            // 获取连接的客户的IP
            String ip = s.getInetAddress().getHostAddress();
            // 从客户端获取输入流
            InputStream in = s.getInputStream();
            // 创建输出的路径对象
            File dir = new File("./log/");
            if (!dir.exists()) {
                dir.mkdir();
            }
            int count = 1;
            // 创建输出的文件对象
            // TODO 获得文件信息
            File filePic = new File(dir, ip + "-" + count + ".png");
            while (filePic.exists()) {
                count++;
                filePic = new File(dir, ip + "-" + count + ".png");
            }
            // 创建文件输出流对象
            FileOutputStream fos = new FileOutputStream(filePic);
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            // 设置返回信息
            OutputStream out = s.getOutputStream();
            out.write("上传成功".getBytes());
            fos.close();
            s.close();
        } catch (IOException e) {
        }
    }
}

// 客户端
class TCPClient {

    // 服务端地址
    static String ip = "127.0.0.1";

    // 服务端端口
    static int port = 9999;

    public static void main(String[] args) throws Exception {
        send("/Users/liuluming/Documents/Java IO.png");
    }

    static void send(String filePath) throws IOException {
        System.out.println("开始上传文件......");
        // 创建socket。
        Socket s = new Socket(ip, port);
        // 读取源文件。
        File picFile = new File(filePath);
        if (!picFile.exists()) {
            throw new IOException("文件不存在");
        }
        FileInputStream fis = new FileInputStream(picFile);

        // 获得socket输出流。
        // TODO 封装文件信息（SequenceInputStream）
        OutputStream out = s.getOutputStream();
        byte[] buf = new byte[1024];
        int len = 0;
        // 让输出流从文件读取内容弄。
        while ((len = fis.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        // 告诉服务器端图片数据发送完毕，不要等着读了。
        s.shutdownOutput();
        // 获取返回信息。
        InputStream in = s.getInputStream();
        byte[] bufIn = new byte[1024];
        int lenIn = in.read(bufIn);
        System.out.println(new String(bufIn, 0, lenIn));
        // 关闭。
        fis.close();
        System.out.println("结束上传文件......");
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