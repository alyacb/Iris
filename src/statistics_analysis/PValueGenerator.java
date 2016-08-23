
package statistics_analysis;

import java.util.ArrayList;
import statistics.DataSet;
import statistics.Datum;
import statistics.Distribution;

/**
 *
 * @author alyacarina
 */

// This is a class that determines

public class PValueGenerator {
    
    private DataSet data;
    
    public PValueGenerator(DataSet data){
        this.data = data;
    }
    
    public void changeDataSet(DataSet next){
        this.data = next;
    }
    
    public double getPValue(Distribution d){
        double mean = findMostLikelyMean(d); // sets mean of d to this
        
        double test_statistic = 0;
        for(Datum dt: data.getRawSortedData()){
            test_statistic += dt.getContent()
                    *Math.log(dt.getContent()/d.f(dt.getContent()));
        }
        test_statistic *= 2;
        
        ChiSquared m = new ChiSquared();
        return m.F(test_statistic);
    }
    
    private double findMostLikelyMean(Distribution dist){
        double max = dist.getLowerLimit();
        
        for(double x = dist.getLowerLimit(); x<=dist.getUpperLimit(); x+=dist.getDx()){
            dist.setMean(x);
            double gof = getGoodnessOfFit(dist);
            if(gof>max){
                max = gof;
            }
        }
        
        dist.setMean(max);
        return max;
    }
    
    private double getGoodnessOfFit(Distribution dist){
        double likelihood = 1;
        
        ArrayList<Datum> ald = data.getRawSortedData();
        for(Datum d: ald){
            likelihood*=Math.pow(dist.f(d.getContent()), d.frequency);
        }
        
        return likelihood;
    }
    
}
