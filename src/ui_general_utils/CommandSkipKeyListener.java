
package ui_general_utils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 *
 * @author alyacarina
 */
public class CommandSkipKeyListener implements KeyListener {
    ArrayList<String> commands;

    public CommandSkipKeyListener(){
        commands = new ArrayList<>();
    } 
    
    public CommandSkipKeyListener(ArrayList<String> commands){
        this.commands = commands;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    
}
