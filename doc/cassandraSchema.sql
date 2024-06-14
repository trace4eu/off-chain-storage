CREATE KEYSPACE dap WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
use dap;
create table fileStore(
    id              uuid PRIMARY KEY,
    documentId      TEXT,
    data            blob,
    owner           TEXT,
    extension       TEXT
);

CREATE INDEX fs_docid_idx ON fileStore (documentId);
CREATE INDEX fs_publisherId_idx ON fileStore (owner);
