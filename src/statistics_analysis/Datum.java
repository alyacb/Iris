
package statistics_analysis;

/**
 *
 * @author alyacarina
 */
public class Datum {
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
