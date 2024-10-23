package com.trace4eu.offchain.repository;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbOptions {
    private String username;
    private String password;
    private String hostname;
    private String dbName;
    private Integer port;
    private String datacenter;
    private String clusterName;

    private int CONNECTION_POOL_LOCAL_SIZE = 8;
    private int CONNECTION_POOL_REMOTE_SIZE = 2;
    private int CONNECTION_MAX_REQUESTS = 1024;
    public DbOptions(String propertiesFile) throws Exception {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(propertiesFile)) {
            properties.load(input);
            this.username = properties.getProperty("database.username");
            this.password = properties.getProperty("database.password");
            this.hostname = properties.getProperty("database.hostname");
            this.dbName = properties.getProperty("database.dbname");
            this.port = Integer.parseInt(properties.getProperty("database.port"));
            this.datacenter = properties.getProperty("database.datacenter");
            this.clusterName = properties.getProperty("database.clustername");

            this.CONNECTION_MAX_REQUESTS = Integer.parseInt(properties.getProperty("database.connection.max_requests"));
            this.CONNECTION_POOL_REMOTE_SIZE = Integer.parseInt(properties.getProperty("database.connection.pool_remotesize"));
            this.CONNECTION_POOL_LOCAL_SIZE = Integer.parseInt(properties.getProperty("database.connection.pool_localsize"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public DbOptions(){}


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }




    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDatacenter() {
        return datacenter;
    }

    public void setDatacenter(String datacenter) {
        this.datacenter = datacenter;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public int getCONNECTION_POOL_LOCAL_SIZE() {
        return CONNECTION_POOL_LOCAL_SIZE;
    }

    public void setCONNECTION_POOL_LOCAL_SIZE(int CONNECTION_POOL_LOCAL_SIZE) {
        this.CONNECTION_POOL_LOCAL_SIZE = CONNECTION_POOL_LOCAL_SIZE;
    }

    public int getCONNECTION_POOL_REMOTE_SIZE() {
        return CONNECTION_POOL_REMOTE_SIZE;
    }

    public void setCONNECTION_POOL_REMOTE_SIZE(int CONNECTION_POOL_REMOTE_SIZE) {
        this.CONNECTION_POOL_REMOTE_SIZE = CONNECTION_POOL_REMOTE_SIZE;
    }

    public int getCONNECTION_MAX_REQUESTS() {
        return CONNECTION_MAX_REQUESTS;
    }

    public void setCONNECTION_MAX_REQUESTS(int CONNECTION_MAX_REQUESTS) {
        this.CONNECTION_MAX_REQUESTS = CONNECTION_MAX_REQUESTS;
    }
}
