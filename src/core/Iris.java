package core;

import graphs.DistributionNode;
import java.io.Serializable;
import java.util.ArrayList;
import statistics_analysis.Composer;
import statistics_distributions.Distribution;
import statistics_distributions.GraphDistribution;

/**
 *
 * @author alyacarina
 */
public class Iris implements Serializable {

    private final DistributionNode only;
    private ArrayList<Scope> tracker;
    private Composer bach;

    public Iris() {
        only = new DistributionNode(new GraphDistribution(), new ArrayList(), 0);
        tracker = new ArrayList<>();
        tracker.add(new Scope(only));
        bach = new Composer(this);
    }

    // Convert string input to numbers- BASIC: chars to ints.
    public void placeInput(String input) {
        double[] vals = new double[input.length()];
        for (int i = 0; i < vals.length; i++) {
            vals[i] = input.charAt(i);
        }
        placeInput(vals);
    }

    private void placeInput(double[] input) {
        for (int i = 0; i < input.length; i++) {
            Scope current = Scope.clone(tracker.get(i));
            current.setLocale(current.find(input[i]));
            tracker.add(new Scope(current));
        }
        tracker.remove(0);

        // Identify discrepancies, substitute them with the best distribution 
        //  for the job, reinforce existing along path, and insert new nodes
        //  where they do not currently exist
        for (int i = 0; i < input.length; i++) {
            Scope level = tracker.get(i);
            level.getGraphSource().addConfirmedDatum(input[i]);
            level.getGraphSource().feed();
            
            if (level.getLocale() != null) {
                // reinforce
                level.getLocale().feed();
                level.getLocale().addConfirmedDatum(input[i]);
            } else {
                // create node and place it
                Distribution temp = bach.generateDistribution(input[i]);
                
                if (i > 0 && tracker.get(i - 1).getLocale() != null) {
                    DistributionNode previousNode = tracker.get(i - 1).getLocale();
                    Distribution previousDistribution = previousNode.getDistribution();

                    if (previousDistribution instanceof GraphDistribution) {
                        ((GraphDistribution) previousDistribution)
                                .addDistributionNode(temp);
                    } else {
                        previousDistribution = previousDistribution.toGraphDistribution();
                        previousNode.setDistribution(previousDistribution);
                        ((GraphDistribution) previousDistribution)
                                .addDistributionNode(temp);
                    }
                    ((GraphDistribution)previousDistribution).getNewest().addConfirmedDatum(input[i]);
                } else {
                    level.getGraphScope().addDistributionNode(temp);
                    level.setLocale((DistributionNode) level.getGraphScope().getNewest());
                    level.getLocale().addConfirmedDatum(input[i]);
                }
            }
            
        }

        //tracker.get(tracker.size()-1);
        // TODO: backtracking, from last locale
        // TEMP: start over from origin
        tracker = new ArrayList();
        tracker.add(new Scope(only));
    }

    public GraphDistribution getMemory() {
        return (GraphDistribution)only.getDistribution();
    }

    public void sleep() {
        ((GraphDistribution)only.getDistribution()).goodNight();
    }
}
