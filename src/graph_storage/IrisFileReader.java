package graph_storage;

import graphs.MemoryManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

/**
 *
 * @author alyacarina
 */
public class IrisFileReader {

    private String file_name;
    private MemoryManager memory;

    public IrisFileReader(String file_name, MemoryManager memory) {
        this.file_name = file_name;
        this.memory = memory;
    }

    public void readIn() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(file_name));
        String nextLine;
        while ((nextLine = in.readLine()) != null) {
            String words[] = nextLine.split(" ");
            for (int j = 0; j < words.length; j++) {
                words[j] = words[j].trim();
                words[j] = words[j].toLowerCase(Locale.ENGLISH);
                int i = 0;
                while (i < words[j].length()) {
                    if (!Character.isLetter(words[j].charAt(i))) {
                        if (i == 0) {
                            words[j] = words[j].substring(i + 1);
                        } else {
                            words[j] = words[j].substring(0, i)
                                    + words[j].substring(i + 1);
                        }
                    } else {
                        i++;
                    }
                }
            }
            memory.learnWords(words);
        }
        in.close();
    }
}
