package com.trace4eu.offchain.dto;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.internal.core.auth.PlainTextAuthProvider;
import com.trace4eu.offchain.repository.DbOptions;

import java.net.InetSocketAddress;
import java.util.Arrays;

public class CassandraConnection {
    private static CassandraConnection instance;
    private static CqlSession session;
    private static DbOptions options;
    private void connectWithoutPassword(){
        String hostName= getHostname();
        InetSocketAddress node = new InetSocketAddress(hostName, options.getPort());

        DriverConfigLoader loader = DriverConfigLoader.programmaticBuilder()
//                .withStringList(DefaultDriverOption.CONTACT_POINTS, Arrays.asList( hostName + ":" + options.getPort().toString()))
                .withInt(DefaultDriverOption.CONNECTION_POOL_LOCAL_SIZE, options.getCONNECTION_POOL_LOCAL_SIZE())
                .withInt(DefaultDriverOption.CONNECTION_POOL_REMOTE_SIZE, options.getCONNECTION_POOL_REMOTE_SIZE())
                .withInt(DefaultDriverOption.CONNECTION_MAX_REQUESTS, options.getCONNECTION_MAX_REQUESTS())
                .build();

        if (options.getDatacenter().isEmpty()){
            session = CqlSession.builder()
                    .withKeyspace(options.getDbName())
                    .addContactPoint(node)
                    .withConfigLoader(loader)
                    .build();
        }else {
            session = CqlSession.builder()
                    .withKeyspace(options.getDbName())
                    .addContactPoint(node)
                    .withLocalDatacenter(options.getDatacenter())
                    .withConfigLoader(loader)
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
                .withStringList(DefaultDriverOption.CONTACT_POINTS, Arrays.asList( hostName + ":" + options.getPort().toString()))
                .withInt(DefaultDriverOption.CONNECTION_POOL_LOCAL_SIZE, options.getCONNECTION_POOL_LOCAL_SIZE())
                .withInt(DefaultDriverOption.CONNECTION_POOL_REMOTE_SIZE, options.getCONNECTION_POOL_REMOTE_SIZE())
                .withInt(DefaultDriverOption.CONNECTION_MAX_REQUESTS, options.getCONNECTION_MAX_REQUESTS())
                .build();
        InetSocketAddress node = new InetSocketAddress(hostName, options.getPort());

        if (options.getDatacenter().isEmpty()){

            session = CqlSession.builder()
                    .withKeyspace(options.getDbName())
                    .withConfigLoader(loader)
                    .addContactPoint(node)
                    .build();
        }else {
            session = CqlSession.builder()
                    .withKeyspace(options.getDbName())
                    .addContactPoint(node)
                    .withConfigLoader(loader) //dodano naknadno
                    .withLocalDatacenter(options.getDatacenter())
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
    private CassandraConnection(DbOptions options) {
//        String propFile =
//        Vars.DB_OPTIONS = new DbOptions(propFile);

//        this.options = Vars.DB_OPTIONS;
        this.options = options;
        if (session==null || session.isClosed())
            this.connect();
    }

    public static CassandraConnection getInstance(DbOptions options) {
        if (instance == null /* || instance.getSession().isClosed()*/) {
            instance = new CassandraConnection(options);
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
