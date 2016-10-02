
package graphs;

import java.util.ArrayList;

/**
 *
 * @author alyacarina
 */
public class ConceptNode extends MemoryNode {
    
    public ConceptNode(ArrayList<MemoryNode> neighbors, int id, Concept idea) {
        super(neighbors, id);
        initialize(idea);
    }
    
    private void initialize(Concept idea){
        this.setData(idea);
    }
    
    @Override
    public void setData(Object datum){
        if (!(datum instanceof Concept))
            throw new IllegalStateException("ConceptNodes must be equipped with a Concept!");
        super.setData(datum);
    }
    
    public Concept getConcept(){
        if(this.getData()==null) return null;
        return (Concept)this.getData();
    }
    
    public ConceptNode seekByConcept(Concept idea, ArrayList<Integer> to_ignore){
        if(this.getConcept()!=null
                 && //Concept.percentageMatch(idea, this.getConcept())>=0.5){
                this.getConcept().getRootDescriptor()
                        .equals(idea.getRootDescriptor())){
            return this;
        }
        
        to_ignore.add(this.getId());
        
        for(MemoryNode neighbor: getNeighbors()){
            if (!to_ignore.contains(neighbor.getId())){
                if(neighbor instanceof ConceptNode){
                    ConceptNode temp = ((ConceptNode)neighbor).seekByConcept(idea, to_ignore);
                    if(temp!=null){
                        return temp;
                    }
                } else {
                    MemoryNode temp = neighbor.seekByObject(idea, to_ignore);
                    if(temp!=null && temp instanceof ConceptNode){
                        return (ConceptNode)temp;
                    }
                }
                /**/
            }
        }
        
        // Not found
        return null;
    }
    
    @Override
    public String toSummary(){
        String summary = "";
        summary += "<" + getId() + "|";
        summary += getConcept().toSummary() + "|<";
        for(int i = 0; i<getNeighbors().size(); i++){
            summary+= getNeighbors().get(i).getId();
            if(i<getNeighbors().size()-1){
                summary+= ",";
            }
        }
        summary = summary.substring(0, summary.length()) + ">>";
        return summary;
    }
    
    // TEMPORARY:
    @Override
    public String toString(){
        return this.getConcept().toString();
    }
}
