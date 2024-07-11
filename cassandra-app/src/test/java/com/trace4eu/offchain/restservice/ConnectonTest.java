package com.trace4eu.offchain.restservice;

import com.trace4eu.offchain.Vars;
import com.trace4eu.offchain.dto.CassandraConnection;
import com.trace4eu.offchain.repository.DbOptions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotNull;

public class ConnectonTest {
    @Test
    public void testConnect() throws Exception{
        Vars.DB_OPTIONS = new DbOptions("c:\\Users\\IRB\\Documents\\projekti\\trace4eu\\off-chain-storage\\doc\\configPass.properties");
        CassandraConnection connection =  CassandraConnection.getInstance();
        assertNotNull(connection);
    }
}
