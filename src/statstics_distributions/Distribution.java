
package statstics_distributions;

/**
 *
 * @author alyacarina
 */

public abstract class Distribution {
    
    // Abstract superclass managing statistical distributions.
    //    Each concrete subclass has a unique implementation of CDF, PDF
    
    private final double parameters[];
    private final String name;
    private double DX;
    
    public Distribution(String name, double[] parameters){
        this.name = name;
        this.parameters = parameters;
    }
    
    // error methods
    protected final void throwBadArgs() throws IllegalArgumentException {
        throw new IllegalArgumentException(name + ": recieved an invalid argument.");
    }
    
    // getters, self-explanatory titles.
    public abstract double getMean();
    public double getStandardDeviation(){
        return Math.sqrt(this.getVariance());
    }
    public abstract double getVariance();
    
    public final String getName(){
        return name;
    }
    public final int getNumberOfParameters(){
        return parameters.length;
    }
    public final double getParameter(int i){
        return parameters[i];
    }
    public final double getDX(){
        return DX;
    }

    @Override 
    public final String toString(){
        String s = name;
        s+='(';
        for(int n=0; n<parameters.length-1; n++){
            s+=parameters[n]+',';
        }
        s+=parameters[parameters.length-1];
        s+=')';
        return s;
    }
    
    // method for setting integration interval size
    protected final void setDX(double dx){
        DX = dx;
    }
    
    // method for integration of f(x)
    protected final double integrate_f(double a, double b){
        double lower_limit = Math.min(a, b);
        double upper_limit = Math.max(a, b);
        double result = 0;
        for(double x = lower_limit; x<=upper_limit; x+=DX){
            result+=f(x)*DX;
        }
        return result;
    }
    
    // Methods used by several subclasses in PDF
    protected final int factorial(int n){
        int result = 1;
        for(int i=2; i<=n; i++){
            result*=i;
        }
        return result;
    }
    
    protected final int choose(int a, int b){
        int maxi = Math.max(a, b);
        int mini = Math.min(a, b);
        return factorial(maxi)/(factorial(mini)*factorial(maxi-mini));
    }
    
    // Probability Density Function
    public abstract double f(double x);
    
    // Cumulative Distribution Function
    public double F(double x){
        return integrate_f(getMean() - 5*getStandardDeviation(), x);
    }
}
