package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/*
 * 演示二：创建使用UDP通信的发送端
 */
public class UDPSocketSendDemo1 {

    public static void main(String[] args) throws IOException {
        send();
    }

    static void send() throws IOException {
        //创建发送端对象，此时创建的发送端没有指定端口
        DatagramSocket dgs = new DatagramSocket();
        //从键盘输入内容
        BufferedReader buffr = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        while ((line = buffr.readLine()) != null) {
            byte[] buf = line.getBytes();
            //指定数据发送给具体的接收端对象
            InetAddress ip = InetAddress.getByName("127.0.0.1");
            //将发送的数据进行封包,并指定接收数据的ip和端口
            DatagramPacket dgp = new DatagramPacket(buf, buf.length, ip, 10000);
            // 发送数据
            dgs.send(dgp);
            // 如果输入的字符是over，结束发送。
            if (line.equals("over")) {
                break;
            }
        }
        //关闭发送端
        dgs.close();
    }
}
// 总结：
// 1、创建UDP的【发送端】：使用DatagramSocket的【无参构造】方法。
// 2、创建用于发送数据的包：使用DatagramPacket的带参构造方法（指定服务端地址和端口号）。
// 3、发送数据：使用DatagramSocket类的send(DatagramPacket)方法。


// TCP和UDP协议的区别：https://zhuanlan.zhihu.com/p/26649540。
