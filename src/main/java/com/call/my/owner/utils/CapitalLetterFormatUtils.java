package com.call.my.owner.utils;

public class CapitalLetterFormatUtils {

    public static String formatText(String string){
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

}
