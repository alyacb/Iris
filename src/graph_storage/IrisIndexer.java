
package graph_storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author alyacarina
 */
public class IrisIndexer {
    
    final static String INDEX_PATH = ".\\graph_storage\\iris_index.txt";
    final static String CORRUPTED_FILE = "Incorrect formatting of iris_index.txt; "
            + "must begin with number of Irises.";
    
    // Reads iris_index.txt and produces list of Iris file_paths
    // Respawns iris_index.txt if missing
    // Fails if iris_index.txt is corrupted
    public static String[] getPaths() {
        try (BufferedReader in = new BufferedReader(new FileReader(INDEX_PATH));){
            int n = Integer.parseInt(in.readLine());
            System.out.println(n);
            String[] iris_paths = new String[n];
            
            String nextLine;
            int i=0;
            while((nextLine = in.readLine())!=null && i<n){
                iris_paths[i] = nextLine;
                i++;
            }
            
            return iris_paths;
        } catch(FileNotFoundException fnfe){
            try(PrintWriter out = 
                new PrintWriter(new BufferedWriter(new FileWriter(INDEX_PATH, false)))){
                (new File(INDEX_PATH)).createNewFile();
                out.println(0);
            } catch(IOException ioe){
                System.out.println(ioe.getMessage());
            }
            return new String[0];
        } catch(IOException ioe){
            System.out.println(ioe.getMessage());
            return null;
        } catch(NumberFormatException nfe){
            System.out.println(CORRUPTED_FILE);
            return null;
        }
    }
    
    // Writes new path to iris_index.txt and produces list of Iris file_paths
    // Respawns iris_index.txt if missing
    // Fails if iris_index.txt is corrupted
    public static void savePath(String path) throws IOException{
        try (BufferedReader in = new BufferedReader(new FileReader(INDEX_PATH));){
            int num_paths = Integer.parseInt(in.readLine());
            num_paths++;
            
            String[] iris_paths = new String[num_paths];
            
            String nextLine;
            int i=0;
            while((nextLine = in.readLine())!=null && i<num_paths-1){
                iris_paths[i] = nextLine;
                i++;
            }
            iris_paths[i] = path;
            
            try(PrintWriter out = 
                    new PrintWriter(new BufferedWriter(new FileWriter(INDEX_PATH, false)))){ 

                out.println(num_paths);
                for(String p: iris_paths){
                    out.println(p);
                }
                
            } catch(FileNotFoundException fnfe){
                File respawn = new File(INDEX_PATH);
                respawn.createNewFile();
                savePath(path);
                
            } catch(IOException ioe){
                System.out.println(ioe.getMessage());
            }
        
        } catch(FileNotFoundException fnfe){
            File respawn = new File(INDEX_PATH);
            respawn.createNewFile();
            savePath(path);
            
        } catch(IOException ioe){
            System.out.println(ioe.getMessage());
        } catch(NumberFormatException nfe){
            System.out.println(CORRUPTED_FILE);
        }
    }
    
    // Overwrites iris_index.txt with paths
    // Respawns iris_index.txt if missing
    // Fails if iris_index.txt is corrupted
    public static void savePaths(String[] paths) throws IOException{
        int num_paths = paths.length;
        try(PrintWriter out = 
                new PrintWriter(new BufferedWriter(new FileWriter(INDEX_PATH, false)))){
            
            out.println(num_paths);
            for(String path: paths){
                out.println(path);
            }
            
        } catch(FileNotFoundException fnfe){
            File respawn = new File(INDEX_PATH);
            respawn.createNewFile();
            savePaths(paths);
            
        } catch(IOException ioe){
            System.out.println(ioe.getMessage());
        }
    }
    
}
