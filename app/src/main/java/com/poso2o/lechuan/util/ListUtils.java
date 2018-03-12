package com.poso2o.lechuan.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaydon on 2017/4/8.
 */
public class ListUtils {

    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }

    public static boolean isEmpty(List list) {
        return list == null || list.size() <= 0;
    }

    public static void move(ArrayList list, int fromPosition, int toPosition) {
        Object tmp = list.remove(fromPosition);
        list.add(toPosition, tmp);
    }
}