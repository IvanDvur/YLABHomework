package io.ylab.intensive.lesson05.messagefilter;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MessageParser {

    public List<String> stringSplit(String message) {
        Pattern p = Pattern.compile("([^a-zA-Zёа-яЁА-Я\\d]+)");
        Pattern p1 = Pattern.compile("(\\s|\\S+)");
        Matcher m = p1.matcher(message);
        List<String> res = new ArrayList<>();
        while (m.find()) {
            res.add(m.group());
        }
        List<String> list = new ArrayList<>();
        for (String s : res) {
            Matcher matcher = p.matcher(s);
            boolean found = false;
            int i = 0;
            while (matcher.find()) { //отделение пунктуации в отдельные элементы
                found = true;
                list.add(s.substring(i, matcher.start()));
                list.add(s.substring(matcher.start(), matcher.end()));
                i = matcher.end();
            }
            if (found) {
                if (i < s.length())
                    list.add(s.substring(i));
            } else
                list.add(s);
        }
        return list;
    }

    public String censorWord(String word) {
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != 0 && i != chars.length - 1) {
                chars[i] = '*';
            }
        }
        return String.valueOf(chars);
    }
}
