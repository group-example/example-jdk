## 【TCP】使用总结
### 客户端
 1. 创建TCP协议的【客户端】：使用Socket带参构造方法（指定服务端地址和端口）。
 1. 给服务端发送数据：先使用客户端对象的getOutputStream()方法获得输出流，然后写出。
 1. 获取服务端返回的数据：先使用客户端对象的getInputStream()方法输入流，然后读入。

### 服务端
 1. 创建TCP协议的【服务端】：使用ServerSocket带参构造方法（指定端口）。
 1. 获取接收到的客户端对象（Socket）：使用ServerSocket的accept方法。
 1. 获取客户端发来的数据：先使用客户端对象的getInputStream()方法获得输入流，然后读入。
 1. 给客户端返回数据：先使用客户端对象的getOutputStream()方法获得输出流，然后写出。## 【TCP】使用总结

## 【UDP】使用总结
### 接收端
 1. 创建UDP的【接收端】：使用DatagramSocket的【带参构造】方法。
 1. 创建用于接收数据的包：使用DatagramPacket的带参构造方法。
 1. 接收数据：使用DatagramSocket类的receive(DatagramPacket)方法。

### 发送端
 1. 创建UDP的【发送端】：使用DatagramSocket的【无参构造】方法。
 1. 创建用于发送数据的包：使用DatagramPacket的带参构造方法（指定服务端地址和端口号）。
 1. 发送数据：使用DatagramSocket类的send(DatagramPacket)方法。
 
 
 ## TCP和UDP区别
 参考：https://zhuanlan.zhihu.com/p/26649540；
 