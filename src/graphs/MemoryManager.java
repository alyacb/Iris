
package graphs;

import graph_ui.Grapher;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author alyacarina
 */
public class MemoryManager {
    public MemoryNode root;
    protected int number_of_nodes; // Root is node 0
    
    // Constructor:
    public MemoryManager(){
        number_of_nodes = 0;
        root = new MemoryNode(new ArrayList<>(), 0);
        root.mouse_x = 200;
        root.mouse_y = 100;
    }
    
    // Method to build a randomized graph with a given number of nodes
    public void generateNewRandom(int number_of_nodes){
        if(this.number_of_nodes!=0){
            removeAllMemoryNodes();
        }
        
        if(number_of_nodes<0){
            throw new IllegalStateException("There must be at least "
                    + "one memory node for a randomized graph.");
        }
        
        // Build up randomized graph with given number_of_nodes
        for(int i=1; i<=number_of_nodes; i++){
            MemoryNode temp = new MemoryNode(new ArrayList<>(), getNextId());
            int parent_index = (int)(Math.random()*(i/2)+i/2);
            temp.mouse_x = 50+(int)(1200*Math.random());
            temp.mouse_y = 50+(int)(500*Math.random());
            addMemoryNode(temp, parent_index);
        }
        
        
    }
    
    // Method to get ID of next MemoryNode
    // Call ONLY before next node is added
    public int getNextId(){
        number_of_nodes++;
        return number_of_nodes;
    }
    
    // Method to remove all MemoryNodes (except for the root node, which cannot be removed)
    public void removeAllMemoryNodes(){
        removeMemoryNode(0);
        number_of_nodes = 0;
    }
    
    // Method to remove a MemoryNode with the given id from the Graph
    public MemoryNode removeMemoryNode(int id){
        if(id<0 || id>number_of_nodes){
            //System.out.println("Not a valid id!");
            return null;
        }
        
        MemoryNode toRemove = root.seek(id, new ArrayList<>());
        if(toRemove!=null){
            toRemove.detatchFromNeighbors();
        }
        
        // null if no node found
        return toRemove;
    }
    
    // Method to add a new MemoryNode to the root
    public void addMemoryNode(){
        addMemoryNode(0);
    }
    
    // Method to add a memory node to neighbours of node with id destination_id
    public void addMemoryNode(int destination_id) {
        if(destination_id<0 || destination_id>number_of_nodes){
            //System.out.println("Not a valid destination_id!");
            return;
        }
        
        MemoryNode toAdd = new MemoryNode(new ArrayList<>(), getNextId());
        
        MemoryNode destination = root.seek(destination_id, new ArrayList<>());
        if(destination!=null){
            destination.addNeighbor(toAdd);
        } else {
            /*System.out.println("MemoryNode with id: " + destination_id + 
                    " is inaccessible!");*/
            number_of_nodes--;
        }
    }
    
    // Method to attach a given MemoryNode to a specified one
    
    // single
    public void addMemoryNode(MemoryNode node, int destination_id){
        if(destination_id<0 || destination_id>number_of_nodes){
            //System.out.println("Not a valid destination_id!");
            return;
        }
        
        MemoryNode destination = root.seek(destination_id, new ArrayList<>());
        
        if(destination==null){
            throw new IllegalStateException("No node with id: " + destination_id 
                    + " is accessible!");
        } else {
            destination.addNeighbor(node);
        }
    }
    
    // multiple
    public void addMemoryNodes(ArrayList<MemoryNode> memories, int destination_id){
        if(destination_id<0 || destination_id>number_of_nodes){
            //System.out.println("Not a valid destination_id!");
            return;
        }
        
        MemoryNode destination = root.seek(destination_id, new ArrayList<>());
        
        if(destination==null){
            throw new IllegalStateException("No node with id: " + destination_id 
                    + " is accessible!");
        } else {
            destination.addNeighbors(memories);
        }
    }
    
    public void addStringNode(String s, int destination_id){
        MemoryNode node = new MemoryNode(new ArrayList<>(), getNextId());
        node.setData(s);
        node.mouse_x = 50+(int)(400*Math.random());
        node.mouse_y = 50+(int)(400*Math.random());
        addMemoryNode(node, destination_id);
    }
    
    public void addStringNodes(String[] s, int destination_id){
        ArrayList<MemoryNode> memories = new ArrayList<>();
        for(String x:s){
            MemoryNode temp = new MemoryNode(new ArrayList<>(), getNextId());
            temp.setData(x);
            temp.mouse_x = 50+(int)(400*Math.random());
            temp.mouse_y = 50+(int)(400*Math.random());
            memories.add(temp);
        }
        addMemoryNodes(memories, destination_id);
    }
    
    // Method to join two MemoryNodes
    public void knit(int id1, int id2){
        if(id1<0 || id1>number_of_nodes || id1<0 || id1>number_of_nodes){
            //System.out.println("Invalid node id(s) input!");
            return;
        }
        
        MemoryNode node1 = root.seek(id1, new ArrayList<>());
        MemoryNode node2 = root.seek(id2, new ArrayList<>());
        
        if(node1==null){
            throw new IllegalStateException("No node with id: " + id1 
                    + " is accessible!");
        } else if (node2==null){
            throw new IllegalStateException("No node with id: " + id1 
                    + " is accessible!");
        } else if(!node1.getNeighbors().contains(node2)){
            node1.addNeighbor(node2);
        }
    }
    
    // Method to clean Graph, i.e. remove repetition of nodes & deal with
    // missing/ detached nodes
    public void clean(ArrayList<MemoryNode> removed){
        for(int i=0; i<=number_of_nodes; i++){
            if(root.seek(i, new ArrayList<>())==null){
                if(removed == null){
                    MemoryNode temp = new MemoryNode(new ArrayList<>(), i);
                    temp.mouse_x = (int) (800*Math.random());
                    temp.mouse_y = (int) (500*Math.random());
                            
                    ArrayList<MemoryNode> tempList = new ArrayList<>();
                    tempList.add(temp);
                    root.addNeighbors(tempList);
                } else {
                    Iterator it = removed.iterator();
                    
                    while(it.hasNext()){
                        MemoryNode temp = (MemoryNode) it.next();
                        if(root.seek(temp.getId(), new ArrayList<>())!=null){
                            it.remove();
                        }
                    }
                    
                    root.addNeighbors(removed);
                }
            }
        }
    }
    
    // Get number of nodes connected to root
    public int getNumberOfNodes(){
        return number_of_nodes;
    }
    
    // Method to find a node at a given position
    public MemoryNode getNodeAtPosition(int mouse_x, int mouse_y){
        MemoryNode sought = null;
        for(int i=0; i<=number_of_nodes; i++){
            sought = root.seek(i, new ArrayList<>());
            int half = Grapher.NODE_RADIUS/2;
            if(sought!=null 
                    && (mouse_x<=sought.mouse_x+half) 
                    && (mouse_x>=sought.mouse_x-half)
                    && (mouse_y<=sought.mouse_y+half) 
                    && (mouse_y>=sought.mouse_y-half)) {
                return sought;
            }
            sought = null;
        }
        return sought;
    }
    
    // (Visual) Node-ordering Methods:
    // Method for reorganizing node-ordering in chronological order:
    public void lineUpNodes(){
        int deftx = 50;
        int defty = 50;
        if(number_of_nodes>10){
            deftx = (int) (1000/Math.sqrt(number_of_nodes));
            defty = deftx/2;
        } 
        int x_tracker = deftx;
        int y_tracker = defty;
        for(int i=0; i<=number_of_nodes; i++){
            MemoryNode temp = root.seek(i, new ArrayList<>());
            if(temp==null) continue;
            temp.mouse_x = x_tracker;
            temp.mouse_y = y_tracker;
            if(x_tracker<1000){
                x_tracker+=deftx;
            } else {
                x_tracker = deftx;
                y_tracker += defty;
            }
        }
    }
    
    private void merge(ArrayList<MemoryNode> merged, 
            ArrayList<MemoryNode> sorted_list1, 
            ArrayList<MemoryNode> sorted_list2){
        int i1 = 0;
        int i2 = 0;
        int s1 = sorted_list1.size();
        int s2 = sorted_list2.size();
        int m = merged.size();
        for(int i=0; i<m; i++){
            MemoryNode next_node;
            if(i1<s1 && i2<s2){
                MemoryNode mn1 = sorted_list1.get(i1);
                MemoryNode mn2 = sorted_list2.get(i2);
                if(mn1.getNumberOfCalls() > mn2.getNumberOfCalls()){
                    next_node = mn1;
                    i1++;
                } else {
                    next_node = mn2;
                    i2++;
                }
            } else if(i1<s1){
                next_node = sorted_list1.get(i1);
                i1++;
            } else {
                next_node = sorted_list2.get(i2);
                i2++;
            }
            merged.set(i, next_node);
        }
    }
    
    private void mergesort(ArrayList<MemoryNode> neighbors){
        if(neighbors.isEmpty() || neighbors.size()==1) return;
        
        int t = neighbors.size();
        int l = t/2;
        int r = t - t/2;
        
        ArrayList<MemoryNode> left_list = new ArrayList<>();
        for(int i=0; i<l; i++){
            left_list.add(neighbors.get(i));
        }
        ArrayList<MemoryNode> right_list = new ArrayList<>();
        for(int i=0; i<r; i++){
            right_list.add(neighbors.get(i+l));
        }
        
        mergesort(left_list);
        mergesort(right_list);
        merge(neighbors, left_list, right_list);
    }
    
    /*public void sort(ArrayList<MemoryNode> neighbors){
        int size = neighbors.size();
        for(int i=0; i<size-1; i++){
            int max = i;
            for(int j=i+1; j<size; j++){
                if(neighbors.get(j).getNumberOfCalls()
                        > neighbors.get(max).getNumberOfCalls()){
                    max = j;
                }
            }
            MemoryNode temp = neighbors.get(i);
            neighbors.set(i, neighbors.get(max));
            neighbors.set(max, temp);
        }
    }*/
    
    // Method to sort all neighbors of all nodes by access count (descending)
    public void sleep(){
        for(int i=0; i<=number_of_nodes; i++){
            try {
                MemoryNode node = root.seek(i, new ArrayList<>());
                mergesort(node.getNeighbors());
            } catch(NullPointerException e){
                System.out.println("HUH: " + i);
                //clean(null);
                //i--;
            }
        }
    }
    
    // Method to print out Graph:
    @Override
    public String toString(){
        String s = "";
        for(int i=0; i<=number_of_nodes; i++){
            try {
                s+=root.seek(i, new ArrayList<>()).toString() + "\n";
            } catch(NullPointerException e){
                System.out.println("WARNING: MemoryNode with id: " + i + 
                        " is inaccessible from root.");
            }
        }
        s = s.substring(0, s.length()-1);
        return s;
    }

    // learns new unformatted strings in an array of strings
    public void learnWords(String[] words) {
        int i = 0;
        int last_index = 0;
        
        while(i<words.length){
            MemoryNode existing = root.seekByObject(words[i], 
                    new ArrayList<>());
            if(existing==null){
                addStringNode(words[i], last_index);
                last_index = number_of_nodes;
            } else {
                if(last_index>0){
                    knit(existing.getId(), last_index);
                }
                last_index = existing.getId();
                existing.feed();
            }
            i++;
        }
        sleep();
    }
}
