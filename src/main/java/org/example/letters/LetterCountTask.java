package org.example.letters;

import lombok.Getter;
import org.example.util.PrintUtil;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Stream;

@Getter
public class LetterCountTask extends RecursiveTask<Map<Character, Long>> {
    private final String str;

    private static final long MAX_STR_LEN_PER_TASK = 50;
    public LetterCountTask(String str){
        this.str = str;
    }

    @Override
    protected Map<Character, Long> compute() {
        if(this.getStr().length() > MAX_STR_LEN_PER_TASK){
            String partOne = this.getStr().substring(0, this.getStr().length()/2);
            String partTwo = this.getStr().substring(this.getStr().length()/2);
            LetterCountTask partOneLetterCountTask = new LetterCountTask(partOne);
            LetterCountTask partTwoLetterCountTask = new LetterCountTask(partTwo);
            partOneLetterCountTask.fork();
            partTwoLetterCountTask.fork();
            Map<Character, Long> charCountFromOne =  partOneLetterCountTask.join();
            Map<Character, Long> charCountFromTwo =  partTwoLetterCountTask.join();
            return getMergedMap(charCountFromOne, charCountFromTwo);
        } else {
            Map<Character, Long> characterLongMap = new HashMap<>();
            for(int i = 0, n = this.getStr().length() ; i < n ; i++) {
                char c = this.getStr().charAt(i);
                characterLongMap.merge(c, 1L, Long::sum);
            }
            System.out.println("Sub-section: " + this.getStr()+ " Completed by: "
                    + Thread.currentThread().getName());

            PrintUtil.printMap(characterLongMap);
            return characterLongMap;
        }
    }

    public Map<Character, Long> getMergedMap(Map<Character, Long> map1, Map<Character, Long> map2){
        map2.forEach((key, value) -> {
            map1.merge(key, value, Long::sum);
        });
        return map1;
    }
}
