
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
    private final ContinuousDistribution distribution;
    private Canvas face;
    private Label details;
    private final int x_base, y_base, padding;
    
    public ContinuousDistributionGrapher(ContinuousDistribution distribution) {
        x_base = 30;
        y_base = 30;
        padding = 15;
        
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
                distribution.setMean(distribution.getCenter());
                
                int width = this.getWidth() - x_base;
                int height = this.getHeight() - y_base;
                double wf = width/(distribution.getUpperLimit()-distribution.getLowerLimit());
                double hf = height;
                
                g2d.setColor(Color.black);
                double previous = distribution.f(distribution.getLowerLimit());
                for(double x = distribution.getLowerLimit() + distribution.getDx(); 
                        x<=distribution.getUpperLimit(); 
                        x+=distribution.getDx()){
                    double next = distribution.f(x);
                    double prex = x - distribution.getDx();
                    
                    g2d.drawLine(width/2 + (int)(wf*prex), 
                            height - (int)(hf*previous),
                            width/2 + (int)(wf*x), 
                            height - (int)(hf*next));
                    previous = next;
                }
                
                g2d.setColor(Color.red);
                int meanx = width/2 - (int)(wf*mean_temp);
                g2d.drawString(mean_temp+"", width/2-padding, height+padding);
                g2d.drawLine(meanx, height, meanx, y_base);
                g2d.drawLine(x_base, height, width, height);
                
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
