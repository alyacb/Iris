package statistics_ui;

import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import statistics.DataSet;
import ui_general_utils.EnterKeyListener;

/**
 *
 * @author alyacarina
 */
public class DataSetEditor extends Panel {

    private DataSet ds;
    private double step_size;

    public DataSetEditor(DataSet ds) {
        this.ds = ds;
        step_size = 1;
        initialize();
    }
    
    // Allows containers/ subclasses to add extra refresh functionality
    public void onRefresh(){}

    private void initialize() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        DataSetGrapher graphs = new DataSetGrapher(ds) {
            @Override
            public void refresh(double step_size){
                super.refresh(step_size);
                onRefresh();
            }
        };
        this.add(graphs);

        TextArea commandBox = new TextArea("", 2, 2, TextArea.SCROLLBARS_NONE);
        commandBox.addKeyListener(new EnterKeyListener() {
            @Override
            public void doThis(KeyEvent e) {
                String next = commandBox.getText();
                commandBox.setText("");
                next = next.trim();
                if(next.substring(1).isEmpty()){
                    return;
                }

                double d = Double.parseDouble(next.substring(1));
                if (next.contains("a")) {
                    ds.addDatum(d);
                } else if (next.contains("r")) {
                    ds.removeDatum(d);
                } else if (next.contains("s")){
                    step_size = d;
                } else {
                    return;
                }
                
                graphs.refresh(step_size);
            }
        });
        this.add(commandBox);
    }

    public static void main(String[] args) {
        JFrame lookAtMe = new JFrame("DataSet Editor");
        lookAtMe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lookAtMe.setLayout(new BorderLayout());
        lookAtMe.add("Center", new DataSetEditor(new DataSet()));
        lookAtMe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        lookAtMe.setVisible(true);
    }

}
