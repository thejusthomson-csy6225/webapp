package com.csye6225.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;

@Service
public class CheckConnectionService {

    private final DataSource dataSource;

    @Autowired
    public CheckConnectionService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void checkConnection(){
        try {
            dataSource.getConnection();
            System.out.println("Connection Successful!");
        } catch (SQLException se) {
            System.out.println("Connection failed...");
            throw new RuntimeException(se);
        }

    }

}
