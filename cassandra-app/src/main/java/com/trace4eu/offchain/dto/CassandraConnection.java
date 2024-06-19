package com.trace4eu.offchain.dto;

import com.datastax.oss.driver.api.core.CqlSession;
import com.trace4eu.offchain.repository.DbOptions;
import hr.irb.Vars;

import java.net.InetSocketAddress;

public class CassandraConnection {
    private static CassandraConnection instance;
    private static CqlSession session;
    private static DbOptions options;
    private void connect(){
        String hostName= options.getHostname();
        if (hostName==null) hostName="localhost";
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
    private CassandraConnection() {
        this.options = Vars.DB_OPTIONS;
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
