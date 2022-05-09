package io.nio;

import io.bio.BioDemo;
import io.bio.BioFileOperator;

import java.util.Random;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/21 18:49
 */
public class Demo {
    public static void main(String[] args) {
        String path = Demo.class.getResource("/test.txt").getPath();
        String result = NioFileOperator.read(path);
        System.out.println(result);

        String testInfo = String.valueOf(new Random().nextDouble());
        System.out.println("inputString:" + testInfo);
        NioFileOperator.write(path, testInfo);

        String resultAfterWriter = NioFileOperator.read(path);
        System.out.println("result: " + resultAfterWriter);
        assert(testInfo.equals(resultAfterWriter));
    }
}
