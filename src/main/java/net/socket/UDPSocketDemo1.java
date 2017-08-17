package net.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * UDP Socket（DatagramSocket）演示一：聊天室
 */
public class UDPSocketDemo1 {

    public static void main(String[] args) throws Exception {

        //创建【发送端】
        UDPSender s = new UDPSender("127.0.0.1",1234);

        //创建【接收端】
        UDPReceiver r = new UDPReceiver(1234);

        //创建线程并开启
        new Thread(r).start();
        Thread.sleep(1000);
        new Thread(s).start();
    }
}

// 发送端
class UDPSender implements Runnable {

    // 服务端地址
    String ip;

    // 服务端端口
    int port;

    // 发送端Socket
    DatagramSocket socket = null;

    public UDPSender(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        // 创建发送端Socket对象，此时没有指定端口。
        socket =new DatagramSocket();
    }

    public void run() {
        System.out.println("发送端已启动，请输入：");
        // 从键盘输入内容
        BufferedReader buffr = new BufferedReader(new InputStreamReader(System.in));

        try {
            String line = null;
            while ((line = buffr.readLine()) != null) {
                byte[] buf = line.getBytes();
                //指定数据发送给具体的接收端对象（指定ip和port）
                DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName(ip), port);
                // 发送数据
                socket.send(dp);
                // 如果输入的字符是over，结束发送。
                if ("886".equals(line)) {
                    socket.close();
                }
            }
        } catch (IOException e) {
        }
    }
}

// 接收端
class UDPReceiver implements Runnable {

    // 接收端端口
    int port;

    // 接收端Socket
    DatagramSocket socket = null;

    public UDPReceiver(int port) throws IOException{
        this.port=port;
        // 创建接收端对象，并指定端口。
        this.socket = new DatagramSocket(port);
    }

    public void run() {
        System.out.println("接收端已启动");
        while (true) {
            try {
                //创建数组对象，用于接收数据
                byte[] buf = new byte[1024];
                //创建用于接收发送端数据的包
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                //接收数据
                socket.receive(dp);
                //通过【数据包对象】获取发送端的相关信息
                // 获取发送端ip。
                String ip = dp.getAddress().getHostAddress();
                //获取发送端端口号
                int port = dp.getPort();
                //获取发送的数据
                String value = new String(dp.getData(), 0, dp.getLength());
                System.out.println("来自"+ip + ":" + port + "的消息:" + value);

                if (value.equals("886")) {
                    System.out.println(ip + "离开了聊天室");
                }
            } catch (IOException e) {
            }
        }
    }
}

// 【接收端】总结：
// 1、创建UDP的【接收端】：使用DatagramSocket的【带参构造】方法。
// 2、创建用于接收数据的包：使用DatagramPacket的带参构造方法。
// 3、接收数据：使用DatagramSocket类的receive(DatagramPacket)方法。

// 【发送端】总结
// 1、创建UDP的【发送端】：使用DatagramSocket的【无参构造】方法。
// 2、创建用于发送数据的包：使用DatagramPacket的带参构造方法（指定服务端地址和端口号）。
// 3、发送数据：使用DatagramSocket类的send(DatagramPacket)方法。

//

