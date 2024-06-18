package hr.irb.CIR.DAP.repository;

public class IndexFactory {
    public static IIndex createIndexer(String type, DbOptions options) {
        if (options == null) {
            throw new RuntimeException("Options not available!");
        }
        switch (type) {
            case IndexerType.Cassandra:
                return new CassandraIndex(options);
            case IndexerType.IPFS:
                //TODO
                return null;
            default:
                throw new IllegalArgumentException("Invalid indexer type: " + type);
        }
    }
}
