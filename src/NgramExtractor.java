import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class NgramExtractor {
    public static void main(String[] args) throws IOException {
       StringBuilder input= new StringBuilder();
       StringBuilder output= new StringBuilder();

        List<String> lines = Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8);
        for (String line : lines) {
            input.append(line);
        }

        String[] words = input.toString().replaceAll("\\p{Punct}", "").replaceAll("\\u2014","").
                replaceAll("\\u201c",""). replaceAll("\\u201d","").
                replaceAll("\\u2019", "").replaceAll("\\u2018", "").
               toLowerCase().split("\\s+");

        String[] newWords= ngram(words,Integer.parseInt(args[2]));

        output.append("Total number of tokens: ").append(words.length).append("\n");
        HashMap<String, Integer> wordMap = new HashMap<>();

        assert newWords != null;
        for (String newWord : newWords) {
            if (wordMap.containsKey(newWord)) wordMap.put(newWord, wordMap.get(newWord) + 1);
            else wordMap.put(newWord, 1);
        }

        wordMap = wordMap.entrySet().stream().sorted(Comparator.comparing(
                Map.Entry<String, Integer>::getValue).reversed()).collect(
                LinkedHashMap<String, Integer>::new,
                (map1, e) -> map1.put(e.getKey(), e.getValue()),
                LinkedHashMap::putAll);
        output.append("ngram,count,frequency\n");
        for (String key : wordMap.keySet()
             ) {
            double frequency =(double) (wordMap.get(key))/(words.length)*100;
            output.append(key).append(",").append(wordMap.get(key)).append(",").append(frequency).append("\n");

        }
        FileWriter fw = new FileWriter(args[1]);
        fw.write(output.toString());
        fw.close();

    }

    public static String[] ngram(String[] array, int a) {
        if(a<0 || a>array.length) return null ;
        int givenN=a;
        StringBuilder temp= new StringBuilder();
        int newSize = array.length-(a-1);

        String[] arr = new String[newSize];

        for (int i = 0; i <newSize; i++) {
            givenN=a;
            temp = new StringBuilder();
            int counter=i;

            while (givenN>0){
                temp.append(array[counter]).append(" ");
                counter++;
                givenN--;

        }
            arr[i] = temp.substring(0,temp.length()-1);
        }
        return arr;
    }
}