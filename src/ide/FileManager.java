
package ide;

import core.Iris;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.BoxLayout;

/**
 *
 * @author alyacarina
 */
public class FileManager extends Panel {
    
    private static final File IRIS_DIRECTORY = 
            (new File("iris_store")).getAbsoluteFile();
    private Panel irises;
    private Iris brains;
    private final IDE parent;
    
    public FileManager(Iris brains, IDE parent){
        this.brains = brains;
        this.parent = parent;
        initialize_ui();
        initialize_directory();
    }
    
    public void setIris(Iris brains){
        this.brains = brains;
    }
    
    private void initialize_ui(){
        setLayout(new BorderLayout());
        
        irises = new Panel();
        irises.setLayout(new BoxLayout(irises, BoxLayout.Y_AXIS));
        add("Center", irises);
        
        Panel options = new Panel();
        Button save = new Button("S");
        save.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try(FileOutputStream fout = 
                        new FileOutputStream(IRIS_DIRECTORY.getAbsolutePath()+"/iris.ser")){
                    ObjectOutputStream oos = new ObjectOutputStream(fout);
                    oos.writeObject(brains);
                } catch(IOException eio){
                    
                }
                initialize_directory();
            }
        });
        options.add(save);
        
        add("North", options);
    }
    
    private void initialize_directory() {
        irises.removeAll();
        
        if(!(IRIS_DIRECTORY.exists()&&IRIS_DIRECTORY.isDirectory())){
            System.out.println("Did iris_store respawn? " + IRIS_DIRECTORY.mkdir());
        }
        
        String[] lst = IRIS_DIRECTORY.list();
        for(String x: lst){
            if(!x.contains(".ser")){
                continue;
            }
            Button temp = new Button(x);
            temp.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    try(FileInputStream fin = 
                            new FileInputStream(IRIS_DIRECTORY.getAbsolutePath()+"/"+temp.getLabel())){
                        ObjectInputStream ois = new ObjectInputStream(fin);
                        brains = (Iris) ois.readObject();
                        ois.close();
                    } catch(IOException ioe){
                        System.out.println(ioe.getMessage());
                    } catch (ClassNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                    parent.notifyOfBrain();
                }
            });
            irises.add(temp);
        }
        
        irises.revalidate();
    }

    public Iris getIris() {
        return brains;
    }
}
