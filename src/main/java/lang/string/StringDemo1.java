package lang.string;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * 按字节截取字符串。
 * 需求：对字符串按照字节数截取(默认码表)。
 * 比如："abc你好" 有5个字符，有7个字节。比如：按照3个字节截取 abc，结果是abc ；按照四个字节截取 ，实际保存了abc和"你"字的一半，但结果只显示abc，"你"字的一半字节舍弃。
 * 思路：
 * 1、一个中文gbk编码占用两个字节（utf-8三个字节），且都是负数，比如："你好"的字节是 -60 -29 -70 -61。
 * 2、先判断最后一位是不是负数。如果不是，直接截取就可以了；如果是，再判断一下该负数之前连续出现了几个负数。
 * 3、因为中文两个负数字节，出现的负数的个数如果是偶数，不需要舍弃；如果是奇数，就舍弃最后一个字节。
 */
public class StringDemo1 {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = "abc你好cd谢谢！";
        byte[] buf = str.getBytes("GBK");
        for (int i = 0; i < buf.length; i++) {
            String s = cutString(str, i + 1);
            System.out.println(str + ",截取" + (i + 1) + "个结果是:" + s);
        }
    }

    public static String cutString(String str, int len) throws UnsupportedEncodingException {
        //1,将字符串编码成字节数组。
        byte[] buf = str.getBytes("GBK");
        int count = 0;
        //2,对数组进行遍历。从【截取位】开始【往回】遍历。
        for (int i = len - 1; i >= 0; i--) {
            //判断最后截取位上是否是负数
            if (buf[i] < 0) {
                count++;
            } else {
                break;
            }
        }

        //判断奇偶数。
        if (count % 2 == 0) {
            return new String(Arrays.copyOfRange(buf, 0, len), "gbk");
        } else {
            return new String(Arrays.copyOfRange(buf, 0, len - 1), "gbk");//舍弃最后一个。
        }
    }

}
