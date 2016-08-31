
package statistics_ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import statistics.Distribution;

/**
 *
 * @author alyacarina
 */
public class DistributionGrapher extends Panel {
    
    private Distribution distribution;
    private Canvas graph;
    
    public DistributionGrapher(Distribution distribution){
        super();
        this.distribution = distribution;
        initialize();
    }
    
    private void initialize(){
        this.setBackground(Color.BLACK);
        graph = new Canvas() {
            @Override
            public void paint(Graphics g){
                Graphics2D g2d = (Graphics2D)g;
                g2d.setColor(new Color((int) (150 * Math.random()+105),
                                       (int) (150 * Math.random()+105),
                                       (int) (150 * Math.random()+105)));
                
                int x_min = getWidth()/10;
                int x_max = getWidth() - x_min;
                int y_min = getHeight()/10;
                int height = getHeight()*8/10;
                
                int mean_loc = getWidth()/2;
                
                // graph left of mean
                int y;
                int x = mean_loc;
                double val = distribution.getMean();
                while(x>=x_min){
                    y = (int) (distribution.f(val)*height) + y_min;
                    
                    val-=distribution.getDx();
                    x--;
                }
                
                // graph right of mean
                x = mean_loc+1;
                val = distribution.getMean()+distribution.getDx();
                while(x<=x_max){
                    y = (int) (distribution.f(val)*height) + y_min;
                    
                    val+=distribution.getDx();
                    x++;
                }
            }
        };
    }
    
    public final void setDistribution(Distribution distribution){
        this.distribution = distribution;
    }
    
}
