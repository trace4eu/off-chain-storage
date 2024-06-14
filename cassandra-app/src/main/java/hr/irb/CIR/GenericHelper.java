package hr.irb.CIR;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.web3j.utils.Numeric;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
