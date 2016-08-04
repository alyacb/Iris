
package statistics_ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import javax.swing.JFrame;
import statistics.ContinuousDistribution;
import statistics.DataSet;
import statistics.NormalDistribution;

/**
 *
 * @author alyacarina
 */
public class DistributionComparator extends Panel {
    private DataSet source;
    private double step_size;
    private ContinuousDistribution cd;
    private ContinuousDistributionGrapher cdg;
    
    public DistributionComparator(){
        source = new DataSet();
        step_size = 1;
        cd = new NormalDistribution(source.getMean(), source.getVariance());
        
        initialize();
    }
    
    private void initialize(){
        this.setLayout(new GridLayout(1, 2));
        
        this.add(new DataSetEditor(source) {
            @Override
            public void onRefresh(){
                super.onRefresh();
                cd.setMean(source.getMean());
                cd.setStandardDeviation(source.getStandardDeviation());
                cdg.refresh();
            }
        });
        
        cdg = new ContinuousDistributionGrapher(cd);
        this.add(cdg);
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
