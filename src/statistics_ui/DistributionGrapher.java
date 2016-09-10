
package statistics_ui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import javax.swing.JFrame;
import statistics_distributions.Distribution;
import statistics_distributions.StudentT;

/**
 *
 * @author alyacarina
 */
public class DistributionGrapher extends Panel {
    
    private Distribution distribution;
    private Canvas graph;
    private static final double PADDING = 0.05;
    
    public DistributionGrapher(Distribution distribution){
        super();
        this.distribution = distribution;
        initialize();
    }
    
    private void initialize(){
        this.setBackground(Color.BLACK);
        this.setLayout(new BorderLayout());
        graph = new Canvas() {
            @Override
            public void paint(Graphics g){
                Graphics2D g2d = (Graphics2D)g;
                g2d.setColor(new Color((int) (150 * Math.random()+105),
                                       (int) (150 * Math.random()+105),
                                       (int) (150 * Math.random()+105)));
                
                int x_min = (int) (getWidth()*PADDING);
                int x_max = getWidth() - x_min;
                int y_min = (int) (getHeight()*PADDING);
                int height = (int) (getHeight()*(1-2*PADDING));
                
                int mean_loc = getWidth()/2;
                
                int x = x_min;
                double val = distribution.getMean()-distribution.getDX()*(mean_loc-x_min);
                int y = (int) (distribution.f(val)*height) + y_min;
                while(x<=x_max){
                    val+=distribution.getDX();
                    int x_next = x+1;
                    int y_next = (int) (distribution.f(val)*height) + y_min;
                    
                    g2d.drawLine(x, getHeight() - y, x_next, getHeight() - y_next);
                    
                    x = x_next;
                    y = y_next;
                }
            }
        };
        this.add("Center", graph);
    }
    
    public final void setDistribution(Distribution distribution){
        this.distribution = distribution;
        repaint();
    }
    
    public static void main(String[] args){
        JFrame lookAtMe = new JFrame("DataSet Grapher");
        lookAtMe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lookAtMe.setLayout(new BorderLayout());
        DistributionGrapher m = new DistributionGrapher(new StudentT(100));
        lookAtMe.add("Center", m);
        lookAtMe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        lookAtMe.setVisible(true);
    }
}
