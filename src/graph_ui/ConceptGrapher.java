
package graph_ui;

import graph_ui.Grapher;
import graph_storage.ConceptGraphInterpreter;
import graphs.Concept;
import graphs.ConceptGraph;
import graphs.MemoryNode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Panel;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author alyacarina
 */

public class ConceptGrapher extends Panel {
    private ConceptGraph gilgamesh;
    private MemoryNode current;
    private Panel graphs;
    private Grapher east_graph, west_graph;
     
    public ConceptGrapher(ConceptGraph enkidu){
        gilgamesh = enkidu;
        current = gilgamesh.root;
        initialize();
    }
    
    private void initialize(){
        setLayout(new BorderLayout());
        graphs = initializeGraphs();
        add("Center", graphs);
        add("South", new MemoryManagerConsole(gilgamesh, east_graph));
    }
    
    private Panel initializeGraphs(){
        Panel p = new Panel();
        p.setLayout(new GridLayout(1, 2));
        east_graph = initializeConceptGraph();
        west_graph = initializeConcept();
        p.add(west_graph);
        p.add(east_graph);
        return p;
    }

    private Grapher initializeConceptGraph() {
        Grapher g = new Grapher(gilgamesh, false){
            @Override
            public void overrideableAction(){
                if(!current.equals(getSelected())){
                    current = getSelected();
                    refreshConcept();
                }
            }
            
            @Override
            public MemoryNode getNewMemoryNode(){
                MemoryNode mnemo = super.getNewMemoryNode();
                mnemo.setData(new Concept(mnemo.getId()));
                return mnemo;
            }
        };
        return g;
    }

    private Grapher initializeConcept() {
        Grapher g = new Grapher((Concept)current.getData(), false);
        g.add("North", new JLabel("Concept #" + current.getId()));
        g.setBackground(Color.white);
        return g;
    }
    
    public void refreshConcept(){
        graphs.remove(east_graph);
        graphs.remove(west_graph);
        west_graph = initializeConcept();
        graphs.add(west_graph);
        graphs.add(east_graph);
    }
    
    public ConceptGraph load(){
        gilgamesh = new ConceptGraphInterpreter("/Users/alyacarina/NetBeansProjects/Iris/"
                                        + "src/graph_storage/graph_memory.txt").interpret();
                
        return gilgamesh;
    }
    
    public static void main(String[] args){
        JFrame lookAtMe = new JFrame("Concept Grapher");
        lookAtMe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lookAtMe.setLayout(new BorderLayout());
        lookAtMe.add("Center", new ConceptGrapher(new ConceptGraph()));
        lookAtMe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        lookAtMe.setVisible(true);
    }
}
