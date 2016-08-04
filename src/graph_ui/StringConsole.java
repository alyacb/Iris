
package graph_ui;

import graphs.ConceptGraph;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.util.Locale;
import ui_general_utils.EnterKeyListener;

/**
 *
 * @author alyacarina
 */
public class StringConsole extends Panel {
    private TextArea input, log;
    private ConceptGraph zeus;
    private Grapher olympus;
    
    public StringConsole(ConceptGraph zeus, Grapher olympus){
        super();
        this.zeus = zeus;
        this.olympus = olympus;
        initialize();
    }
    
    private void initialize(){
        setLayout(new BorderLayout());

        input = new TextArea("", 1, 156, TextArea.SCROLLBARS_VERTICAL_ONLY);
        input.setEditable(true);
        input.addKeyListener(new EnterKeyListener() {
            @Override
            public void doThis(KeyEvent e) {
                String command = input.getText();
                while(command.contains("\n")){
                    command = command.substring(0, command.indexOf("\n"))
                            + command.substring(command.indexOf("\n")+1);
                }
                
                String words[] = command.split(" ");
                for(int j=0; j<words.length; j++){
                words[j] = words[j].trim();
                words[j] = words[j].toLowerCase(Locale.ENGLISH);
                int i=0;
                
                while(i<words[j].length()){
                        if(!Character.isLetter(words[j].charAt(i))){
                            if(i==0){ 
                                    words[j] = words[j].substring(i+1);
                                } else {
                                    words[j] = words[j].substring(0, i) 
                                            + words[j].substring(i+1);
                                }    
                            } else {
                                i++;
                            }
                        }
                    }              
                
                    zeus.learnWords(words);
                    log.append(command+"\n");
                olympus.refresh();
                input.setText("");
            }
        });
        add("North", input);

        log = new TextArea("", 3, 156, TextArea.SCROLLBARS_VERTICAL_ONLY);
        log.setBackground(Color.black);
        log.setForeground(Color.white);
        log.setEditable(false);
        add("Center", log);
        
    }
}
