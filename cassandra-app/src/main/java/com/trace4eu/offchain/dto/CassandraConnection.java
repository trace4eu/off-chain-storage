package com.trace4eu.offchain.dto;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.auth.AuthProvider;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.context.DriverContext;
import com.datastax.oss.driver.internal.core.auth.PlainTextAuthProvider;
import com.trace4eu.offchain.repository.DbOptions;
import com.trace4eu.offchain.Vars;

import java.net.InetSocketAddress;

public class CassandraConnection {
    private static CassandraConnection instance;
    private static CqlSession session;
    private static DbOptions options;
    private void connectWithoutPassword(){
        String hostName= getHostname();
        InetSocketAddress node = new InetSocketAddress(hostName, options.getPort());
        if (options.getClusterName().isEmpty()){
            session = CqlSession.builder()
                    .withKeyspace(options.getDbName())
                    .addContactPoint(node)
                    .build();
        }else {
            session = CqlSession.builder()
                    .withKeyspace(options.getDbName())
                    .addContactPoint(node)
                    .withLocalDatacenter(options.getClusterName())
                    .build();
        }
    }
    private String getHostname(){
        String hostName = options.getHostname();
        if (hostName==null) hostName="localhost";
        return  hostName;
    }
    private void connectWithPassword(){
        String hostName= getHostname();
        DriverConfigLoader loader = DriverConfigLoader.programmaticBuilder()
                .withString(DefaultDriverOption.AUTH_PROVIDER_CLASS, PlainTextAuthProvider.class.getName())
                .withString(DefaultDriverOption.AUTH_PROVIDER_USER_NAME, options.getUsername())
                .withString(DefaultDriverOption.AUTH_PROVIDER_PASSWORD, options.getPassword())
                .withString(DefaultDriverOption.CONTACT_POINTS, hostName + ":" + options.getPort().toString())
                .build();

        InetSocketAddress node = new InetSocketAddress(hostName, options.getPort());

        if (options.getClusterName().isEmpty()){
            session = CqlSession.builder()
                    .withKeyspace(options.getDbName())
                    .withConfigLoader(loader)
                    .addContactPoint(node)
                    .build();
        }else {
            session = CqlSession.builder()
                    .withKeyspace(options.getDbName())
                    .addContactPoint(node)
                    .withLocalDatacenter(options.getClusterName())
                    .build();
        }
    }
    private void connect(){
        if (this.options.getUsername() == null || this.options.getUsername().isBlank() || this.options.getUsername().isEmpty()){
            this.connectWithoutPassword();
            return;
        }
        connectWithPassword();
    }
    private CassandraConnection() {
//        String propFile =
//        Vars.DB_OPTIONS = new DbOptions(propFile);

        this.options = Vars.DB_OPTIONS;
        if (session==null || session.isClosed())
            this.connect();
    }

    public static CassandraConnection getInstance() {
        if (instance == null /* || instance.getSession().isClosed()*/) {
            instance = new CassandraConnection();
        }
        return instance;
    }

    public void disconnect(){
        session.close();
    }
    public CqlSession getSession() {
//        if (this.session == null)
//            this.connect();
        return session;
    }
}
