package com.csye6225.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class CheckConnectionService {

    private final DataSource dataSource;

    final Logger logger = LoggerFactory.getLogger(CheckConnectionService.class);

    @Autowired
    public CheckConnectionService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void checkConnection(){
        try(Connection conn = dataSource.getConnection()) {
            logger.info("Connection Successful!");
            System.out.println("Connection Successful!");
        } catch (SQLException se) {
            logger.error("Connection failed..");
            System.out.println("Connection failed...");
            throw new RuntimeException(se);
        }

    }

}
