package io.bio;

import java.util.Random;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/19 18:09
 */
public class BioDemo {


    public static void main(String[] args) {
        String path = BioDemo.class.getResource("/test.txt").getPath();
        String result = BioFileOperator.read(path);
        System.out.println(result);

        String testInfo = String.valueOf(new Random().nextDouble());
        System.out.println("inputString:" + testInfo);
        BioFileOperator.write(path, testInfo);

        String resultAfterWriter = BioFileOperator.read(path);
        System.out.println("result: " + resultAfterWriter);
        assert(testInfo.equals(resultAfterWriter));

    }
}
