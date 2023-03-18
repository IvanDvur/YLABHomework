package Homework3.Transliterator;

import java.util.Locale;

public class TransliteratorTest {
    public static void main(String[] args) {
        String s = "Эй, цирюльникъ, Hello World, ёжик выстриги, да щетину ряхи сбрей, феном вошь за печь гони! Hello,World";
        String upperCased = s.toUpperCase(Locale.ROOT);
        Transliterator transliterator = new TransliteratorImpl();
        System.out.println(transliterator.transliterate(upperCased));
    }
}
