package statistics_analysis;

import statistics_distributions.ChiSquared;
import statistics_distributions.Distribution;

/**
 *
 * @author alyacarina
 */
// This is a class that determines P Values
// Note that the mean of a given distribution is irrelevant, the code only
//   finds the best p-value for the given data-set belonging to that distribution
//   with the optimal mean
public class PValueGenerator {

    private DataSet data;

    public PValueGenerator(DataSet data) {
        this.data = data;
    }

    public void changeDataSet(DataSet next) {
        this.data = next;
    }

    // Calculates pvalue after binnifying the data
    //    Note: bin_size of 0 looks at original, un-binnified dataset
    public double getPValue(Distribution d, double bin_size) {
        try {
            double lambda = 0;
            DataSet data2 = data.binnify(bin_size);
            for (int i = 0; i < d.getNumberOfParameters(); i++) {
                d.setParameter(i, d.estimateParameter(i, data));
            }

            System.out.println(d);
            for (Datum dt : data2.getRawSortedData()) {
                double e = (d.F(dt.getContent() + bin_size) - d.F(dt.getContent()))
                            * data.getTotalFrequency();
                double y = dt.frequency;
                
                lambda += y * Math.log(y / e);
            }
            lambda *= 2;

            int k = data2.getRawSortedData().size() - 1 - d.getNumberOfParameters();
            ChiSquared m = new ChiSquared(k);
            return 1 - m.F(lambda);
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }
}
