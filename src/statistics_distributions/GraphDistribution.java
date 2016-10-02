
package statistics_distributions;

import graphs.DistributionManager;
import statistics_analysis.DataSet;

/**
 *
 * @author alyacarina
 */
public class GraphDistribution extends Distribution {

    private final DistributionManager graph_boss;
    
    public GraphDistribution() {
        super("Graph Distribution", new double[]{});
        graph_boss = new DistributionManager();
    }

    @Override
    public double getMean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getVariance() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double f(double x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected double est_param_impl(int i, DataSet data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void validate() throws IllegalArgumentException {
        // nothing to do, no args
    }
    
}
