
package statistics_analysis;

import statistics_distributions.Distribution;

/**
 *
 * @author alyacarina
 */
public class DistributionGenerator {

    // A class that generates the most appropriate distribution for a data-set
    
    private final DataSet source;
    private boolean integers, positive;
    
    public DistributionGenerator(DataSet dataSet) {
        source = dataSet;
        
        integers = true;
        positive = true;
        for(Datum d: source.getRawSortedData()){
            if(!(d.getContent() - (int)d.getContent() == 0)){
                integers = false;
            }
            if(d.getContent()<0){
                positive = false;
            }
            if(!positive && !integers){
                break;
            }
        }
    }
    
    // Generates a distribution that best fits the dataset
    public Distribution generateBestDistribution(){
        return null;
    }
    
}
