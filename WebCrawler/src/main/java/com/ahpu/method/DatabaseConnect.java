package com.ahpu.method;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnect {
    public Connection getconn() {
        String driver = null;
        String url = null;
        String user = null;
        String password = null;
        Connection conn = null;

        /*
        Properties pro = new Properties(); //新建一个properties实例，用于从DBConfig中拿到实例参数
        try {         //  隔了一段时间出现空指针异常
            InputStream is = this.getClass().getResourceAsStream("DBconfig.properties");
            //pro.load(this.getClass().getClassLoader().getResourceAsStream("DBconfig.properties"));
            pro.load(is);
            driver = pro.getProperty("driver");
            url = pro.getProperty("url");
            user = pro.getProperty("user");
            password = pro.getProperty("password");
            is.close();
            // System.out.println(driver+url+user+password);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        try {
            driver = "com.mysql.jdbc.Driver";
            url = "jdbc:mysql://localhost:3306/ahpu";
            user = "root";
            password = "root";
            Class.forName(driver);    //加载jdbc驱动
            conn = DriverManager.getConnection(url, user, password); //获取数据库连接
            conn.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
