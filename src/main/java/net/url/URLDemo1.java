package net.url;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/*
* URL（Uniform Resource Locator）：中文名为统一资源定位符。
* URL的组成：
* * 协议为(protocol)：http/https/ftp/jdbc:mysql/
* * 主机为(host:port)：www.runoob.com
* * 端口号为(port): 80 ，以上URL实例并未指定端口，因为 HTTP 协议默认的端口号为 80。
* * 文件路径为(path)：/index.html
* * 请求参数(query)：language=cn
* * 定位位置(fragment)：j2se，定位到网页中 id 属性为 j2se 的 HTML 元素位置 。
* URL 书写形式：
* protocol://host:port/path?query#fragment
*
*
* 演示一：URL的简单使用
*/
public class URLDemo1 {

    public static void main(String[] args) throws IOException {

        String net = "http://baidu.com:80";
        //将要访问的资源封装成URL对象
        URL url = new URL(net);
        System.out.println(url.getHost()); //获取访问的主机
        System.out.println(url.getPort()); //获取访问的端口号
        System.out.println(url.getProtocol()); //获取使用的协议
        System.out.println(url.getQuery()); //获取携带的参数
        //获取和服务器的连接对象
        URLConnection con = url.openConnection();
        //获取输入流，从而读取到服务端发来的数据
        BufferedReader bufr = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line = null;
        while ((line = bufr.readLine()) != null) {
            //由于网站编码为UTF-8，这里需要进行转码
            String s = new String(line.getBytes(), "utf-8");
            System.out.println(s);
        }
    }
}
