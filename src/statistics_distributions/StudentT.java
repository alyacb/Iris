package statistics_distributions;

import statistics_analysis.DataSet;

/**
 *
 * @author alyacarina
 */
public class StudentT extends ContinuousDistribution {

    // implements Student T's distribution
    private final double COEFFICIENT;

    public StudentT(int k) {
        super("Student T's", new double[]{k});
        if (k <= 0) {
            throwBadArgs();
        } else if (k > 100) {
            COEFFICIENT = 1 / Math.sqrt(2 * Math.PI);
            return;
        }
        double num = gamma((double) (k + 1) / 2);
        double denom = (Math.sqrt(Math.PI * k) * gamma(((double) k) / 2));
        COEFFICIENT = num / denom;
    }

    @Override
    public double getMean() {
        return 0; // Note that its undefined for k=1, irrelevant to my calculations (for now)
    }

    @Override
    public double getVariance() {
        return getParameter(0) / (getParameter(0) - 2);
    }

    @Override
    public double f(double x) {
        if(getParameter(0)>100){
            return COEFFICIENT*Math.exp(-Math.pow(x, 2)/2);
        }
        return COEFFICIENT * Math.pow(1 + x * x / getParameter(0), -(getParameter(0) + 1) / 2);
    }

    @Override
    protected double est_param_impl(int i, DataSet data) {
        double variance = data.getVariance();
        return variance * 2 / (variance - 1);
    }
}
