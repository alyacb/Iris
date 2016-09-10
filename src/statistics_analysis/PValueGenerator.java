
package statistics_analysis;

import data_sets.DataSet;
import data_sets.Datum;
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
    
    public PValueGenerator(DataSet data){
        this.data = data;
    }
    
    public void changeDataSet(DataSet next){
        this.data = next;
    }
    
    // Calculates pvalue for a given, un-binnified data-set 
    public double getPValue(Distribution d){
        d.setMean(data.getMean()); // sets mean of d to most likely mean
        
        double lambda = 0;
        for(Datum dt: data.getRawSortedData()){
            double e = d.f(dt.getContent())*data.getTotalFrequency();
            System.out.println(e);
            double y = dt.frequency;
            System.out.println(y);
            
            lambda += y*Math.log(y/e);
        }
        lambda *= 2;
        
        int k = data.getNumberOfDataPoints()-1-d.getNumberOfParameters();
        ChiSquared m = new ChiSquared(k);
        return 1-m.F(lambda);
    }
    
    // Calculates pvalue after binnifying the data
    public double getPValue(Distribution d, double bin_size){
        double lambda = 0;
        d.setMean(data.getMean());
        
        DataSet data2 = data.binnify(bin_size);
        for(Datum dt: data2.getRawSortedData()){
            double e = (d.F(dt.getContent()+bin_size)-d.F(dt.getContent()))
                    *data2.getTotalFrequency();
            double y = dt.frequency;
            
            lambda += y*Math.log(y/e);
        }
        lambda *= 2;
        
        int k = data2.getRawSortedData().size()-1-d.getNumberOfParameters();
        ChiSquared m = new ChiSquared(k);
        return 1-m.F(lambda);
    }
}
