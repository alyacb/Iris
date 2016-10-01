
package statistics_distributions;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import statistics_analysis.DataSet;
import statistics_ui.DistributionGrapher;

/**
 *
 * @author alyacarina
 */

// This is a distribution made up of multiple other distributions, where the pdf of
//    each distribution applies over a certain interval
// NOTE: closed intervals

public class CompositeDistribution extends Distribution {
    
    private final Component[] components;
    private double range;

    class Component {
        Distribution distribution;
        double interval_start;
        double interval_end;
        
        Component(double s, double e, Distribution d){
            interval_start = s;
            interval_end = e;
            distribution = d;
        }
    }
    
    // Ranges must be in same order as their distributions
    public CompositeDistribution(double[][] ranges, Distribution[] distributions) {
        super("Composite Distribution", new double[]{});
        setDX(0.001);
        
        if(ranges[0].length!=2 || ranges.length != distributions.length){
            throwBadArgs();
        }
        
        components = new Component[ranges.length];
        for(int i=0; i<ranges.length; i++){
            components[i] = new Component(ranges[i][0], ranges[i][1], distributions[i]);
        }
        
        validate();
        
        range = 0;
        for(Component c: components){
            range+=(c.interval_end-c.interval_start);
        }
    }

    @Override
    public double getMean() {
        double d = 0;
        for(Component c: components){
           d+=c.distribution.getMean();
        }
        d/=components.length;
        return d;
    }

    @Override
    public double getVariance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double f(double x) {
        if(x<components[0].interval_start
                || x>components[components.length-1].interval_end){
            return 0;
            
        } else {
            int i=1;
            while(i<components.length && x>components[i].interval_start){
                i++;
            }
            
            if(x>components[i-1].interval_end){
                return 0;
            }
            
            return components[i-1].distribution.f(x)
                    *(components[i-1].interval_end-components[i-1].interval_start)/range;
        }
    }

    @Override
    public double F(double x){
        if(x<components[0].interval_start){
            return 0;
        } else if(x>components[components.length-1].interval_end){
            return 1;
        } else {
            int i=1;
            while(i<components.length && x>components[i].interval_start){
                i++;
            }
            
            double d = 0;
            for(int j=0; j<i-1; j++){
                d+=(components[j].interval_end-components[j].interval_start);
            }
            
            if(x<components[i-1].interval_end){
                d+=components[i-1].distribution.F(x)*
                        (components[i-1].interval_end-components[i-1].interval_start);
            }
            
            return d/range;
        }
    }
    
    @Override
    protected double est_param_impl(int i, DataSet data) {
        throw new UnsupportedOperationException("Cannot estimate 0 parameters."); 
        // SHOULD NEVER BE CALLED
    }

    @Override
    protected final void validate() throws IllegalArgumentException {
        // Sorts list of components so ranges fit
        // SHOULD NEVER BE CALLED OUTSIDE OF CONSTRUCTOR
        
        // check for bad intervals
        for(Component c: components){
            if(c.interval_end<c.interval_start){
                throwBadArgs();
            }
        }
        
        //sort intervals
        for(int i=0; i<components.length; i++){
            for(int j=i+1; j<components.length; j++){
                if(components[i].interval_start>components[j].interval_start){
                    Component temp = components[i];
                    components[i] = components[j];
                    components[j] = temp;
                }
            }
        }
        
        // check for overlapping intervals
        Component c0 = components[0];
        for(int i=1; i<components.length; i++){
            if(c0.interval_end>components[i].interval_start){
                throwBadArgs();
            }
            c0 = components[i];
        }
    }
    
    public static void main(String[] args){
        CompositeDistribution cd = new CompositeDistribution(
                new double[][]{{-1,0},{0.3,1}}, new Distribution[]{
                    new Normal(0,0.5), 
                    new Normal(0.1,0.25)}
        );
        
        JFrame lookAtMe = new JFrame("DataSet Grapher");
        lookAtMe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lookAtMe.setLayout(new BorderLayout());
        DistributionGrapher m = new DistributionGrapher(cd);
        lookAtMe.add("Center", m);
        lookAtMe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        lookAtMe.setVisible(true);
    }
    
}
