
package graph_storage;

import graphs.ConceptGraph;
import graphs.MemoryManager;
import graphs.MemoryNode;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author alyacarina
 */
public class MemorySaver {
    MemoryManager seed;
    String memory_location;
    
    public MemorySaver(MemoryManager seed, String file_path){
        if(seed==null) 
            throw new NullPointerException("Can't store a graph with a null root.");
        this.seed = seed;
        this.memory_location = file_path;
    }
    
    private void span(MemoryNode node, 
                      PrintWriter out, 
                      ArrayList<Integer> to_ignore){
        out.println(node.toSummary());
        to_ignore.add(node.getId());
        for(MemoryNode current: node.getNeighbors()){
            if(to_ignore.contains(current.getId())) continue;
            span(current, out, to_ignore);
        }
    }
    
    public void save_to_file(){
        try(PrintWriter out = 
                new PrintWriter(new BufferedWriter(new FileWriter(memory_location, false)))){
            String x = "";
            if(seed instanceof ConceptGraph){
                x = ((ConceptGraph)seed).free_ids.toString();
            }
            out.println(x);
            span(seed.root, out, new ArrayList<>());
        } catch(FileNotFoundException fnfe){
            File respawn = new File(memory_location);
            respawn.mkdirs();
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
}
