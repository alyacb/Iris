
package statistics_ui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import javax.swing.JFrame;
import statistics_analysis.DataSet;
import statistics_analysis.Datum;

/**
 *
 * @author alyacarina
 */

// Graphs the frequency distribution of bins based on the size of each bin

public class DataSetGrapher extends Panel {
    private final DataSet data;
    private DataSet bin;
    private Canvas face;
    private double step_size;
    
    private final int x_base, y_base, padding;
    
    public DataSetGrapher(DataSet data){
        x_base = 30;
        y_base = 50;
        padding = 15;
        
        this.data = data;
        step_size = 1;
        bin = data.binnify(step_size);
        initialize();
    }
    
    private void initialize(){
        setLayout(new BorderLayout());

        face = new Canvas(){
            @Override
            public void paint(Graphics g){
                Graphics2D g2d = (Graphics2D)g;
                
                int width = this.getWidth() - x_base;
                int height = this.getHeight() - y_base;
                
                int bin_width = (int) ((double)(width-x_base)
                        /(double)bin.getRawSortedData().size());
                
                int x0 = x_base;
                
                for(Datum thing: bin.getRawSortedData()){
                    int y = (int) ((height - y_base)* 
                            ((double)thing.frequency/
                             (double)bin.getTotalFrequency()));
                    g2d.setColor(new Color((int) (255 * Math.random()),
                                            (int) (255 * Math.random()),
                                            (int) (255 * Math.random())));
                    g2d.fillRect(x0, height-y, bin_width, y);
                    g2d.drawString("["+thing.getContent()+"-"+
                            (thing.getContent()+step_size)
                            + "]", x0, height+padding);
                    
                    x0 += bin_width;
                }
                
                g2d.setColor(Color.black);
                g2d.drawString("0", padding, height);
                g2d.drawString("1", padding, y_base);
                
                g2d.drawLine(x_base, height, x_base, y_base);
                g2d.drawLine(x_base, height, width, height);
            }
        };
        add("Center", face);
        //resize(face);
    }
    
    public void refresh(double step_size){
        this.step_size = step_size;
        bin = data.binnify(step_size);
        face.repaint();
    }
    
    public static void main(String[] args) {
        JFrame lookAtMe = new JFrame("DataSet Grapher");
        lookAtMe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lookAtMe.setLayout(new BorderLayout());
        DataSetGrapher m = new DataSetGrapher(new DataSet());
        lookAtMe.add("Center", m);
        lookAtMe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        lookAtMe.setVisible(true);
    }
}
