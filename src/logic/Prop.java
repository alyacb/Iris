
package logic;

/**
 *
 * @author alyacarina
 */
public class Prop {
    private boolean value;
    private final String name;
            
    // False by default
    public Prop(String name){
        value = false;
        this.name= name;
    }
    
    public Prop(Prop p){
        value = p.value;
        this.name= p.getName();
    }
    
    // Sets current prop value
    public boolean setValue(boolean value){
        this.value = value;
        return value;
    }
    
    // Gets current prop setting
    public boolean getValue(){
        return this.value;
    }
    
    // Gets current prop name
    public String getName(){
        return this.name;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
