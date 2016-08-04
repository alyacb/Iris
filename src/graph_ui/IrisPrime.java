
package graph_ui;

import graph_storage.IrisIndexer;
import graph_storage.ConceptGraphInterpreter;
import graph_storage.MemorySaver;
import graphs.ConceptGraph;
import graphs.ConceptNode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.UIManager;

/**
 *
 * @author alyacarina
 */
public class IrisPrime extends Panel {
    private ConceptGraph boss;
    private String[] iris_paths;
    private ConceptGraphInterpreter interpreter;
    private Grapher viewer, concept;
    private MemorySaver recaller;
    private IrisIndexer indexer;
    private Panel conceptPanel, graphs;
    private ConceptNode current;
    
    public IrisPrime(){
        super();
        initialize_blank();
    }
    
    @SuppressWarnings("empty-statement")
    private void initialize_blank() {
        setLayout(new BorderLayout());
        
        boss = new ConceptGraph();
        current = (ConceptNode) boss.root;
        iris_paths = IrisIndexer.getPaths();
        
        add("Center", display_graphs());
        add("South", display_command_pane());
        add("North", display_menu_bar());
    }
    
    private Panel display_menu_bar(){
        Panel buttons = new Panel();
        
        JComboBox loadIris = new JComboBox();
        for(String iris: iris_paths){
            loadIris.addItem(iris);
        }
        buttons.add(loadIris);
        
        JButton newGraph = new JButton();
        newGraph.setIcon(UIManager.getIcon("FileView.fileIcon"));
        newGraph.setToolTipText("New Iris");
        buttons.add(newGraph);
        
        JButton saveGraph = new JButton();
        saveGraph.setIcon(UIManager.getIcon("FileView.floppyDriveIcon"));
        saveGraph.setToolTipText("Save Iris");
        buttons.add(saveGraph);
        
        JLabel merge = new JLabel(" Merge: ");
        buttons.add(merge);
        buttons.add(new MergeConsole(boss, viewer));
        
        return buttons;
    }
    
    private Panel display_graphs(){
        viewer = new Grapher(boss) {
            @Override
            public void overrideableAction(){
                if(!current.equals(viewer.getSelected())){
                    current = (ConceptNode) viewer.getSelected();
                    graphs.remove(conceptPanel);
                    conceptPanel = display_concept();
                    graphs.add(conceptPanel);
                }
            }
        };
        
        graphs = new Panel();
        graphs.setLayout(new GridLayout(1, 2));
        
        graphs.add(viewer);
        
        conceptPanel = display_concept();
        graphs.add(conceptPanel);
        
        return graphs;
    }
    
    private Panel display_concept(){
        concept = new Grapher(current.getConcept());
        
        Panel concept_menu = new Panel();
        concept_menu.setLayout(new BoxLayout(concept_menu, BoxLayout.X_AXIS));
        Label title = new Label("Concept  ");
        title.setForeground(Color.white);
        concept_menu.add(title);
        concept_menu.add(new ConceptConsole(current.getConcept(), concept));
        
        concept.add("North", concept_menu);
        
        return concept;
    }

    private Panel display_command_pane() {
        Panel commandPane = new Panel();
        
        commandPane.add(new StringConsole(boss, viewer));
        
        return commandPane;
    }
}
