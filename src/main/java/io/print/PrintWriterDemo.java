package io.print;

import java.io.*;

/**
 * @author: liuluming
 * @CreatedDate: 2017/8/24 下午3:04
 */
public class PrintWriterDemo {
    public static void main(String[] args) throws Exception{

        byte[] sim = {(byte) 0xbc, (byte) 0xf2, // 简
                (byte) 0xcc, (byte) 0xe5, // 体
                (byte) 0xd6, (byte) 0xd0, // 中
                (byte) 0xce, (byte) 0xc4}; // 文
        InputStreamReader inputStreamReader = new InputStreamReader(new ByteArrayInputStream(sim), "GB2312");
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(System.out, "GB2312"));
        PrintStream printStream = new PrintStream(System.out, true, "GB2312");
        int in;
        while ((in = inputStreamReader.read()) != -1){

            printWriter.println((char) in);

            printStream.println((char) in);

        }
        inputStreamReader.close();
        printWriter.close();
        printStream.close();

    }


}
