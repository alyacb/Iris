package statistics_analysis;

import java.util.ArrayList;

/**
 *
 * @author alyacarina
 */

public class DataSet {

    private ArrayList<Datum> data;
    private ArrayList<Double> mode;
    private double mean, variance;
    private int dataNumber, modeFrequency, totalFrequency;

    // Constructors:
    public DataSet() {
        data = new ArrayList<>();
        mode = new ArrayList<>();
        modeFrequency = 0;
        dataNumber = 0;
        totalFrequency = 0;

        // Useless values:
        mean = 0;
        variance = 0;
    }

    public DataSet(ArrayList<Datum> data) {
        data.stream().forEach((thing) -> {
            for (int i = 0; i < thing.frequency; i++) {
                addDatum(thing.getContent());
            }
        });
    }

    // Clone:
    public DataSet(DataSet clone) {
        this.data = (ArrayList<Datum>) clone.data.clone();
        this.mode = (ArrayList<Double>) clone.mode.clone();
        this.modeFrequency = clone.modeFrequency;
        this.mean = clone.mean;
        this.variance = clone.variance;
        this.totalFrequency = clone.totalFrequency;
    }
    
    // Update:

    // helper
    private void updateThing(Datum thing) {
        double value = thing.getContent();
        // Update mode as needed
        if (thing.frequency > modeFrequency) {
            mode = new ArrayList<>();
            mode.add(value);
            modeFrequency = thing.frequency;
        }

        // Update mean and standard deviation
        variance += Math.pow(mean, 2);
        variance *= dataNumber;
        variance += Math.pow(value, 2);

        mean *= dataNumber;
        mean += value;
        dataNumber++;
        mean /= dataNumber;
        variance /= dataNumber;
        variance -= Math.pow(mean, 2);
        
        totalFrequency ++;
    }

    public void addDatum(double value) {
        int where_to_insert = 0;
        for (Datum thing : data) {
            if (thing.getContent() == value) {
                thing.frequency++;

                updateThing(new Datum(value));

                return;
            } else if (thing.getContent() > value) {
                break;
            }
            where_to_insert++;
        }

        // If there is no datum in there already
        data.add(where_to_insert, new Datum(value));

        updateThing(new Datum(value));
    }

    // helper
    private void unupdateThing(Datum thing) {
        double value = thing.getContent();
        // Update mean and standard deviation
        variance += Math.pow(mean, 2);
        variance *= dataNumber;
        variance -= Math.pow(value, 2);

        mean *= dataNumber;
        mean -= value;

        dataNumber--;
        if (dataNumber > 0) {
            mean /= dataNumber;
            variance /= dataNumber;
            variance -= Math.pow(mean, 2);
        } else {
            mean = 0;
            variance = 0;
        }

        // Update mode
        if (mode.contains(value)) {
            if (modeFrequency - 1 == thing.frequency && mode.size() == 1) {
                modeFrequency--;
                data.stream().filter((thing2) -> 
                        (thing2.frequency == modeFrequency)).forEach((thing2) -> {
                    mode.add(thing2.getContent());
                });
            } else {
                mode.remove(value);
            }
        }
        
        totalFrequency -= thing.frequency;
    }
    
    int indexSearch(double value, int start, int end){
        if(data.isEmpty() || end < start){
            return -1;
        }
        
        int mid = (start + end)/2;
        if(data.get(mid).getContent() == value){
            return mid;
        } else if(data.get(mid).getContent() > value){
            return indexSearch(value, start, mid-1);
        } else {
            return indexSearch(value, mid+1, end);
        }
    }

    public void removeDatum(double value) {
        int i = indexSearch(value, 0, data.size()-1);
        
        if(i!=-1){
            Datum thing = data.remove(i);
            unupdateThing(thing);
        }
    }

    @Override
    public String toString() {
        return "mean: " + mean + "; mode(s): " + mode + "; mode frequency: " + modeFrequency
                + "\nstandard deviation: " + getStandardDeviation() + "; variance: " + variance
                + "; data points: " + dataNumber + "; CDF: " + getCumulativeDistribution()
                + "; max: " + getMax() + "; min: " + getMin() + "\n" + data.toString();
    }

    // Getters:
    public double getStandardDeviation() {
        return Math.sqrt(variance);
    }

    public double getMean() {
        return mean;
    }

    public double getVariance() {
        return variance;
    }

    public ArrayList<Double> getMode() {
        return mode;
    }

    public int getNumberOfDataPoints() {
        return dataNumber;
    }

    public int getModeFrequency() {
        return modeFrequency;
    }

    public double getMax() {
        if(data.isEmpty())
            return 0;
        return data.get(data.size() - 1).getContent();
    }

    public double getMin() {
        if(data.isEmpty())
            return 0;
        return data.get(0).getContent();
    }

    public ArrayList<Double> getCumulativeDistribution() {
        ArrayList<Double> cdf = new ArrayList<>();
        if (dataNumber == 0) {
            return null;
        }
        cdf.add((double) data.get(0).frequency / dataNumber);
        for (int i = 1; i < data.size(); i++) {
            cdf.add((double) data.get(i).frequency / dataNumber + cdf.get(i - 1));
        }
        return cdf;
    }
    
    public int getFrequency(double value){
        int i = indexSearch(value, 0, data.size()-1);
        if(i!=-1){
            return data.get(i).frequency;
        } else {
            return 0;
        }
    }
    
    public ArrayList<Datum> getRawSortedData(){
        return data;
    }
    
    // Gets total number of data-points
    public int getTotalFrequency(){
        return totalFrequency;
    }
    
    public DataSet binnify(double bin_size){
        DataSet dt = new DataSet();
        
        double lower = getMin();
        
        for(Datum thing: data){
            if(thing.getContent() > lower + bin_size){
                lower = thing.getContent();
            } 
            
            dt.addDatum(lower);
            
            for(int i=1; i<thing.frequency; i++){
                dt.addDatum(lower);
            }
        }
        
        return dt;
    }

    /*// For testing purposes:
    public static void main(String[] args) {
        DataSet aset = new DataSet();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String next;
            while ((next = br.readLine()) != null
                    && !next.equals("exit")) {
                try {
                    double d = Double.parseDouble(next.substring(1));
                    if (next.contains("a")) {
                        aset.addDatum(d);
                    } else if (next.contains("r")) {
                        aset.removeDatum(d);
                    } else if (next.contains("f")) {
                        System.out.println(aset.getFrequency(d));
                    } else if (next.contains("b")) {
                        System.out.println(aset.binnify(d));
                    }
                    System.out.println(aset);
                } catch (Exception e) {
                    // ignore value
                    System.out.println("NaN");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }*/
}
