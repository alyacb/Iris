
package logic;

/**
 *
 * @author alyacarina
 */
public class Negation extends Formula {
    
    public Negation(Formula a){
        super(new Formula[]{a, a}, new AdequateConnective());
    }
    
}
