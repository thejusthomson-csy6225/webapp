package com.csye6225.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class CheckConnectionServiceTest {

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private CheckConnectionService checkConnectionService;

    @Test
    public void checkConnection_Successful() throws SQLException {
        Connection mockConnection = Mockito.mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(mockConnection);
        checkConnectionService.checkConnection();
        verify(dataSource).getConnection();
        assertNotNull(mockConnection, "Connection is null");
    }

    @Test
    public void checkConnection_Failure() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(RuntimeException.class, () -> checkConnectionService.checkConnection());
    }
}