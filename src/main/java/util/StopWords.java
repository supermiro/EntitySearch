package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class StopWords {
    
    String file = null;
    Set<String> sw = null;

    public StopWords(File file) {
        init(file);
    }

    private void init(File file) {
        if (!file.exists() || !file.canRead()) {
            return;
        }

        sw = new HashSet<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            
            
            while ((line = br.readLine()) != null) {
                String word = line.trim().toLowerCase();
                sw.add(word);
            }

            br.close();
        } catch (IOException e) {
            System.out.println("Error:" + e);
        }         
        
    }

    public Set<String> getStopWords() {
        return sw;
    }

}
