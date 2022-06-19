package com.mxh.mysqldemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.*;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("insert")
    public String insert() {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ec",
                    "root",
                    "123456"
            );
            long start = System.currentTimeMillis();
            stmt = conn.prepareStatement("insert `order` values (?,?,?)");
            for (int i = 1; i <= 1000000; i++) {
                stmt.setString(1, String.valueOf(i));
                stmt.setString(2, "2");
                stmt.setString(3, "3");
                stmt.addBatch();
            }
            stmt.executeBatch();
            stmt.clearBatch();
            System.out.println(System.currentTimeMillis() - start);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return "ok";
    }

    @GetMapping("ss/select")
    public String ssSelect() throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from stu limit 1");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return "" + resultSet.getInt(1);
    }


    @GetMapping("dy/select")
    public String dySelect() throws SQLException {
        AbstractRoutingDataSource abstractRoutingDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return null;
            }
        };
        return "";
    }
}
