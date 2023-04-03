package io.ylab.intensive.lesson05.messagefilter;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MessageParser {

    public List<String> stringSplit(String message) {
        Pattern p = Pattern.compile("([^a-zA-Zёа-яЁА-Я-\\d\\s]+)");
        String[] split = message.split(" ");
        List<String> list = new ArrayList<>();
        for (String s : split) {
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

    public String joinList(List<String> words) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.size(); i++) {
            String currentWord = words.get(i);
            if (i > 0 && i < words.size() - 1 && (words.get(i + 1).matches("[.!;:,-]+") ||
                                                    words.get(i-1).matches("[-]+"))) {
                sb.append(currentWord);
            } else {
                sb.append(currentWord);
                sb.append(" ");
            }
        }
        return sb.toString();
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
