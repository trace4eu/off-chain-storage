package com.trace4eu.offchain.restservice;

import com.trace4eu.offchain.Vars;
import com.trace4eu.offchain.dto.OutputFile;
import com.trace4eu.offchain.repository.CassandraIndex;
import com.trace4eu.offchain.repository.DbOptions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SearchTest {
//    CassandraIndex ndx ;
    @Before
    private void setUp(){
//        Vars.DB_OPTIONS = new DbOptions(null,null,null,"cass.trace4eu.com","dap",9042,"Mars");
//        ndx = new CassandraIndex(Vars.DB_OPTIONS);
//            ndx.connect();
    }

    @Test
    public void testSearch0() throws Exception {
        CassandraIndex ndx ;
        Vars.DB_OPTIONS = new DbOptions(null,null,null,"cass.trace4eu.eu","dap",9042,"Mars");
        ndx = new CassandraIndex(Vars.DB_OPTIONS);
        ndx.connect();
        List<OutputFile> l1 = ndx.getListOfFilesPaging("","pablo",1,0);
        Assert.assertEquals(1,l1.size());
        List<OutputFile> l2 = ndx.getListOfFilesPaging("","pablo",1,2);
        Assert.assertEquals(1,l2.size());
        Assert.assertNotEquals(l1.get(0).getId().toString(),l2.get(0).getId().toString());
        List<OutputFile> l3 = ndx.getListOfFilesPaging("","pablo",2,0);
        Assert.assertEquals(2,l3.size());
    }

}
