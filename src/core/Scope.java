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

    private DistributionNode locale, bestNode;
    private DistributionManager boss;
    private boolean flag;
    private double max;

    private void initialize(DistributionManager boss, DistributionNode locale) {
        if (boss == null) {
            throw new NullPointerException("Root of scope must not be null.");
        }
        this.boss = boss;
        this.locale = locale;
        this.flag = false;
    }

    public Scope(DistributionManager boss, DistributionNode locale) {
        initialize(boss, locale);
    }

    public Scope(DistributionManager boss) {
        initialize(boss, null);
    }

    public Scope(Scope old) {
        if (old.locale == null) {
            initialize(old.boss, null);
            flag = true;
        } else {
            if (!(old.locale.getDistribution() instanceof GraphDistribution)) {
                // Input kept going, there is more depth to this distribution,
                //    than previously believed
                old.locale.setDistribution(old.locale.getDistribution().toGraphDistribution());
            }
            initialize(((GraphDistribution) old.locale.getDistribution())
                    .getGraph(), null);
        }
    }

    static Scope clone(Scope get) {
        return new Scope(get.boss, get.locale);
    }

    public DistributionManager getGraphScope() {
        return boss;
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
            if (fx > max) {
                max = fx;
                bestNode = node;
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
            }
        }
    }

    public DistributionNode find(double d) {
        max = 0;
        bestNode = (DistributionNode) boss.root;
        if (!isBroad()) {
            findImpl((DistributionNode) boss.root, d, new ArrayList());
        } else {
            findImplBroad((DistributionNode) boss.root, d, new ArrayList());
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
