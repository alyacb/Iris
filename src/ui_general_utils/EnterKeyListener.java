
package ui_general_utils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author alyacarina
 */
public abstract class EnterKeyListener implements KeyListener {
    public void EnterKeyListener(){
        
    }
    
    public abstract void doThis(KeyEvent ke);

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_ENTER){
            doThis(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_ENTER){
            doThis(e);
        }
    }
}
