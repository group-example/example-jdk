package io.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取指定目录下所有的.java文件(包含子目录中的)，并将这些java文件的绝路路径写入到一个文件中。建立一个java文件清单列表。
 * 思路：
 * 1，一看到包含子目录，必须递归。
 * 2，写数据到文件，输出流。
 * 3，继续分析，发现只要.java ，需要过滤器。
 * 4，满足过滤的条件的文件有可能非常多，先进行存储。
 */
public class FileDemo3 {

    private static final String LINE_SEPARATOR = SeparatorTool.LINE_SEPARATOR;

    public static void main(String[] args) throws IOException {
        //被遍历的目录。
        File dir = new File("/Users/liuluming/Downloads/");
        //明确一个过滤器。
        FileFilter filter = new FileFilterBySuffix(".java");
        //符合过滤器条件的文件有很多，最好先存储起来，然后在进行操作。
        List<File> list = new ArrayList<File>();
        //获取指定文件清单。
        getFileList(dir, filter, list);
        File destFile = new File(dir, "javalist.txt");
        write2File(list, destFile);
    }

    // 将集合中的数据的绝对路径写入到文件中。
    public static void write2File(List<File> list, File destFile) throws IOException {
        FileWriter fos = null;
        BufferedWriter bufos = null;
        try {
            fos = new FileWriter(destFile);
            bufos = new BufferedWriter(fos);
            bufos.write("该目录下包含以下文件：");
            bufos.flush();
            for (File file : list) {
                String info = file.getAbsolutePath() + LINE_SEPARATOR;
                bufos.write(info);
                bufos.flush();//每写一个绝对路径就刷新一次。
            }
        } finally {
            if (bufos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    throw new RuntimeException("关闭失败");
                }
            }
        }
    }

    //根据指定的过滤器在指定目录下获取所有的符合过滤条件的文件，并存储到list集合中。
    public static void getFileList(File dir, FileFilter filter, List<File> list) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                // 递归获取
                getFileList(file, filter, list);
            } else {
                //如果是文件，传递到过滤器中去过滤。将满足条件存储起来。
                if (filter.accept(file)) {
                    list.add(file);
                }
            }
        }
    }
}

/**
 * 自定义文件过滤器
 */
class FileFilterBySuffix implements FileFilter {
    private String suffix;

    public FileFilterBySuffix(String suffix) {
        super();
        this.suffix = suffix;
    }

    @Override
    public boolean accept(File pathname) {
        return pathname.getName().endsWith(suffix);
    }
}

/**
 *  为了避免总是调用System.getProperties("系统属性中的指定键");
 *  进行了封装。下次再使用分隔符，直接找个分隔符工具类就哦了。
 *  
 */
class SeparatorTool {
    private SeparatorTool() {
        super();
    }

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
}


