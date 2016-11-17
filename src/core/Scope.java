package core;

import graphs.DistributionManager;
import graphs.DistributionNode;
import graphs.MemoryNode;
import java.util.ArrayList;
import statistics_distributions.GraphDistribution;

/**
 *
 * @author alyacarina
 */
public class Scope {

    private DistributionNode locale, graphSource, bestNode;
    private boolean flag;
    private double max;
    

    private void initialize(DistributionNode graphSource, DistributionNode locale) {
        if (graphSource == null) {
            throw new NullPointerException("Root of scope must not be null.");
        }
        if (!(graphSource.getDistribution() instanceof GraphDistribution)) {
            graphSource.setDistribution(graphSource.getDistribution().toGraphDistribution());
        }
        this.graphSource = graphSource;
        this.locale = locale;
        this.flag = false;
    }

    public Scope(DistributionNode graphSource, DistributionNode locale) {
        initialize(graphSource, locale);
    }

    public Scope(DistributionNode graphSource) {
        initialize(graphSource, null);
    }

    public Scope(Scope old) {
        flag = old.locale == null;
        initialize(old.graphSource, old.locale);
    }

    static Scope clone(Scope get) {
        return new Scope(get.graphSource, get.locale);
    }

    public DistributionManager getGraphScope() {
        return ((GraphDistribution)graphSource.getDistribution()).getGraph();
    }
    
    public DistributionNode getGraphSource(){
        return graphSource;
    }

    public DistributionNode getLocale() {
        return locale;
    }

    public void setLocale(DistributionNode locale) {
        this.locale = locale;
    }

    private void findImpl(DistributionNode node,
            double d,
            ArrayList<Integer> visited) {
        visited.add(node.getId());
        if (node.getDistribution() != null) {
            double fx = node.getDistribution().f(d);
            if (fx >= max) {
                max = fx;
                bestNode = node;
                // definite
                if(max == 1){
                    return;
                }
            }
        }
        for (MemoryNode m : node.getNeighbors()) {
            if (visited.contains(m.getId())) {
                continue;
            }
            findImpl((DistributionNode) m, d, visited);
        }
    }

    // same as findImpl, only seeks best in tier immediately beneath current root
    private void findImplBroad(DistributionNode node,
            double d,
            ArrayList<Integer> visited) {
        for (MemoryNode m : node.getNeighbors()) {
            if (visited.contains(m.getId())) {
                continue;
            }
            DistributionNode dn = (DistributionNode) m;
            if (dn.getDistribution() instanceof GraphDistribution) {
                findImpl((DistributionNode) ((GraphDistribution) dn.getDistribution()).getGraph().root,
                        d,
                        visited);
                // definite
                if(max == 1){
                    break;
                }
            }
        }
    }

    public DistributionNode find(double d) {
        max = 0;
        DistributionNode root = (DistributionNode) 
                ((GraphDistribution)graphSource.getDistribution()).getGraph().root;
        bestNode = (DistributionNode) root;
        if (!isBroad()) {
            findImpl((DistributionNode) root, d, new ArrayList());
        } else {
            findImplBroad((DistributionNode) root, d, new ArrayList());
        }
        if (max == 0) {
            return null;
        }
        return bestNode;
    }

    public final boolean isBroad() { // check if scope is 'broad', i.e. should span entire subgraph
        return flag;
    }

}
