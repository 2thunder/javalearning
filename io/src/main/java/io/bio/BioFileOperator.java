package io.bio;

import java.io.*;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/19 17:55
 */
public class BioFileOperator {
    /**
     * @param path 路径
     * @return
     */
    public static String read(String path){
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            do {
                String buffer = reader.readLine();
                result.append(buffer);
            } while (reader.read() != -1);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void write(String path, String message){
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(message);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
