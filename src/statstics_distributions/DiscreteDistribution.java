
package statstics_distributions;

/**
 *
 * @author alyacarina
 */
public abstract class DiscreteDistribution extends Distribution {
    
    // abstract superclass for grouping discrete distributions
    
    private final double max;
    private final static int DISCRETE_DX = 1;

    public DiscreteDistribution(String name, double[] parameters, double max) {
        super(name, parameters);
        this.max = max;
        setDX(DISCRETE_DX);
    }
    
    // Methods to be overridden by Discrete subclasses
    public abstract double f_implementation(int x);
    
    // Probability Density Function
    @Override
    public double f(double x){
        if(x<0 || x>max) {
            return 0;
        }
        return f_implementation((int)x);
    }
    
    // Cumulative Density Function
    @Override
    public double F(double x){
        if(x<0) {
            return 0;
        } else if(x >= max){
            return 1;
        }
        
        int result = 0;
        for(int i=0; i<=x; i++){
            result+=f(x);
        }
        return result;
    }
    
}
