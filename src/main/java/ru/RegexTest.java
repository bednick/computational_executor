package ru;

import java.util.regex.*;

public class RegexTest {

    public static void main(String args[]) {
        String text = "name1 'name 2' % {[for][name_3]} %{[for][name_4]} 'name 5' %  {[for][name_6]}";
        //print(text, "'[\\s\\S]*?'");
        //print(text, "'[^']*'");
        //
        //print(text, "([\\S]*)");
        //
        //print(text, "%[\\s]*?\\{[^\\}]*\\}");
        System.out.println(replaceGlobalFor(text));
    }

    private static void print(String text, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        System.out.println("    START");
        while(m.find()) {
            //System.out.println(String.format("start %d  finish %d", m.start(), m.end()));
            System.out.println(text.substring(m.start(), m.end()));
        }
    }

    private static String replaceGlobalFor(String line) {
        String pattern = "%[\\s]*?\\{[^\\}]*\\}";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(line);
        if (m.find()) {
            System.out.println(line.substring(m.start(), m.end()));
            return replaceGlobalFor(line.replace(line.substring(m.start(), m.end()), "---"));
        }
        return line;
    }
}