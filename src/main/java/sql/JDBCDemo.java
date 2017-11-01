package sql;

import java.sql.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: liuluming
 * @CreatedDate: 2017/10/24 上午10:33
 */
public class JDBCDemo {

    AtomicLong nameNumber = new AtomicLong(963427);

    public static void main(String[] args) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                JDBCDemo jdbcDemo = new JDBCDemo();

                jdbcDemo.insertBatch();
            }
        };

        for (int i = 0; i < 50; i++) {
            Thread thread = new Thread(runnable);
            thread.setName("thread-" + i);
            thread.start();
            System.out.println("新线程" + thread.getName() + "已启动");
        }


    }

    private Connection getConn() {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://liuluming.cn:3307/test";
        String username = "test";
        String password = "123qwe";
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
            System.out.println(Thread.currentThread().getName() + "数据库连接成功！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private void insertBatch() {
        java.util.Date date = new java.util.Date();
        Connection conn = getConn();
        System.out.println(date.toLocaleString() + ":" + Thread.currentThread().getName() + ":【开始】批量插入……");

        String preSql = "insert into user (name,age) values";
        try {
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("");


            while (nameNumber.get() < 10000000l) {
                long currentNumber = 0l;
                StringBuilder values = new StringBuilder();

                for (int i = 0; i < 1000; i++) {
                    currentNumber = nameNumber.getAndIncrement();
                    values.append("('" + "user" + currentNumber + "',10 ),");
                }
                values.setCharAt(values.length() - 1, ';');
                String sql = preSql + values.toString();
                System.out.println(new java.util.Date().toLocaleString() + ":" + Thread.currentThread().getName() + ":当前nameNumber=" + currentNumber + "，开始提交");
                pstmt.addBatch(sql.toString());
                pstmt.executeBatch();
                conn.commit();

            }
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            System.out.println(new java.util.Date().toLocaleString() + ":" + Thread.currentThread().getName() + ":【结束】批量插入……");
        }
    }
}

class User {
    private Long id;
    private String name;
    private int age;
    private Date gmtCreated;
    private Date gmtModified;
    private boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
