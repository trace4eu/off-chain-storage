package hr.irb.CIR.DAP.repository;

public class IndexFactory {
    public static IIndex createIndexer(String type, DbOptions options) {
        if (options == null) {
            throw new RuntimeException("Options not available!");
        }
        switch (type) {
            case IndexerType.Pg:
                return new PgIndex(options);
            case IndexerType.Cassandra:
                return new CassandraIndex(options);
            default:
                throw new IllegalArgumentException("Invalid indexer type: " + type);
        }
    }
}
