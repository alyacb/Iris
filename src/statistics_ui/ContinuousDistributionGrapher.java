
package statistics_ui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.Panel;
import statistics.ContinuousDistribution;

/**
 *
 * @author alyacarina
 */
public class ContinuousDistributionGrapher extends Panel {
    private ContinuousDistribution distribution;
    private Canvas face;
    private Label details;
    
    
    public ContinuousDistributionGrapher(ContinuousDistribution distribution) {
        this.distribution = distribution;
        initialize();
    }
    
    private void initialize(){
        setLayout(new BorderLayout());
        
        face = new Canvas(){
            @Override
            public void paint(Graphics g){
                Graphics2D g2d = (Graphics2D)g;
                
                double mean_temp = distribution.getMean();
                distribution.setMean(0);
                
                int x_shift = 1200;
                int y_max = 600;
                double wf = x_shift/(distribution.getUpperLimit()-distribution.getLowerLimit());
                double hf = y_max;
                
                g2d.setColor(Color.black);
                double previous = distribution.f(distribution.getLowerLimit());
                for(double x = distribution.getLowerLimit() + distribution.getDx(); 
                        x<=distribution.getUpperLimit(); 
                        x+=distribution.getDx()){
                    double next = distribution.f(x);
                    double prex = x - distribution.getDx();
                    
                    g2d.drawLine(x_shift/2 + (int)(wf*prex), 
                            y_max - (int)(hf*previous),
                            x_shift/2 + (int)(wf*x), 
                            y_max - (int)(hf*next));
                    previous = next;
                }
                
                g2d.setColor(Color.red);
                int meanx = x_shift/2 - (int)(wf*mean_temp);
                g2d.drawString(mean_temp+"", x_shift/2-5, y_max+15);
                g2d.drawLine(meanx, y_max, meanx, 0);
                g2d.drawLine(0, y_max, x_shift*2, y_max);
                
                distribution.setMean(mean_temp);
            }
        };
        
        details = new Label();
        
        add("Center", face);
        add("South", details);
        refresh();
    } 

    void refresh() {
        face.repaint();
        details.setText(distribution.getName() + " mean: " + distribution.getMean() 
                                               + " standard deviation: " + distribution.getStandardDeviation());
    }
}
