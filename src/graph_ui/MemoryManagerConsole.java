package graph_ui;

import graph_ui.Grapher;
import graph_storage.MemorySaver;
import graphs.ConceptGraph;
import graphs.ConceptNode;
import graphs.MemoryManager;
import graphs.MemoryNode;
import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import graph_storage.IrisFileReader;
import ui_general_utils.EnterKeyListener;

/**
 *
 * @author alyacarina
 */
//NOTE: needs work.
public class MemoryManagerConsole extends Panel {

    private MemoryManager memory;
    MemoryNode temp;
    ArrayList<MemoryNode> tempList;
    private TextArea inputTaker, logger;

    public MemoryManagerConsole(MemoryManager memory, Grapher g) {
        this.memory = memory;

        temp = null;
        tempList = null;

        setLayout(new BorderLayout());

        inputTaker = new TextArea("", 1, 25, TextArea.SCROLLBARS_NONE);
        inputTaker.setEditable(true);
        inputTaker.addKeyListener(new EnterKeyListener() {
            @Override
            public void doThis(KeyEvent e) {
                String command = inputTaker.getText();
                while(command.contains("\n")){
                    command = command.substring(0, command.indexOf("\n"))
                            + command.substring(command.indexOf("\n")+1);
                }
                String returned;
                try {
                    returned = processCommand(command);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    returned = "";
                }
                if(!returned.equals("")){
                    logger.append(returned + "\n");
                }
                g.refresh();
                inputTaker.setText("");
            }
        });
        add("North", inputTaker);

        logger = new TextArea("", 3, 25, TextArea.SCROLLBARS_VERTICAL_ONLY);
        logger.setEditable(false);
        add("Center", logger);
    }

    public String processCommand(String command) throws IOException {
        if(command.startsWith("IRIS")){
            command = command.toLowerCase();
            String toProcess = command.substring("IRIS ".length());
            toProcess = toProcess.toLowerCase();
            String[] words = toProcess.split(" ");
            
            memory.learnWords(words);
            
            return "String Network Built!";
        }
        switch (command) {
            case "partition":
                if (memory instanceof ConceptGraph){
                    System.out.println("beginning");
                    ((ConceptGraph) memory).partitionStart();
                    System.out.println("ending");
                }
                break;
            case "think":
                memory.sleep();
                int l = (int)(Math.random()*20);
                int i0 = (int)(Math.random()*memory.number_of_nodes);
                if(memory instanceof ConceptGraph){
                    ConceptGraph cg = (ConceptGraph)memory;
                    ConceptNode x = (ConceptNode) cg.root.seek(i0, new ArrayList<>());
                    String response = "";
                    ArrayList<String> repetitions = new ArrayList<>();
                    for(int i=0; i<l; i++){
                        String summand = (String)x.getConcept().root.getData();
                        if(repetitions.contains(summand)){
                            x = (ConceptNode)x.neighbors.get(
                                    (int)(Math.random()*x.neighbors.size()));
                            summand = (String)x.getConcept().root.getData();
                        }
                        response+= " " + summand;
                        int j;
                        for(j=0; j<x.neighbors.size()-1; j++){
                            if(Math.random()>0.8){
                                break;
                            }
                        }
                        x = (ConceptNode)x.neighbors.get(j);
                        repetitions.add(summand);
                    }
                    return response;
                }
                break;
            case "save":
                MemorySaver ms = 
                        new MemorySaver(memory, 
                                "/Users/alyacarina/NetBeansProjects/Iris/"
                                        + "src/graph_storage/graph_memory.txt");
                ms.save_to_file();
                break;
            case "line up":
            case "line":
            case "tango":
                memory.lineUpNodes();
                break;
            case "random":
                memory.generateNewRandom((int)(Math.random()*10));
                break;
            case "add":
                if (temp == null) {
                    memory.addMemoryNode();
                } else if (memory.root.seek(temp.getId(),
                        new ArrayList<>()) == null) {
                    ArrayList<MemoryNode> tempList2 = new ArrayList<>();
                    tempList2.add(temp);
                    memory.addMemoryNodes(tempList2, 0);
                    temp = null;
                } else {
                    temp = null;
                    return "Temporarily stored node has been replaced & dumped.";
                }
                break;
            case "sleep":
                memory.sleep();
                break;
            case "print":
                return memory.toString();
            default:
                if (command.contains("read")){
                    command = command.substring(0, command.indexOf("read"))
                            + command.substring(command.indexOf("read") + 4);
                    command = command.trim();
                    IrisFileReader irf = new IrisFileReader(command, memory);
                    irf.readIn();
                } else if (command.contains("remove")) {
                    command = command.substring(0, command.indexOf("remove"))
                            + command.substring(command.indexOf("remove") + 6);
                    try {
                        command = command.trim();
                        int i = Integer.parseInt(command);
                        if (i == 0) {
                            memory.removeAllMemoryNodes();
                        } else {
                            tempList = memory.root.seek(i, new ArrayList<>()).neighbors;
                            temp = memory.removeMemoryNode(i);
                        }
                    } catch (Exception e) {
                        if (command.contains("all")) {
                            memory.removeAllMemoryNodes();
                        } else {
                            e.printStackTrace();
                            return "Invalid id: " + command;
                        }
                    }
                } else if (command.contains("add") && !(memory instanceof ConceptGraph)) {
                    command = command.substring(0, command.indexOf("add"))
                            + command.substring(command.indexOf("add") + 3);
                    try {
                        command = command.trim();
                        int i = Integer.parseInt(command);
                        if (temp == null) {
                            memory.addMemoryNode(i);
                        } else if (memory.root.seek(temp.getId(),
                                new ArrayList<>()) == null) {
                            ArrayList<MemoryNode> tempList2 = new ArrayList<>();
                            tempList2.add(temp);
                            memory.addMemoryNodes(tempList2, i);
                        } else {
                            temp = null;
                            return "Temporarily removed node "
                                    + "has been replaced!" + " Node has been dumped.";
                        }
                    } catch (Exception e) {
                        return "Invalid id: " + command;
                    }
                } else if (command.contains("knit")) {
                    command = command.substring(0, command.indexOf("knit"))
                            + command.substring(command.indexOf("knit") + 4);
                    try {
                        command = command.trim();
                        String[] nodeIds = command.split(" ");
                        MemoryNode[] nodes = new MemoryNode[nodeIds.length];
                        for (int i = 0; i < nodeIds.length; i++) {
                            nodes[i] = memory.root.seek(
                                    Integer.parseInt(nodeIds[i]),
                                    new ArrayList<>());
                        }
                        for (int i = 0; i < nodeIds.length; i++) {
                            for (int j = 0; j < nodeIds.length; j++) {
                                nodes[i].addPrisoner(nodes[j]);
                            }
                        }
                    } catch (Exception e) {
                        return "Invalid id(s): " + command;
                    }
                } else if (command.contains("clean")) {
                    if (command.contains("true") || command.contains("replace")) {
                        tempList.add(temp);
                        memory.clean(tempList);
                        tempList = null;
                    } else {
                        memory.clean(null);
                    }
                } else if(command.contains("merge") 
                        && memory instanceof ConceptGraph){
                    command = command.substring(0, command.indexOf("merge"))
                            + command.substring(command.indexOf("merge") + 5);
                    command = command.trim();
                    try{
                        int n1 = Integer.parseInt(command.substring(0, 1).trim());
                        int n2 = Integer.parseInt(command.substring(1).trim());
                        ConceptGraph tempmory = (ConceptGraph)memory;
                        tempmory.mergeConceptNodes(n1, n2, true);
                        //memory = tempmory;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "Unknown Command: " + command;
                    }
                } else {
                    if(command.contains("random")){
                        command = command.substring(0, command.indexOf("random"))
                            + command.substring(command.indexOf("random") + 6);
                    }
                    try {
                        command = command.trim();
                        int n = Integer.parseInt(command);
                        memory.generateNewRandom(n);
                        return "";
                    } catch (Exception e) {
                        return "Unknown Command: " + command;
                    }
                }
        }
        
        return "";
    }

    public static void main(String[] args) {
        MemoryManager mmo = new MemoryManager();
        MemoryManagerConsole mmc = new MemoryManagerConsole(mmo, new Grapher(mmo));

        try (BufferedReader systemin
                = new BufferedReader(new InputStreamReader(System.in))) {
            String command;

            while ((command = systemin.readLine()) != null) {
                if ("break".equals(command) || "end".equals(command)) {
                    break;
                } 
                String result = mmc.processCommand(command);
                if(result!="")
                    System.out.println(result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
