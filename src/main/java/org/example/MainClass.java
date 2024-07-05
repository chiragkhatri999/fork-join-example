package org.example;

import org.apache.commons.io.IOUtils;
import org.example.essay.EssayTask;
import org.example.letters.LetterCountTask;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public class MainClass {

    public static void main(String[] args) throws InterruptedException, IOException {
        String filePath = ".\\input.txt";
        if(args.length > 1){
            System.out.println("Provide file name");
            filePath = args[1];
            return;
        }
        FileInputStream fis = new FileInputStream(filePath);
        String data = IOUtils.toString(fis, StandardCharsets.UTF_8);
        printMap(getCharacterCount(data));
    }

    private static Map<Character, Long> getCharacterCount(String str) throws InterruptedException {
        ForkJoinPool executor = ForkJoinPool.commonPool();
        LetterCountTask letterCountTask = new LetterCountTask(str);
        Map<Character, Long> result = executor.invoke(letterCountTask);
        Thread.sleep(1000);
        return result;
    }

    private static Long getEssayResult(){
        int[] tasks = new int[1000];
        ForkJoinPool executor = ForkJoinPool.commonPool();
        EssayTask submitTasks = new EssayTask(tasks, 0, tasks.length);
        return executor.invoke(submitTasks);
    }

    private static void printMap(Map map){
        System.out.println("unique chars count: " + map.size());
        map.entrySet().forEach(System.out::println);
    }
}
