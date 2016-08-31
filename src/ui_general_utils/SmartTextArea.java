
package ui_general_utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author alyacarina
 */
public class SmartTextArea extends TextArea {
    
    public SmartTextArea(int width, int height){
        super("", width, height, TextArea.SCROLLBARS_VERTICAL_ONLY);
        initialize();
    }
    
    public void doOnEnterKey(KeyEvent ke){
        
    }
    
    private void initialize(){
        this.setBackground(Color.BLACK);
        this.setForeground(Color.WHITE);
        this.addKeyListener(new EnterKeyListener() {
            @Override
            public void doThis(KeyEvent ke) {
                doOnEnterKey(ke);
            }
        });
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)){ // on right-click
                    rightClickPopUp();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }
    
    private void rightClickPopUp(){
        System.out.println("right-click");
    }
    
    // for testing purposes
    public static void main(String[] args){
        JFrame lookAtMe = new JFrame("TESTING");
        lookAtMe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lookAtMe.setLayout(new BorderLayout());
        lookAtMe.add("Center", new SmartTextArea(2, 2));
        lookAtMe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        lookAtMe.setVisible(true);
    }
    
}
