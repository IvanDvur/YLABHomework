package Homework3.Transliterator;

import java.util.HashMap;

public class TransliteratorImpl implements Transliterator{

    HashMap<Character,String> lettersMap = new HashMap<>();

    public TransliteratorImpl() {
        fillHashMap(this.lettersMap);
    }

    @Override
    public String transliterate(String source) {
        StringBuilder sb = new StringBuilder();
        for (Character c:source.toCharArray()) {
            sb.append(lettersMap.getOrDefault(c, String.valueOf(c)));
        }
        return sb.toString();
    }

    private void fillHashMap(HashMap<Character,String> map){
        char[] keys = new char[33];
        String[] values = {"A","B","V","G","D","E","E","ZH","Z",
                "I","I","K","L","M","N","O","P","R","S","T","U","F","KH","TS","CH","SH",
                "SHCH","IE","Y","","E","IU","IA"};

        for(int i = 1040,j=0;i<=1071;i++,j++){
            if(j == 6){
                char io = 1025;
                keys[j]=io;
                i--;
                continue;
            }
            keys[j] = (char)i;
        }
        for (int i = 0; i < values.length; i++) {
            map.put(keys[i],values[i]);
        }

    }
}
