package hr.irb.CIR.Auth;

import hr.irb.CIR.GenericHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class LookupAuth {
    private String fileName="clients.txt";
    public String getFileName(){
        return fileName;
    }
    public LookupAuth(String fileName){
        if (fileName != null)
            this.fileName = fileName;
    }
    public boolean exists(String uid) throws IOException {
        if (!new File(fileName).exists()) return false;
        return GenericHelper.fileContainsLine(fileName,uid);
    }
    public String addUser() throws IOException {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        GenericHelper.addLineToFile(fileName,uuidString);
        return uuidString;
    }
    public void deleteKey(String uid){
        List<String> lines = GenericHelper.loadFileToList(fileName);
        lines.removeIf(uid::equals);
        GenericHelper.saveListToFile(lines, fileName);
    }
}
