
package graph_ui;

import graphs.ConceptGraph;
import graphs.ConceptNode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import ui_general_utils.EnterKeyListener;

/**
 *
 * @author alyacarina
 */
public class MergeConsole extends Panel {
    private TextArea input;
    private ConceptGraph zeus;
    private Grapher olympus;
    
    public MergeConsole(ConceptGraph zeus, Grapher olympus){
        super();
        this.zeus = zeus;
        this.olympus = olympus;
        initialize();
    }
    
    private void initialize(){
        setLayout(new BorderLayout());

        input = new TextArea("", 1, 15, TextArea.SCROLLBARS_VERTICAL_ONLY);
        input.setEditable(true);
        input.addKeyListener(new EnterKeyListener() {
            @Override
            public void doThis(KeyEvent e) {
                String command = input.getText();
                while(command.contains("\n")){
                    command = command.substring(0, command.indexOf("\n"))
                            + command.substring(command.indexOf("\n")+1);
                }
                
                String words[] = command.split(",");
                int[] ids = new int[words.length];
                for(int j=0; j<words.length; j++){
                    words[j] = words[j].trim();
                    try{
                        ids[j] = Integer.parseInt(words[j]);
                    } catch(NumberFormatException nfe){
                        olympus.refresh();
                        input.setText("");
                        return;
                    }
                }
                
                ConceptNode x = (ConceptNode) zeus.root.seek(ids[0], new ArrayList<>());
                for(int i=1; i<words.length; i++){
                    zeus.mergeConceptNodes(ids[0], ids[i], true);
                }
                
                olympus.refresh();
                
                input.setText("");
            }
        });
        
        input.setBackground(Color.black);
        input.setForeground(Color.white);
        
        add("North", input);
    }
}

