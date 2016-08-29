
package statistics_ui;

import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import statistics.ContinuousDistribution;
import statistics.NormalDistribution;
import statistics.StudentTDistribution;
import ui_general_utils.EnterKeyListener;

/**
 *
 * @author alyacarina
 */
public class ContinuousDistributionEditor extends Panel {
    private ContinuousDistribution distribution;
    
    public ContinuousDistributionEditor(ContinuousDistribution distribution){
        this.distribution = distribution;
        initialize();
    }
    
    int findFirstDigit(String next) {
        for(int i=0; i<next.length(); i++){
            if(("-0123456789").indexOf(next.charAt(i))!=-1){
                return i;
            }
        }
        return -1;
    }
    
    private void initialize(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        ContinuousDistributionGrapher graphs = new ContinuousDistributionGrapher(distribution);
        this.add(graphs);
        
        TextArea commandBox = new TextArea("", 2, 2, TextArea.SCROLLBARS_NONE);
        commandBox.addKeyListener(new EnterKeyListener() {
            @Override
            public void doThis(KeyEvent e) {
                String next = commandBox.getText();
                next = next.trim();
                
                int indexNum = findFirstDigit(next);
                if(indexNum==-1){
                    System.out.println("Invalid command.");
                    return;
                }
                
                double x;
                try {
                    x = Double.parseDouble(next.substring(indexNum));
                } catch(Exception ex){
                    System.out.println("Invalid command.");
                    commandBox.setText("");
                    return;
                }
                
                if(next.contains("mean")){
                    distribution.setMean(x);
                } else if(next.contains("sd") && distribution instanceof NormalDistribution){
                    ((NormalDistribution)distribution).setStandardDeviation(x);
                } else {
                    System.out.println("Invalid command.");
                }
                
                graphs.refresh();
                commandBox.setText("");
            }
        });
        this.add(commandBox);
    }
    
    public static void main(String[] args){
        JFrame lookAtMe = new JFrame("CDistribution Editor");
        lookAtMe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lookAtMe.setLayout(new BorderLayout());
        lookAtMe.add("Center", new ContinuousDistributionEditor(new StudentTDistribution(3)));
        lookAtMe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        lookAtMe.setVisible(true);
    }
}
