package statistics;

/**
 *
 * @author alyacarina
 */
public class StudentTDistribution extends ContinuousDistribution {

    private double ck;
    private int degrees_of_freedom;

    public StudentTDistribution(int k) {
        degrees_of_freedom = k;
        this.mean = 0;
        this.lower_lim = -10;
        this.upper_lim = 10;
        this.name = "Student-T";
        ck = gamma((k + 1) / 2) / gamma(k / 2);
        ck /= Math.sqrt(k * Math.PI);
    }

    @Override
    public double getStandardDeviation() {
        return Math.sqrt(degrees_of_freedom / (degrees_of_freedom + 2));
    }

    @Override
    public void setMean(double mean) {
    } // mean is ALWAYS 0

    public int getDegreesOfFreedom() {
        return degrees_of_freedom;
    }

    public void setDegreesOfFreedom(int k) {
        degrees_of_freedom = k;
        ck = gamma((k + 1) / 2) / gamma(k / 2);
        ck /= Math.sqrt(k * Math.PI);
    }

    private int gamma(int k) {
        int n = 1;
        for (int i = 2; i < k; i++) {
            n *= i;
        }
        return n;
    }

    @Override
    public double f(double x) {
        double n = ck*Math.pow(x*x/degrees_of_freedom+1, -((double)degrees_of_freedom+1)/2);
        return n;
    }

}
