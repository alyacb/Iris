package logic;
/**
 *
 * @author alyacarina
 */
public class Disjunction extends Formula {
    
    public Disjunction(Formula a, Formula b){
        super(new Formula[]{
            new Formula(new Formula[]{a, b}, new AdequateConnective()),
            new Formula(new Formula[]{a, b}, new AdequateConnective())
        }, new AdequateConnective());
    }
    
}
