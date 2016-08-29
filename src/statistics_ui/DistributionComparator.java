
package statistics_ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import statistics.ContinuousDistribution;
import statistics.DataSet;
import statistics.NormalDistribution;
import statistics.ChiSquaredDistribution;
import statistics.ExponentialDistribution;

/**
 *
 * @author alyacarina
 */
public class DistributionComparator extends Panel {
    private DataSet source;
    private double step_size;
    private ContinuousDistribution cd;
    private ContinuousDistributionGrapher cdg;
    private Panel cdPanel, dsPanel, distChooser;
    private JComboBox distributioner;
    private ContinuousDistribution[] distributions;
    
    public DistributionComparator(){
        source = new DataSet();
        step_size = 1;
        
        distributions = new ContinuousDistribution[]{
            new NormalDistribution(source.getMean(), source.getStandardDeviation()),
            new ChiSquaredDistribution((int)source.getMean()),
            new ExponentialDistribution(source.getMean())
        };
        
        cd = distributions[0];
        
        initialize();
    }
    
    private void initialize(){
        this.setLayout(new GridLayout(1, 2));
        
        cdPanel = new Panel();
        cdPanel.setLayout(new BorderLayout());
        cdg = new ContinuousDistributionGrapher(cd);
        cdPanel.add("Center", cdg);
        
        distChooser = new Panel();
        String[] names = new String[distributions.length];
        
        for(int i=0; i<names.length; i++){
            names[i] = distributions[i].getName();
        }
        
        distributioner = new JComboBox(names);
        distChooser.add(distributioner);
        Button bee = new Button("Set Distribution");
        bee.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                cdg.setDistribution(distributions[distributioner.getSelectedIndex()]);
            }
        });
        distChooser.add(bee);
        cdPanel.add("North", distChooser);
        this.add(cdPanel);
        
        dsPanel = new Panel();
        dsPanel.setLayout(new GridLayout(1, 1));
        DataSetEditor dse = new DataSetEditor(source) {
            @Override
            public void onRefresh(){
                super.onRefresh();
                cd.setMean(source.getMean());
                if(cd instanceof NormalDistribution) 
                    ((NormalDistribution)cd).setStandardDeviation(source.getStandardDeviation());
                cdg.refresh();
            }
        };
        dsPanel.add(dse);
        this.add(dsPanel);
    }
    
    public static void main(String[] args){
        JFrame lookAtMe = new JFrame("Distribution Comparator");
        lookAtMe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lookAtMe.setLayout(new BorderLayout());
        lookAtMe.add("Center", new DistributionComparator());
        lookAtMe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        lookAtMe.setVisible(true);
    }
}
