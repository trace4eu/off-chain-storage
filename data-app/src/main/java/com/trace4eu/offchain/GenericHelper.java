package com.trace4eu.offchain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.MediaType;
import org.web3j.utils.Numeric;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GenericHelper {
    public static ObjectNode hashMap2ObjectNode(HashMap<String, String> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonObject = objectMapper.createObjectNode();
        for (String key : map.keySet()) {
            String value = map.get(key);
            jsonObject.put(key, value);
        }
        return jsonObject;
    }

    public static boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    public static MediaType getMediaType(String extension) {
        final Map<String, String> FILE_EXTENSION_MEDIA_TYPES = new HashMap<>();
        FILE_EXTENSION_MEDIA_TYPES.put("txt", "text/plain");
        FILE_EXTENSION_MEDIA_TYPES.put("html", "text/html");
        FILE_EXTENSION_MEDIA_TYPES.put("css", "text/css");
        FILE_EXTENSION_MEDIA_TYPES.put("js", "application/javascript");
        FILE_EXTENSION_MEDIA_TYPES.put("json", "application/json");
        FILE_EXTENSION_MEDIA_TYPES.put("xml", "application/xml");
        FILE_EXTENSION_MEDIA_TYPES.put("jpg", "image/jpeg");
        FILE_EXTENSION_MEDIA_TYPES.put("jpeg", "image/jpeg");
        FILE_EXTENSION_MEDIA_TYPES.put("png", "image/png");
        FILE_EXTENSION_MEDIA_TYPES.put("gif", "image/gif");
        FILE_EXTENSION_MEDIA_TYPES.put("bmp", "image/bmp");
        FILE_EXTENSION_MEDIA_TYPES.put("mp3", "audio/mpeg");
        FILE_EXTENSION_MEDIA_TYPES.put("wav", "audio/wav");
        FILE_EXTENSION_MEDIA_TYPES.put("ogg", "audio/ogg");
        FILE_EXTENSION_MEDIA_TYPES.put("mp4", "video/mp4");
        FILE_EXTENSION_MEDIA_TYPES.put("avi", "video/avi");
        FILE_EXTENSION_MEDIA_TYPES.put("mov", "video/quicktime");
        FILE_EXTENSION_MEDIA_TYPES.put("doc","application/msword");
        FILE_EXTENSION_MEDIA_TYPES.put("docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        FILE_EXTENSION_MEDIA_TYPES.put("xls","application/vnd.ms-excel");
        FILE_EXTENSION_MEDIA_TYPES.put("xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        FILE_EXTENSION_MEDIA_TYPES.put("ods","application/vnd.oasis.opendocument.spreadsheet");
        FILE_EXTENSION_MEDIA_TYPES.put("odt","application/vnd.oasis.opendocument.text");
        FILE_EXTENSION_MEDIA_TYPES.put("java","text/plain");
        FILE_EXTENSION_MEDIA_TYPES.put("jar","application/java-archive");
        FILE_EXTENSION_MEDIA_TYPES.put("zip","application/zip");
        FILE_EXTENSION_MEDIA_TYPES.put("rar","application/vnd.rar");
        FILE_EXTENSION_MEDIA_TYPES.put("7z","application/x-7z-compressed");
        FILE_EXTENSION_MEDIA_TYPES.put("tar","application/x-tar");
        FILE_EXTENSION_MEDIA_TYPES.put("tar.gz","application/x-gtar");

        if (FILE_EXTENSION_MEDIA_TYPES.containsKey(extension.toLowerCase()))
            return MediaType.valueOf(FILE_EXTENSION_MEDIA_TYPES.get(extension.toLowerCase()));

        return MediaType.APPLICATION_OCTET_STREAM;
    }
    public static void saveListToFile(List<String> lines, String filePath) {
        Path path = Paths.get(filePath);

        try {
            Files.write(path, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> stringIterator2List(Iterator<String> iterator){
        List<String> list = new ArrayList<String>();
        for (Iterator<String> it = iterator; it.hasNext(); ) {
            list.add(it.next());
        }
        return list;
    }
    public static List<String> loadFileToList(String filePath) {
        List<String> lines = new ArrayList<>();
        Path path = Paths.get(filePath);

        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
    public static String toHexString(String s){
        if (s.isEmpty()) return "";
        byte[] bytes = s.getBytes();
        return Numeric.toHexString(bytes);
    }

    public static void addLineToFile(String filePath,String line) throws IOException {
        FileWriter fileWriter = new FileWriter(filePath, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(line);
        bufferedWriter.newLine();
        bufferedWriter.close();
    }

    public static boolean fileContainsLine(final String filePath, final String line) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String currentLine;
        while ((currentLine = bufferedReader.readLine()) != null) {
            if (currentLine.equals(line)) {
                bufferedReader.close();
                return  true;
            }
        }
        bufferedReader.close();
        return false;
    }
}
