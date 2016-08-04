
package graphs;

import java.util.ArrayList;

/**
 *
 * @author alyacarina
 */

// A concept is Iris' way of understanding an idea

public class Concept extends MemoryManager {
    
    public Concept(Object root_descriptor){
        root = new MemoryNode(new ArrayList<>(), 0);
        root.mouse_x = 100;
        root.mouse_y = 100;
        root.setData(root_descriptor);
    }
    
    public Concept(Concept old){
        root = old.root;
        number_of_nodes = old.number_of_nodes;
    }
    
    public Object getRootDescriptor(){
        return root.getData();
    }
    
    // Produce a Concept from strings
    public static Concept generateFromStrings(ArrayList<String> source, int num_nodes) {
        Concept c = new Concept(source.get(1).split("\\|")[1]);
        
        for(int i=1; i<=num_nodes; i++){
            String[] strs = source.get(i).split("\\|");
            MemoryNode mn = new MemoryNode(new ArrayList<>(), 
                    Integer.parseInt(strs[0]));
            mn.setData(strs[1]);
            mn.mouse_x = 50+(int)(300*Math.random());
            mn.mouse_y = 50+(int)(300*Math.random());
            String[] neighbor_ids = source.get(i+num_nodes+1).split(",");
            for(int j=0; j<neighbor_ids.length; j++){
                try {
                    if(!neighbor_ids[j].equals("")){
                        int id = Integer.parseInt(neighbor_ids[j]);
                        MemoryNode node = c.root.seek(id, 
                                    new ArrayList<>());
                        if(node!=null){
                            if(id>c.number_of_nodes){
                                c.number_of_nodes = id;
                            }
                            mn.addNeighbor(node);
                        }
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        
        return c;
    }
    
    // Eliminates all duplicate nodes in a Concept
    // Very inefficient- find faster way of doing this
    private void mergeDuplicates(){
        for(int i=0; i<number_of_nodes; i++){
            MemoryNode current = root.seek(i, new ArrayList<>());
            if(current==null) continue;
            for(int j=i+1; j<=number_of_nodes; j++){
                MemoryNode check = root.seek(j, new ArrayList<>());
                if(check==null) continue;
                if(check.getData().equals(current.getData())){
                    current.addNeighbors(check.neighbors);
                    current.feed(check.getNumberOfCalls());
                }
            }
        }
    }
    
    // If first_is_core is true, c1 will provide the root_descriptor.
    //    else, c2's root_descriptor will be used.
    //    this change is irreversible.
    public static Concept mergeConcepts(Concept c1, 
                                        Concept c2, 
                                        boolean first_is_core){
        Concept merged;
        if(first_is_core){
            merged = new Concept(c1);
            c2.root.setId(merged.getNextId());
            c2.root.mouse_x = 50 + (int)(300*Math.random());
            c2.root.mouse_y = 50 + (int)(300*Math.random());
            for(MemoryNode mn: c2.root.neighbors){
                mn.setId(merged.getNextId());
                mn.mouse_x = 50 + (int)(300*Math.random());
                mn.mouse_y = 50 + (int)(300*Math.random());
            }
            merged.addMemoryNode(c2.root, 0);
        } else {
            merged = new Concept(c2);
            c1.root.setId(merged.getNextId());
            c1.root.mouse_x = 50 + (int)(300*Math.random());
            c1.root.mouse_y = 50 + (int)(300*Math.random());
            for(MemoryNode mn: c1.root.neighbors){
                mn.setId(merged.getNextId());
                mn.mouse_x = (int)(300*Math.random());
                mn.mouse_y = (int)(300*Math.random());
            }
            merged.addMemoryNode(c1.root, 0);
        }
        
        merged.mergeDuplicates();
        
        return merged;
    }
    
    public static double percentageMatch(Concept c1, Concept c2){
        int count = 0;
        int total = Math.max(c1.number_of_nodes, c2.number_of_nodes);
        if(total == 0) return 0;
        
        if(total == c1.number_of_nodes){
            for(int i=0; i<total; i++){
                MemoryNode m = c1.root.seek(i, new ArrayList<>());
                if(m==null){
                    throw new IllegalStateException("Incomplete Graph!");
                } else if(c2.root.seekByObject(m.getData(), 
                        new ArrayList<>())!=null){
                    count++;
                }
            }
        } else if(total == c2.number_of_nodes){
            for(int i=0; i<total; i++){
                MemoryNode m = c2.root.seek(i, new ArrayList<>());
                if(m==null){
                    throw new IllegalStateException("Incomplete Graph!");
                } else if(c1.root.seekByObject(m.getData(), 
                        new ArrayList<>())!=null){
                    count++;
                }
            }
        }
        
        return (double)count/(double)total;
    }
    
    @Override
    public String toString(){
        return root.getData().toString();
    }
    
    private String summarize(MemoryNode current, ArrayList<Integer> to_ignore){
        String summary = "";
        summary+= current.toSummary();
        to_ignore.add(current.getId());
        for(MemoryNode node: current.neighbors){
            if(to_ignore.contains(node.getId())) continue;
            summary +=  "!" + summarize(node, to_ignore);
        }
        summary += "";
        return summary;
    }
    
    public String toSummary(){
        return summarize(root, new ArrayList<>());
    }
}
