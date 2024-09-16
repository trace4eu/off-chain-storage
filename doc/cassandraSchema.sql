CREATE KEYSPACE ocs WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
use ocs;
create table fileStore(
    id              uuid PRIMARY KEY,
    documentId      TEXT,
    data            blob,
    owner           TEXT,
    extension       TEXT
);

CREATE INDEX fs_docid_idx ON fileStore (documentId);
CREATE INDEX fs_publisherId_idx ON fileStore (owner);

create materialized view 
if not exists mv_fileStore_docid as
    select      id,documentId,owner,extension
    from        fileStore
    primary key (documentId,owner);


create materialized view 
if not exists mv_fileStore_owner as
    select      id,documentId,owner,extension
    from        fileStore
    primary key (owner,documentId);

