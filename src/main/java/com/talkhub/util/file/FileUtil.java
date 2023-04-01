package com.talkhub.util.file;


import com.google.gson.JsonObject;
import com.talkhub.util.json.GsonUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    public static String getString(String filePath, boolean relative) {
        String path = "";
        if(relative){
            path = System.getProperty("user.dir");
        }
        File file = new File(path + filePath);
        StringBuffer contents = new StringBuffer();
        BufferedReader reader = null;
        try {
            Reader r = new InputStreamReader( new FileInputStream(file), StandardCharsets.UTF_8);
            reader = new BufferedReader(r);
            String text;
            boolean firstLine = true;
            while (true) {
                text = reader.readLine();
                if(text != null){
                    if(firstLine){
                        firstLine = false;
                    }else{
                        contents.append(System.getProperty("line.separator"));
                    }
                    contents.append(text);
                }else{
                    break;
                }
            }
            return contents.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static JsonObject getJsonObject(String filePath, boolean relative) {
        String data = getString(filePath, relative);
        return GsonUtil.toJsonObject(data);
    }

    public static void writeStringToFile(String fileName, String data) {
        Path path = Paths.get(fileName);
        try{
            BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
            writer.append(data);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
