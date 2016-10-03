
package graphs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author alyacarina
 */

/* RULES:
    1. Memory Nodes cannot be lonely (must be paired) 
    unless strs node with no memories attached.
    2. Each id MUST BE UNIQUE. Designated chronologically, by MemoryManager
*/

public class MemoryNode {
    // Fields:
    private ArrayList<MemoryNode> neighbors;
    private int number_of_calls;
    int id;
    protected Object datum;
    public int mouse_x = 0;
    public int mouse_y = 0;
    public Color edge_color = null;
    
    // Constructors:
    
    public MemoryNode(ArrayList<MemoryNode> neighbors, int id){
        this.neighbors = neighbors;
        number_of_calls = 1;
        this.id = id;
        this.datum = null;
    }
    
    // For cloning purposes:
    public MemoryNode(MemoryNode toClone){
        this.neighbors = (ArrayList<MemoryNode>)toClone.neighbors.clone();
        this.number_of_calls = toClone.getNumberOfCalls();
        this.id = toClone.getId();
        this.mouse_x = toClone.mouse_x;
        this.mouse_y = toClone.mouse_y;
        this.datum = toClone.getData();
    }
    
    public final MemoryNode cloneWithNewId(int new_id){
        MemoryNode mn = new MemoryNode(this);
        mn.id = new_id;
        return mn;
    }
    
    //Methods:
    
    // Method to find a neighbor with a given id:
    public final MemoryNode getNodeById(int neighbor_id){
        number_of_calls++;
        for(MemoryNode neighbor: neighbors){
            if(neighbor.getId()==neighbor_id){
                return neighbor;
            }
        }
        return null;
    }
    
    // Methods to remove neighbors:
    // Method to dissociate node from all neighbors
    public final void detatchFromNeighbors(){
        for(MemoryNode neighbor: neighbors){
            neighbor.detatchNeighbor(id);
        }
        this.neighbors = new ArrayList<>();
        number_of_calls++;
    }
    
    // Method to dissociate a given neighbor from neighbors
    public final void detatchNeighbor(int neighbor_id){
        for(MemoryNode neighbor: neighbors){
            if(neighbor.getId()==neighbor_id){
                neighbors.remove(neighbor);
                break;
            }
        }
        number_of_calls++;
    }
    
    // Methods to add neighbors and brothers:
    
    // Add double neighbor
    public final void addNeighbor(MemoryNode neighbor){
        if(getNodeById(neighbor.getId())!=null){
            return;
        } else {
            addPrisoner(neighbor);
            neighbor.addPrisoner(this);
        }
    }
    
    // Add many double neighbors
    public final void addNeighbors(ArrayList<MemoryNode> neighbors){
        Iterator it = neighbors.iterator();
        while(it.hasNext()){
            MemoryNode next = (MemoryNode) it.next();
            if(getNodeById(next.getId())!=null){
                it.remove();
            }
        }
        
        this.neighbors.addAll(neighbors);
        for(MemoryNode neighbor: neighbors){
            neighbor.addPrisoner(this);
        }
        number_of_calls++;
    }
    
    // Method to add a single one-way neighbor:
    // NOTE: this will result in a dead end in the graph
    public final void addPrisoner(MemoryNode neighbor){
        for(MemoryNode node: neighbors){
            if(node.getId()==neighbor.id){
                return;
            }
        }
        
        neighbors.add(neighbor);
        
        //System.out.println(id + " MODIFIED: " + toString());
        //System.out.println(neighbor.getId() + " MODIFIED: " + neighbor.toString());
        number_of_calls++;
    }
    
    //Information access methods:
    // Returns number_of_calls:
    public final int getNumberOfCalls(){
        return number_of_calls;
    }
    
    // Gets neighbors
    public final ArrayList<MemoryNode> getNeighbors(){
        return (ArrayList<MemoryNode>)neighbors.clone();
    }
    
    //Increases priority
    public final void feed(){
        number_of_calls++;
    }
    
    public final void feed(int times){
        number_of_calls += times;
    }
    
    // Returns id:
    public final int getId(){
        return id;
    }
    
    // Sets new id: use sparingly.
    public final void setId(int id){
        this.id = id;
    }
    
    // Returns node with given id:
    // Depth-first search
    public final MemoryNode seek(int destination_id, ArrayList<Integer> to_ignore) {
        if(destination_id == id){
            number_of_calls++;
            return this;
        }
        
        to_ignore.add(this.id);
        
        for(MemoryNode neighbor: neighbors){
            if (!to_ignore.contains(neighbor.getId())){
                MemoryNode temp = neighbor.seek(destination_id, to_ignore);
                if(temp!=null){
                    return temp;
                }
            }
        }
        
        // Not found
        return null;
    }
    
    public final boolean compareNeighbors(MemoryNode mn){
        for(MemoryNode n: neighbors){
            if(!mn.neighbors.contains(n)){
                   return false; 
            }
        }
        return true;
    }
    
    public final void setData(Object datum){
        this.datum = datum;
    }
    
    public final Object getData(){
        return datum;
    }
    
    // Returns string representation of node:
    @Override
    public String toString(){
        String s = "";
        if(datum!=null){
            s = datum.toString() + ": ";
        }
        String name = id + ": " + s + number_of_calls + "\n\r" 
                + "(" + mouse_x + ", " + mouse_y + ") ; (";
        for(MemoryNode neighbor: neighbors){
            name += neighbor.getId() + ": " + neighbor.getNumberOfCalls() + ", ";
        }
        name = name.substring(0, name.length()-2);
        if(name.contains("(")){
            name+=")";
        }
        return name;
    }

    public String toSummary(){
        String summary = "";
        summary += "<" + getId() + "|";
        summary += getData().toString() + "|<";
        for(int i = 0; i<neighbors.size(); i++){
            summary+= neighbors.get(i).getId();
            if(i<neighbors.size()-1){
                summary+= ",";
            }
        }
        summary = summary.substring(0, summary.length()) + ">>";
        return summary;
    }
    
    public MemoryNode seekByObject(Object datum, ArrayList<Integer> to_ignore) {
        if(this.datum!=null){
            if(this.datum instanceof Concept){
                Concept c = (Concept)this.datum;
                if(c.getRootDescriptor().equals(datum)
                        || (datum instanceof Concept 
                        && c.getRootDescriptor().equals(
                                ((Concept)datum).getRootDescriptor()))){
                    number_of_calls++;
                    return this;
                }
            } else if (this.datum.equals(datum)){
                number_of_calls++;
                return this;
            }
        }
        
        to_ignore.add(this.id);
        
        for(MemoryNode neighbor: neighbors){
            if (!to_ignore.contains(neighbor.getId())){
                MemoryNode temp = neighbor.seekByObject(datum, to_ignore);
                if(temp!=null){
                    return temp;
                }
            }
        }
        
        // Not found
        return null;
    }

    // get total frequency of all connected nodes
    public final int getNeighborFrequencySum(ArrayList<Integer> visited) {
        int count = number_of_calls;
        visited.add(id);
        for(MemoryNode mn: neighbors){
            if(visited.contains(mn.id)) continue;
            count+=mn.getNeighborFrequencySum(visited);
        }
        return count;
    }
    
}
