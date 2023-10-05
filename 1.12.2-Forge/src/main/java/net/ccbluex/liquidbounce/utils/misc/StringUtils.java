package net.ccbluex.liquidbounce.utils.misc;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SideOnly(Side.CLIENT)
public final class StringUtils {
    public static String breakString(String s) {
        StringBuilder sb = new StringBuilder();
        String[] sArray = s.split("");
        int index = 0;
        for (String s1 : sArray) {
            if (s1.equals("")) continue;

            if (s1.equals(s1.toUpperCase()) && Character.isLetter(s1.toCharArray()[0])) {
                if (index != 0) {
                    sb.append(" ");
                }
            }

            sb.append(s1);
            index++;
        }

        return sb.toString();
    }

    public static String toCompleteString(final String[] args, final int start) {
        if (args.length <= start) return "";

        return String.join(" ", Arrays.copyOfRange(args, start, args.length));
    }
    private static HashMap<String,String> stringCache = new HashMap<>();
    public static String fixString(String str){
        if(stringCache.containsKey(str)) return stringCache.get(str);

        str=str.replaceAll("\uF8FF","");//remove air chars

        StringBuilder sb=new StringBuilder();
        for(char c:str.toCharArray()){
            if((int) c >(33+65248)&& (int) c <(128+65248)){
                sb.append(Character.toChars((int) c - 65248));
            }else{
                sb.append(c);
            }
        }
        String result=sb.toString();
        stringCache.put(str,result);

        return result;
    }
    public static String replace(final String string, final String searchChars, String replaceChars) {
        if (string.isEmpty() || searchChars.isEmpty() || searchChars.equals(replaceChars))
            return string;

        if (replaceChars == null)
            replaceChars = "";

        final int stringLength = string.length();
        final int searchCharsLength = searchChars.length();
        final StringBuilder stringBuilder = new StringBuilder(string);

        for (int i = 0; i < stringLength; i++) {
            final int start = stringBuilder.indexOf(searchChars, i);

            if (start == -1) {
                if (i == 0)
                    return string;

                return stringBuilder.toString();
            }

            stringBuilder.replace(start, start + searchCharsLength, replaceChars);
        }

        return stringBuilder.toString();
    }
}