package com.trace4eu.offchain.dto;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.session.Session;
import com.trace4eu.offchain.repository.DbOptions;
import hr.irb.Vars;

import java.net.InetSocketAddress;

public class CassandraConnection {
    private static CassandraConnection instance;
    private CqlSession session;
    private DbOptions options;
    private CassandraConnection() {
        this.options = Vars.CASSANDRA_DB_OPTIONS;
        String hostName= options.getHostname();
        if (hostName==null) hostName="localhost";
        InetSocketAddress node = new InetSocketAddress(hostName, options.getPort());
        session = CqlSession.builder()
                .withKeyspace(options.getDbName())
                .addContactPoint(node)
                .withLocalDatacenter(options.getClusterName())
                .build();

//        Cluster cluster = Cluster.builder()
//                .addContactPoint("localhost")
//                .build();
//        session = cluster.connect();
    }

    public static CassandraConnection getInstance() {
        if (instance == null) {
            instance = new CassandraConnection();
        }
        return instance;
    }

    public CqlSession getSession() {
        return session;
    }
}
