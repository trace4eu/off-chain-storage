package com.trace4eu.offchain.restservice;

import com.trace4eu.offchain.Vars;
import com.trace4eu.offchain.dto.CassandraConnection;
import com.trace4eu.offchain.repository.DbOptions;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import com.datastax.oss.driver.api.core.CqlSession;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ConnectonTest {
    private CassandraConnection cassandraConnection;
    private DbOptions mockOptions;
    private CqlSession mockSession;
    @Before
    public  void setUp() throws Exception {
        mockOptions = mock(DbOptions.class);
        mockSession = mock(CqlSession.class);
//database.url=cass-dev.trace4eu.eu:9042
//database.username=
//database.password=
//database.hostname=cass-dev.trace4eu.eu
//database.dbname=dap
//database.port=9042
//database.datacenter=GovPart1
//# database.clustername=Mars
//database.clustername=

        // Assuming Vars.DB_OPTIONS is settable for testing
        Vars.DB_OPTIONS = mockOptions;

        when(mockOptions.getHostname()).thenReturn("cass-dev.trace4eu.eu");
        when(mockOptions.getPort()).thenReturn(9042);
        when(mockOptions.getDbName()).thenReturn("dap");
        when(mockOptions.getDatacenter()).thenReturn("GovPart1");

        cassandraConnection = CassandraConnection.getInstance();


    }
//    @Test
    public void testGetSession() throws Exception {
        this.setUp();
        CqlSession session = cassandraConnection.getSession();
        assertNotNull(session);
        verify(mockSession, times(1)).close();
    }

//    @Test
    public void testDisconnect() {
        cassandraConnection.disconnect();
        verify(mockSession, times(1)).close();
    }
}
