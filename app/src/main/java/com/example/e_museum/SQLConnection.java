package com.example.e_museum;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLConnection {
    private Connection connection;
    private String url;
    private String user;
    private String password;
    private boolean reconnectingNotification;
    private boolean reconnecting;

    int userID;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public boolean isReconnecting() {
        return reconnectingNotification;
    }

    public void setReconnecting(boolean reconnectingNotification) {
        this.reconnectingNotification = reconnectingNotification;
    }

    public Connection getConnection() {
        return connection;
    }

    public SQLConnection(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        reconnecting = true;
    }

    /**
     * You need url to database server, username and password to log in database server.
     *
     * @see Connection
     * @since 1.0
     */
    public void connectServer() {
        while (connection == null) {
            try {
                Log.i("Database", "Connecting");
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                Log.i("Database", "Connection failed");
                reconnectingNotification = true;
                reconnecting = true;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Get result set of a SQL query.
     *
     * @since 1.0
     */
    public ResultSet getDataQuery(String query) {

        ResultSet resultSet = null;
        Statement statement;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }

        return resultSet;
    }


    public int updateQuery(String query) {
        int rowsAffected = 0;
        try (Statement statement = connection.createStatement()) {
            rowsAffected = statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(query);
            throw new RuntimeException(e);
        }
        return rowsAffected;
    }

    /**
     * Close connection to SQL Server.
     *
     * @since 1.0
     */
    public void addClosingWork() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));
    }
}
