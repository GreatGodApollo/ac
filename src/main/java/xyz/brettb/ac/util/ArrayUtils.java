package xyz.brettb.ac.util;

import java.util.List;
import java.util.stream.Collectors;

public class ArrayUtils {

    public static List<String> colorize(List<String> list) {
        if (list == null || list.size() < 1) {
            return list;
        }
        return list.stream().map(str -> "&r" + str).map(TextUtils::colorizeText).collect(Collectors.toList());
    }

}
