
package ide;

import core.Iris;
import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import ui_general_utils.EnterKeyListener;

/**
 *
 * @author alyacarina
 */
public class Consolation extends Panel {
    
    private final GraphLeveller view;
    private Iris brains;
    private TextArea console;
    
    public Consolation(){
        super();
        brains = new Iris();
        view = new GraphLeveller(brains.getMemory().getGraph());
        initialize();
    }
    
    private void initialize(){
        setLayout(new BorderLayout());
        add("Center", view);
        console = new TextArea("", 1, 25, TextArea.SCROLLBARS_NONE);
        console.addKeyListener(new EnterKeyListener() {
            @Override
            public void doThis(KeyEvent e) {
                brains.placeInput(console.getText());
                view.refresh();
                console.setText("");
            }
        });
        add("South", console);
    }

    void setIris(Iris brains) {
        this.brains = brains;
        view.setSource(brains.getMemory().getGraph());
        view.refresh();
    }

}
