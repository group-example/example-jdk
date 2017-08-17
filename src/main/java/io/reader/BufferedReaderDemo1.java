package io.reader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * 实现一个字符缓冲流。
 * 缓冲区目的：减少文件读写次数，提高数据访问的效率。
 * 缓冲区原理：
 * 1、 使用了底层流对象从设备上一次读取多个数据，并将读取到的数据存储到缓冲区的"数组"中；
 * 2、 读取一个字符：当需要使用数据时，通过缓冲区的read方法从缓冲区来获取具体的字符数据；如果缓冲区数据为空，继续从设备上读取。
 * 3、 读取一行字符：使用read方法读取字符数据，并存储到另外一个容器中，直到读取到了换行符。
 */
public class BufferedReaderDemo1 {

    private Reader r;

    // 定义一个字符数组,作为缓冲区。
    private char[] buf = new char[1024];

    // 定义了一个索引，用于操作数组中的元素。
    private int index = 0;

    // 定义了一个变量，用于记录未读取的字符的个数。
    private int count = 0;

    // 需要一初始化就具备一个流对象。
    public BufferedReaderDemo1(Reader r) {// 可以对Reader的所有子类进行高效读取。
        this.r = r;
    }

    // 从键盘读取字符，测试我们实现的字符缓冲流。
    public static void main(String[] args) throws IOException{

        // BufferedReader使用时必须接收字符流对象，而键盘录入是字节流，因此需要将字节流转字符流：InputStreamReader。
        BufferedReaderDemo1 bufr = new BufferedReaderDemo1(new InputStreamReader(System.in));
        String line = null;
        while((line=bufr.readLine())!=null){//键盘录入记住定义结束标记。强制结束。
            if("over".equals(line)){
                break;
            }
            System.out.println("您输入的是："+line);
        }
    }


    public int read() throws IOException {
        // 需要先通过流对象从底层设备上获取一定数据的数据到缓冲区数组中。 使用流对象read(char[]);
        //如果count记录字符个数的变量为0，说明缓冲区已经没有字符数据。
        if (count == 0) {
            //需要从设备上获取一定数量的数据存储到缓冲区中，并用count记录存储字符的个数。
            count = r.read(buf);
            //每取一次新的数据，就需要将角标归0.
            index = 0;
        }
        //如果count小于0，说明到-1，没有数据了，程序直接返回-1.
        if (count < 0) {
            return -1;
        }
        //从缓冲区中取出一个字符。
        char ch = buf[index];

        //角标自增。
        index++;
        //计数器要自减。
        count--;
        return ch;
    }

    //基于高效的read方法，建立一个一次可以读取一行的数据的方法。  将行终止符前的数据转成字符串返回。
    public String readLine() throws IOException {
        //1,定义一个临时容器。
        StringBuilder sb = new StringBuilder();
        //2,调用本类中的read方法，从缓冲区中读取一个字符，存储到临时容器中。
        //存的时候要注意：必须判断，如果是行终止符就不要存储了。就将临时容器中的
        //字符转成字符串返回。
        int ch = 0;
        while ((ch = this.read()) != -1) {
            if (ch == '\r') {
                continue;
            }
            if (ch == '\n') {
                return sb.toString();
            }
            sb.append((char) ch);//将读取到的字符数字转成char类型，存储到sb中。
        }
        //万一文本中最后一行没有行终止符，判断一下sb中是否有内容，如果有则返回。
        if (sb.length() != 0) {
            return sb.toString();
        }
        return null;
    }


    // 关闭流资源。
    public void close() throws IOException {
        // 其实内部就是关闭具体的流。
        r.close();
    }

}

