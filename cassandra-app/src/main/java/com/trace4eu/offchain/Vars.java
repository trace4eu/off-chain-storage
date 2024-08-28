package com.trace4eu.offchain;

import com.trace4eu.offchain.repository.DbOptions;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;

public class Vars {
    public static DbOptions DB_OPTIONS;
//    public static DbOptions DB_OPTIONS;

//    static {
//        try {
//            DB_OPTIONS = new DbOptions(getJarDir()+"/config.options");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }


//    public static File getJarDir() {
//        try {
//            String path = DbOptions.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
//            File jarFile = new File(path);
//            return jarFile.getParentFile();
//        } catch (URISyntaxException ex) {
//            throw new RuntimeException(ex);
//        }
//    }
}
