package io.nio;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/21 17:52
 */
public class NioFileOperator {

    public static final String read(String pathString){
        String result = "";
        Path path = Paths.get(pathString);
        try(
            BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))){


            String currentLine = null;
            while((currentLine = reader.readLine()) != null){
                result += currentLine;
            }
        }catch(IOException ex){
            ex.printStackTrace(); //handle an exception here
        }
        return result;
    }

    public static final void write(String pathString, String message){
        byte [] data = message.getBytes();
        Path path = Paths.get(pathString);

        try (OutputStream out = new BufferedOutputStream(
            Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException error) {
            System.err.println(error);
        }
    }
}
