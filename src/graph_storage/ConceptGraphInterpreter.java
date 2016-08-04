package graph_storage;


import graphs.ConceptGraph;
import graph_ui.ConceptGrapher;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author alyacarina
 */
public class ConceptGraphInterpreter {
    private final String path_name;
    
    public ConceptGraphInterpreter(String path_name){
        this.path_name = path_name;
    }
    
    public ArrayList<String> extractStrings(String source){
        ArrayList<String> strings = new ArrayList<>();
        
        int open = 0;
        int closed = 0;
        int index = 0;
        String nested = "";
        while(index<source.length()){
            char current = source.charAt(index);
            if(current == '<'){
                open++;
                if(open>1){
                    nested+=current;
                } 
            } else if(current == '>'){
                closed++;
                if(open == closed){
                    strings.add(nested);
                    nested = "";
                    open = 0;
                    closed = 0;
                    index++;
                    continue;
                } 
                nested += current;
            } else if(open>closed){
                nested += current;
            }
            index++;
        }
        
        return strings;
    }
    
    public ArrayList<String> extractAllStrings(String source){
        ArrayList<String> inter = new ArrayList<>();
        inter.add(source);
        return extractAllStrings(inter);
    }
    
    public ArrayList<String> extractAllStrings(ArrayList<String> source){
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> temp;
        for(String str: source){
            temp = extractStrings(str);
            result.addAll(temp);
            result.addAll(extractAllStrings(temp));
        }
        return result;
    }
    
    public ConceptGraph interpret(){
        ConceptGraph base = new ConceptGraph();
        try (BufferedReader in = new BufferedReader(new FileReader(path_name));){
            String nextLine;
            
            //free ids:
            nextLine = in.readLine();
            ArrayList<Integer> freeds = new ArrayList<>();
            if(!nextLine.equals("[]")){
                String[] freedstr = nextLine.split(",");
                if(freedstr.length>1){
                    freedstr[0] = freedstr[0].substring(1);
                    freedstr[freedstr.length-1] = freedstr[freedstr.length-1].substring(0,
                            freedstr[freedstr.length-1].length()-1);
                } else if (freedstr.length==1){
                    freedstr[0] = freedstr[0].substring(1, freedstr[0].length()-1);
                }
                for(String s: freedstr){
                    freeds.add(Integer.parseInt(s));
                }
            }
            
            ArrayList<ArrayList<String>> nodeStrings = new ArrayList<>();
            while((nextLine = in.readLine())!=null){
                ArrayList<String> str = extractAllStrings(nextLine);
                nodeStrings.add(str);
            }
            //System.out.println(nodeStrings);
            base = ConceptGraph.generateFromStrings(nodeStrings);
            base.free_ids = freeds;
            
            /*while((nextLine = in.readLine())!=null){
                ArrayList<String> temp = extractAllStrings(nextLine);
                System.out.println(temp);
                System.out.println(extractAllStrings(nextLine));
            }*/
            
        } catch(IOException e){
            e.printStackTrace();
        }
        return base;
    }
    
    public static void main(String[] args) throws IOException{
        ConceptGraphInterpreter x = new ConceptGraphInterpreter(
                "/Users/alyacarina/NetBeansProjects/Iris/"
                                        + "src/graph_storage/graph_memory.txt");
        JFrame lookAtMe = new JFrame("Concept Grapher");
        lookAtMe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lookAtMe.setLayout(new BorderLayout());
        ConceptGrapher gijoe = new ConceptGrapher(x.interpret());
        lookAtMe.add("Center", gijoe);
        lookAtMe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        lookAtMe.setVisible(true);
    }
}
