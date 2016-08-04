
package logic;

/**
 *
 * @author alyacarina
 */
public class Implication extends Disjunction {
    
    public Implication(Formula a, Formula b){
        super(new Negation(a), b);
    }
    
}
