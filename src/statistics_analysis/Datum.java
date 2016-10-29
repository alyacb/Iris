
package statistics_analysis;

import java.io.Serializable;

/**
 *
 * @author alyacarina
 */
public class Datum implements Serializable {
    private final double value;
    public int frequency;
    
    public Datum(double value){
        this.value = value;
        frequency = 1;
    }
    
    public double getContent(){
        return value;
    }
    
    public String toString(){
        return "("+ value + ":" + frequency + ")";
    }
}
