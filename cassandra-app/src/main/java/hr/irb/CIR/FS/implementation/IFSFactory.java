package hr.irb.CIR.FS.implementation;

import hr.irb.CIR.FS.IFileSystem;


class IFSFactory {
    public static IFileSystem createInternetFileSystem(EIFSType type, String hostUrl) throws Exception {
        if (hostUrl == null) throw new Exception("Options not available!");
        switch (type) {
            case IFS :
//                return new IFS(hostUrl);
            case TorrentFS:
//                return new TorrentFS(hostUrl);
            default:
                throw new Exception("Invalid Internet file system type: $type");
        }
    }
}