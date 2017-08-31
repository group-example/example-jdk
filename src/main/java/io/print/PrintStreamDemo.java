package io.print;

import java.io.*;

/**
 * 将系统输出定向到文件。
 *
 * @author: liuluming
 * @CreatedDate: 2017/8/24 下午2:21
 */
public class PrintStreamDemo {
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/liuluming/Documents/hello.txt");
        // 此刻直接输出到屏幕
        System.out.println("hello");
        try {
            System.setOut(new PrintStream(new FileOutputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("这些内容在文件中才能看到哦！");
    }
}
