package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/*
 * 演示：创建使用UDP协议的接收端
 */
public class UDPReceiveSocket {

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
        //关闭接收端
//        socket.close();
    }
}
