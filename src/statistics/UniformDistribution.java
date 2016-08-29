/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package statistics;

/**
 *
 * @author alyacarina
 */
public class UniformDistribution extends ContinuousDistribution {

    private static double RAD3 = Math.sqrt(3);
    
    public UniformDistribution(double a, double b){
        this.num_params = 2;
        this.lower_lim = Math.min(a, b);
        this.upper_lim = Math.max(a, b);
        this.mean = (upper_lim+lower_lim)/2;
        this.name = "Uniform";
    }

    @Override
    public double getStandardDeviation() {
        return (upper_lim-lower_lim)/(2*RAD3);
    }

    @Override
    public double f(double x) {
        if(x<lower_lim) return 0;
        else if (x>upper_lim) return 0;
        return 1/(upper_lim-lower_lim);
    }
    
}
