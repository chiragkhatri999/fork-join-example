package org.example.util;

import java.util.Map;

public class PrintUtil {
    public static void printMap(Map map){
        System.out.println("unique chars count: " + map.size());
        map.entrySet().forEach(System.out::println);
    }
}
