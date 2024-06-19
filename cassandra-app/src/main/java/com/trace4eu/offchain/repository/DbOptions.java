package com.trace4eu.offchain.repository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbOptions {
    private String url;
    private String username;
    private String password;
    private String hostname;
    private String dbName;
    private Integer port;
    private String datacenter;
    private String clusterName;
    public DbOptions(String propertiesFile) throws Exception {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(propertiesFile)) {
            properties.load(input);
            this.url = properties.getProperty("database.url");
            this.username = properties.getProperty("database.username");
            this.password = properties.getProperty("database.password");
            this.hostname = properties.getProperty("database.hostname");
            this.dbName = properties.getProperty("database.dbname");
            this.port = Integer.parseInt(properties.getProperty("database.port"));
            this.datacenter = properties.getProperty("database.datacenter");
            this.clusterName = properties.getProperty("database.clustername");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public DbOptions(String url, String username, String password, String hostname, String dbName, Integer port, String clusterName) {
        this.url = url;
        this.username = username;
        this.password=password;
        this.hostname = hostname;
        this.dbName = dbName;
        this.port = port;
        this.clusterName = clusterName;
    }
    public DbOptions(){}
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) throws Exception {
        this.url = url;
        if (getUrl()!=null && !this.getUrl().startsWith("http")){
            this.url = "http://"+this.url;
        }
        //this.extractDataFromUrl();
    }

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

//    public void setUrlFromData(boolean isHttp) throws Exception {
//        StringBuilder url = new StringBuilder();
//        url.append(isHttp ? "http://" : "https://");
//        if (this.getUsername() != null) {
//            url.append(getUsername());
//            if (this.getPassword()!=null) url.append(":"+this.password);
//            url.append("@");
//        }
//        url.append(getHostname());
//
//        if (getPort() != null)
//            url.append(":").append(getPort().toString());
//
//        url.append("/"). append(getDbName());
//
//        this.setUrl(url.toString());
//    }
    @Deprecated
    private void extractDataFromUrl() throws Exception {
        if (this.getUrl() == null) throw new Exception("No url available");
        Pattern pattern;
        String login, password, hostname,database;
        if (this.getUrl().startsWith("https")){
            pattern= Pattern.compile("https?://(.*?):(.*?)@(.*?)/(.*?)");
        } else {
            pattern= Pattern.compile("http?://(.*?):(.*?)@(.*?)/(.*?)");
        }
        Matcher matcher = pattern.matcher(getUrl());
        if (matcher.find()) {
            login = matcher.group(1);
            password = matcher.group(2);
            hostname = matcher.group(3);
            database = matcher.group(4);

            this.setUsername(login);
            this.setPassword(password);
            this.setHostname(hostname);
            this.setDbName(database);
        } else {
            pattern = Pattern.compile("^http?://[a-zA-Z0-9]+@[a-zA-Z0-9]+/[a-zA-Z0-9]+$");
            Matcher m = pattern.matcher(this.getUrl());
            if (m.find()){
//TODO nije dovrseno
            }else{

            }

//            throw new Exception("URL format is incorrect.");
        }
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
}
