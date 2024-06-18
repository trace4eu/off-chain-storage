package com.trace4eu.offchain.dto;

import com.datastax.oss.driver.api.core.session.Session;

public class CassandraConnection {
    private static CassandraConnection instance;
    private Session session;

    private CassandraConnection() {
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

    public Session getSession() {
        return session;
    }
}
