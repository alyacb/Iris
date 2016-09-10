package statistics_ui;

import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import data_sets.DataSet;
import statistics_analysis.PValueGenerator;
import statistics_distributions.ChiSquared;
import statistics_distributions.Normal;
import ui_general_utils.EnterKeyListener;

/**
 *
 * @author alyacarina
 */
public class DataSetEditor extends Panel {

    private DataSet ds;
    private double step_size;

    public DataSetEditor(DataSet ds) {
        this.ds = ds;
        step_size = 1;
        initialize();
    }

    // Allows containers/ subclasses to add extra refresh functionality
    public void onRefresh() {
    }

    private void initialize() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        DataSetGrapher graphs = new DataSetGrapher(ds) {
            @Override
            public void refresh(double step_size) {
                super.refresh(step_size);
                onRefresh();
            }
        };
        this.add(graphs);

        TextArea commandBox = new TextArea("", 2, 2, TextArea.SCROLLBARS_NONE);
        commandBox.addKeyListener(new EnterKeyListener() {
            @Override
            public void doThis(KeyEvent e) {
                String next = commandBox.getText();
                commandBox.setText("");
                next = next.trim();
                if (next.substring(1).isEmpty()) {
                    return;
                }

                int i = next.indexOf("file");
                if (i != -1) {
                    String file_name = "/Users/alyacarina/NetBeansProjects/Iris/src/statistics_ui/nums.txt";

                    try (BufferedReader in = new BufferedReader(new FileReader(file_name))) {
                        String nextLine;
                        while ((nextLine = in.readLine()) != null) {
                            while (nextLine.length() > 0) {
                                try {
                                    String sub = "";
                                    int j = 0;
                                    for (; j < nextLine.length(); j++) {
                                        if ("-0123456789".contains(nextLine.charAt(j) + "")) {
                                            int x = nextLine.indexOf(" ");
                                            if (x != -1) {
                                                sub = nextLine.substring(j, x);
                                            } else {
                                                sub = nextLine.substring(j);
                                            }
                                            
                                            break;
                                        }
                                    }

                                    double d = Double.parseDouble(sub);
                                    ds.addDatum(d);
                                    nextLine = nextLine.substring(j + sub.length()).trim();

                                } catch (Exception eee) {
                                    break;
                                }
                            }
                        }
                    } catch (IOException ioe) {
                        System.out.println(ioe.getMessage());
                    }

                    graphs.refresh(step_size);
                    return;
                }
                
                i = next.indexOf("eval");
                if(i!=-1){
                    PValueGenerator x = new PValueGenerator(ds);
                    Normal n = 
                            new Normal(ds.getMean(),
                                                   ds.getStandardDeviation());
                    System.out.println(x.getPValue(n, step_size));
                    ChiSquared csd = 
                            new ChiSquared(1);
                    System.out.println(x.getPValue(csd, step_size));
                    System.out.println();
                    return;
                }

                double d;
                try {
                    d = Double.parseDouble(next.substring(1));
                } catch (Exception ep) {
                    return;
                }

                if (next.contains("a")) {
                    ds.addDatum(d);
                } else if (next.contains("r")) {
                    ds.removeDatum(d);
                } else if (next.contains("s")) {
                    step_size = d;
                } else {
                    return;
                }

                graphs.refresh(step_size);
            }
        });
        this.add(commandBox);
    }

    public static void main(String[] args) {
        JFrame lookAtMe = new JFrame("DataSet Editor");
        lookAtMe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lookAtMe.setLayout(new BorderLayout());
        lookAtMe.add("Center", new DataSetEditor(new DataSet()));
        lookAtMe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        lookAtMe.setVisible(true);
    }

}
