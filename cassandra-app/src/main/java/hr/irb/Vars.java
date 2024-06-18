package hr.irb;

import com.trace4eu.offchain.repository.DbOptions;

public class Vars {
    public static DbOptions CASSANDRA_DB_OPTIONS= new DbOptions(null,null,null,"cass.trace4eu.com","dap",9042, "Mars");
}
