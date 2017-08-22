package io.file;

import java.io.*;
import java.util.*;

/**
 * 文件的切割与合并：
 */
public class FileDemo1 {

    private static final int BUFFER_SIZE = 1048576;// 1024*1024
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void main(String[] args) throws Exception {
        File srcFile = new File("/Users/liuluming/Downloads/fdm.dmg");
        File partsDir = new File("/Users/liuluming/Downloads/fdm");
//        splitFile(srcFile, partsDir);
        mergeFile(partsDir);
    }

    /**
     * 切割文件。
     * * 切割思路：
     * 1，读取源文件，将源文件的数据分别复制到多个文件中。
     * 2，切割方式有两种：按照碎片个数切；按照指定大小切。
     * 3，一个输入流对应多个输出流。
     * 4，每一个碎片都需要编号，顺序不要错。
     * 5，将源文件以及切割的一些信息也保存起来随着碎片文件一起发送。
     */
    public static void splitFile(File srcFile, File partsDir)
            throws IOException {

        // 健壮性的判断。
        if (!(srcFile.exists() && srcFile.isFile())) {
            throw new RuntimeException("源文件不是正确的文件或者不存在");
        }
        if (!partsDir.exists()) {
            partsDir.mkdirs();
        }
        // 1，使用字节流读取流和源文件关联。
        FileInputStream fis = new FileInputStream(srcFile);
        // 2，明确目的。目的输出流有多个，只创建引用。
        FileOutputStream fos = null;
        // 3，定义缓冲区。1M.
        byte[] buf = new byte[BUFFER_SIZE];// 1M
        // 4，频繁读写操作。
        int len = 0;
        int count = 0;// 碎片文件的编号。
        while ((len = fis.read(buf)) != -1) {
            // 创建输出流对象。只要满足了缓冲区大小，碎片数据确定，直接往碎片文件中写数据 。
            // 碎片文件存储到partsDir中，名称为编号+part扩展名。
            fos = new FileOutputStream(new File(partsDir, (++count) + ".part"));
            // 将缓冲区中的数据写入到碎片文件中。
            fos.write(buf, 0, len);
            // 直接关闭输出流。
            fos.close();
        }
        // 将源文件以及切割的一些信息也保存起来随着碎片文件一起发送。
        String filename = srcFile.getName();
        int partCount = count;
        // 创建一个输出流。
        fos = new FileOutputStream(new File(partsDir, "info.properties"));
        fos.write(("filename=" + filename + LINE_SEPARATOR).getBytes());
        fos.write(("partcount=" + Integer.toString(partCount)).getBytes());
        fos.close();
        fis.close();
    }

    /**
     * 合并文件。
     *
     * 使用SequenceInputStream序列流，将拆分的文件合并成一个。
     * @param
     * @return
     */

    public static void mergeFile(File partsDir) throws Exception {

        // 获取目标目录下的配置文件。
        // 判断碎片文件目录中是否存在properties文件。使用过滤器完成。
        File[] files = partsDir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".properties");
            }
        });
        if (files.length != 1) {
            throw new Exception("properties扩展名的文件不存在，或不唯一");
        }

        Properties properties = new Properties();
        InputStream in = new FileInputStream(files[0]);
        properties.load(in);

        // 从配置文件中获得文件名、文件个数等信息。
        String fileName = properties.getProperty("filename");
        int amout = new Integer(properties.getProperty("partcount"));


        // 合并所有文件
        List<FileInputStream> list = new ArrayList<FileInputStream>();
        for (int i = 1; i <= amout; i++) {
            FileInputStream fileInputStream = new FileInputStream(new File(partsDir, i + ".part"));
            list.add(fileInputStream);
        }
        Enumeration<FileInputStream> en = Collections.enumeration(list);

        SequenceInputStream sequenceInputStream = new SequenceInputStream(en);

        // 设置合并后文件保存目的地

        FileOutputStream fileOutputStream = new FileOutputStream(new File(partsDir, fileName));
        // 写入到磁盘
        byte[] buf = new byte[BUFFER_SIZE];
        int len = 0;
        while ((len = sequenceInputStream.read(buf)) != -1) {
            fileOutputStream.write(buf, 0, len);
        }
        fileOutputStream.close();
        sequenceInputStream.close();
    }


}
