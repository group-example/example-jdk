package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/*
 * 演示二：创建使用UDP协议的接收端
 */
public class UDPSocketReceiveDemo1 {

    public static void main(String[] args) throws IOException {
        receive();
    }


    static void receive() throws IOException {
        //创建接收端对象，由于发送断是给10000端口上发送，创建接收端的时候，端口需要和发送端一致，否则收不到数据
        DatagramSocket socket = new DatagramSocket(10000);
        while (true) {
            //创建数组对象，用于接收数据
            byte[] buf = new byte[1024];
            //创建用于接收发送端发送过来的数据包
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            //接收数据
            socket.receive(packet);
            //通过数据包对象获取发送端发送来的数据和发送端的信息
            String ip = packet.getAddress().getHostAddress();
            //获取发送端的端口号
            int port = packet.getPort();
            //获取发送的数据
            String value = new String(packet.getData(), 0, packet.getLength());
            //打印接收的数据
            System.out.println(ip + ":" + port + ":" + value);
        }
        //关闭接收端。如果不断的获取客户端，不用关闭服务端。
//        socket.close();
    }
}

// 总结：
// 1、创建UDP的【接收端】：使用DatagramSocket的【带参构造】方法。
// 2、创建用于接收数据的包：使用DatagramPacket的带参构造方法。
// 3、接收数据：使用DatagramSocket类的receive(DatagramPacket)方法。

