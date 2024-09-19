CREATE KEYSPACE ocs WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '3'}  AND durable_writes = true;

use ocs;

CREATE TABLE ocs.filestore ( 
    id uuid PRIMARY KEY,
    data blob,
    documentid text,
    extension text,
    isprivate boolean,
    owner text
) WITH additional_write_policy = '99p'
AND allow_auto_snapshot = true
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
AND cdc = false
AND comment = ''
AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
AND compression = {'chunk_length_in_kb': '16', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND memtable = 'default'
AND crc_check_chance = 1.0
AND default_time_to_live = 0
AND extensions = {}
AND gc_grace_seconds = 864000
AND incremental_backups = true
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair = 'BLOCKING'
AND speculative_retry = '99p';


CREATE INDEX fs_docid_idx ON fileStore (documentId);
CREATE INDEX fs_publisherId_idx ON fileStore (owner);

drop materialized view mv_fileStore_docid;
drop materialized view mv_fileStore_owner;

create materialized view 
if not exists mv_fileStore_docid as
    select      id,documentId,owner,extension,isprivate
    from        fileStore
    WHERE       documentId IS NOT NULL 
    AND         owner IS NOT NULL 
    AND         id IS NOT NULL
    primary key (documentId,id);

create materialized view 
if not exists mv_fileStore_owner as
    select      id,documentId,owner,extension,isprivate
    from        fileStore
    WHERE       owner IS NOT NULL 
    AND         documentId IS NOT NULL 
    AND         id IS NOT NULL
    primary key (owner,id);


 
