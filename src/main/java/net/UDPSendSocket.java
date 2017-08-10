package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSendSocket {

    public static void main(String[] args) throws IOException {
        send("UDP发送端数据发送");
    }

    /*
    * 演示：创建使用UDP通信的客户端
    */
    static void send(String content) throws IOException {
        //创建发送端对象，此时创建的发送端没有指定端口
        DatagramSocket dgs = new DatagramSocket();
        //书写需要发送的数据
        String str = content;
        byte[] buf = str.getBytes();
        //指定数据发送给具体的接收端对象
        InetAddress ip = InetAddress.getByName("127.0.0.1");
        //将发送的数据进行封包,并指定接收数据的ip和端口
        DatagramPacket dgp = new DatagramPacket(buf, buf.length, ip, 10000);
        //发送数据
        dgs.send(dgp);
        //关闭发送端
        dgs.close();
    }
}
