package graphs;

import java.util.ArrayList;

/**
 *
 * @author alyacarina
 */
public class ConceptGraph extends MemoryManager {

    public ArrayList<Integer> free_ids;

    public ConceptGraph() {
        root = new ConceptNode(new ArrayList<>(), 0, new Concept("kandrakar"));
        number_of_nodes = 0;
        free_ids = new ArrayList<>();
        root.mouse_x = 300;
        root.mouse_y = 300;
    }

    public static ConceptGraph generateFromStrings(ArrayList<ArrayList<String>> string_lists) {
        //first string is the root node, always, so can (mostly) ignore data
        ConceptGraph cg = new ConceptGraph();
        ConceptNode x;
        for (int i = 1; i < string_lists.size(); i++) {
            int num_nodes = 1;
            for (int j = 0; j < string_lists.get(i).get(0).length(); j++) {
                if (string_lists.get(i).get(0).charAt(j) == '!') {
                    num_nodes++;
                }
            }

            String[] str_deets = string_lists.get(i).get(0).split("\\|");
            int x_id = Integer.parseInt(str_deets[0]);
            x = new ConceptNode(new ArrayList<>(),
                    x_id,
                    Concept.generateFromStrings(string_lists.get(i), num_nodes));
            x.mouse_x = 50 + (int) (300 * Math.random());
            x.mouse_y = 50 + (int) (300 * Math.random());

            String[] neighbor_ids = string_lists.get(i).get(1 + num_nodes).split(",");
            for (int j = 0; j < neighbor_ids.length; j++) {
                try {
                    if (!("".equals(neighbor_ids[j]))) {
                        try {
                            int id = Integer.parseInt(neighbor_ids[j]);
                            ConceptNode cn = (ConceptNode) cg.root.seek(id,
                                    new ArrayList<>());
                            if (cn != null) {
                                if (id > cg.number_of_nodes) {
                                    cg.number_of_nodes = id;
                                }
                                x.addNeighbor(cn);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return cg;
    }

    @Override
    public int getNextId() {
        if (free_ids.isEmpty()) {
            return super.getNextId();
        } else {
            int i = free_ids.get(0);
            free_ids.remove(new Integer(i));
            return i;
        }
    }

    @Override
    public MemoryNode removeMemoryNode(int id) {
        free_ids.add(id);
        return super.removeMemoryNode(id);
    }

    @Override
    public void generateNewRandom(int number_of_nodes) {
        // do nothing- DEPRECATED
    }

    @Override
    public void addMemoryNode(int destination_id) {
        // do nothing- DEPRECATED
    }

    @Override
    public void addMemoryNode(MemoryNode node, int destination_id) {
        // do nothing: deprecated
    }

    @Override
    public void addMemoryNodes(ArrayList<MemoryNode> memories, int destination_id) {
        // do nothing: deprecated
    }

    // first_is_core determines which node is the new root
    public void mergeConceptNodes(int concept_id_1,
            int concept_id_2,
            boolean first_is_core) {

        ConceptNode m1 = (ConceptNode) root.seek(concept_id_1, new ArrayList<>());
        ConceptNode m2 = (ConceptNode) root.seek(concept_id_2, new ArrayList<>());

        if (m1 == null || m2 == null || concept_id_1 == 0 || concept_id_2 == 0) {
            throw new NullPointerException("Invalid node id!");
        }

        ArrayList<MemoryNode> future_neighbors = new ArrayList<>();
        future_neighbors.addAll(m1.getNeighbors());
        future_neighbors.addAll(m2.getNeighbors());

        // WARNING: if either is NOT a concept-containing node, 
        //    you WILL get a ClassCastException.
        ConceptNode merged = new ConceptNode(new ArrayList<>(), getNextId(),
                Concept.mergeConcepts(m1.getConcept(),
                        m2.getConcept(),
                        first_is_core));
        merged.mouse_x = 100 + (int) (300 * Math.random());
        merged.mouse_y = 100 + (int) (300 * Math.random());
        for (MemoryNode neighbor : future_neighbors) {
            addConceptNode(merged, neighbor.getId());
        }

        // this is done after to preserve links until they are replicated
        //    in the new, merged node
        removeMemoryNode(concept_id_1);
        removeMemoryNode(concept_id_2);

        if (root.seek(merged.getId(), new ArrayList<>()) == null) {
            addConceptNode(merged, 0);
        }
        merged.setId(getNextId());
    }

    @Override
    public void addStringNode(String s, int destination_id) {
        ConceptNode node = new ConceptNode(new ArrayList<>(), getNextId(), new Concept(s));
        node.mouse_x = 50 + (int) (300 * Math.random());
        node.mouse_y = 50 + (int) (300 * Math.random());
        addConceptNode(node, destination_id);
    }

    @Override
    public void addStringNodes(String[] s, int destination_id) {
        ArrayList<ConceptNode> memories = new ArrayList<>();
        for (String x : s) {
            ConceptNode temp = new ConceptNode(new ArrayList<>(), getNextId(), new Concept(x));
            temp.mouse_x = 50 + (int) (300 * Math.random());
            temp.mouse_y = 50 + (int) (300 * Math.random());
            memories.add(temp);
        }
        addConceptNodes(memories, destination_id);
    }

    // learns new unformatted strings in an array of strings
    @Override
    public void learnWords(String[] words) {
        int i = 0;
        int last_index = 0;

        while (i < words.length) {
            ConceptNode existing
                    = ((ConceptNode) root).seekByConcept(new Concept(words[i]),
                            new ArrayList<>());
            if (existing == null) {
                addStringNode(words[i], last_index);
                last_index = number_of_nodes;
            } else {
                if (last_index > 0) {
                    knit(existing.getId(), last_index);
                }
                last_index = existing.getId();
                existing.feed();
            }
            i++;
        }
    }

    public void addConceptNode(ConceptNode node, int destination_id) {
        if (destination_id < 0 || destination_id > number_of_nodes) {
            //System.out.println("Not a valid destination_id!");
            return;
        }

        ConceptNode destination = (ConceptNode) root.seek(destination_id, new ArrayList<>());

        if (destination == null) {
            throw new IllegalStateException("No node with id: " + destination_id
                    + " is accessible!");
        } else {
            destination.addNeighbor(node);
        }
    }

    public void addConceptNodes(ArrayList<ConceptNode> memories, int destination_id) {
        if (destination_id < 0 || destination_id > number_of_nodes) {
            //System.out.println("Not a valid destination_id!");
            return;
        }

        ConceptNode destination = (ConceptNode) root.seek(destination_id, new ArrayList<>());

        if (destination == null) {
            throw new IllegalStateException("No node with id: " + destination_id
                    + " is accessible!");
        } else {
            ArrayList<MemoryNode> mxm = new ArrayList<>();
            mxm.addAll(memories);
            destination.addNeighbors(mxm);
        }
    }

    // find associations in graph
    public void associateLite(MemoryNode current, ArrayList<Integer> to_ignore) {
        ArrayList<String> bin = new ArrayList<>();
        for (int i = 1; i < current.getNeighbors().size(); i++) {
            ConceptNode x = (ConceptNode) current.getNeighbors().get(i);
            if (to_ignore.contains(x.getId())) {
                continue;
            }
            for (int j = 0; j < i; j++) {
                ConceptNode y = (ConceptNode) current.getNeighbors().get(j);
                if (y.compareNeighbors(x) && x.compareNeighbors(y)) {
                    String sx = x.getConcept().toString();
                    if (!bin.contains(sx)) {
                        bin.add(sx);
                    }
                    String sy = y.getConcept().toString();
                    if (!bin.contains(sy)) {
                        bin.add(sy);
                    }
                }
            }
            to_ignore.add(x.getId());
            associateLite(x, to_ignore);
        }
        if (bin.size() > 0) {
            System.out.println(bin);
        }
    }

    // find associations in graph
    public ArrayList<ArrayList<ConceptNode>> associateHeavy(MemoryNode current,
            ArrayList<Integer> to_ignore) {
        ArrayList<ArrayList<ConceptNode>> bins = new ArrayList<>();
        ArrayList<ConceptNode> bin = new ArrayList<>();
        for (int i = 1; i < current.getNeighbors().size(); i++) {
            ConceptNode x = (ConceptNode) current.getNeighbors().get(i);
            if (to_ignore.contains(x.getId())) {
                continue;
            }
            for (int j = 0; j < i; j++) {
                ConceptNode y = (ConceptNode) current.getNeighbors().get(j);
                if (y.compareNeighbors(x) && x.compareNeighbors(y)) {
                    if (!bin.contains(x)) {
                        bin.add(x);
                    }
                    if (!bin.contains(y)) {
                        bin.add(y);
                    }
                }
            }
            to_ignore.add(x.getId());
            bins.addAll(associateHeavy(x, to_ignore));
        }
        if (bin.size() > 0) {
            bins.add(bin);
        }
        return bins;
    }

    public boolean[] isPartitioned(ArrayList<ArrayList<MemoryNode>> bins) {
        int s = bins.size();
        boolean[] partitioned = new boolean[s];

        for (int i = 0; i < s; i++) {
            partitioned[i] = true;
            ArrayList<MemoryNode> x = bins.get(i);
            for (MemoryNode mn : x) {
                partitioned[i] = unneighborly(mn, x);
            }
        }

        return partitioned;
    }

    public boolean graphIsPartitioned(boolean[] partitioned) {
        for (Boolean b : partitioned) {
            if (b == false) {
                return false;
            }
        }
        return true;
    }

    public void partition(MemoryNode current,
            ArrayList<Integer> to_ignore,
            ArrayList<ArrayList<MemoryNode>> bins) {
        if (!to_ignore.contains(current.getId())) {
            if (current.getId() != 0) { //ignore root
                to_ignore.add(current.getId());

                ArrayList<MemoryNode> temp = null; // trying to cheat iterators
                boolean esc;
                for (ArrayList<MemoryNode> part : bins) {
                    esc = true;
                    for (MemoryNode cn : current.getNeighbors()) {
                        if (part.contains(cn)) {
                            esc = false;
                            break;
                        } else if(!unneighborly(cn, part)){
                            esc = false;
                            break;
                        }
                    }

                    if (esc) {
                        temp = part;
                        break;
                    }
                }

                if (temp == null) {
                    temp = new ArrayList<>();
                } else {
                    bins.remove(temp);
                }

                temp.add(current);
                bins.add(temp);
            }

            for (MemoryNode mn : current.getNeighbors()) {
                partition(mn, to_ignore, bins);
            }
        }
    }

    private boolean unneighborly(MemoryNode mn, ArrayList<MemoryNode> x) {
        for (MemoryNode neighbor : mn.getNeighbors()) {
            if (x.contains(neighbor)) {
                return false;
            }
        }
        return true;
    }

    public void rePartition(ArrayList<ArrayList<MemoryNode>> bins) {
        ArrayList<ArrayList<MemoryNode>> nbins = new ArrayList<>();
        for (ArrayList<MemoryNode> x : bins) {
            ArrayList<MemoryNode> rePartition = new ArrayList<>();
            for (MemoryNode mn : x) {
                if (!unneighborly(mn, x)) {
                    rePartition.add(mn);
                }
            }

            for (MemoryNode mn : rePartition) {
                x.remove(mn);
                boolean nal = true;

                for (ArrayList<MemoryNode> b : bins) {
                    if (b != x && unneighborly(mn, b)) {
                        b.add(mn);
                        nal = false;
                        break;
                    }
                }

                if (nal) {
                    for (ArrayList<MemoryNode> b : nbins) {
                        if (b != x && unneighborly(mn, b)) {
                            b.add(mn);
                            nal = false;
                            break;
                        }
                    }
                }

                if (nal) {
                    ArrayList<MemoryNode> n = new ArrayList<>();
                    n.add(mn);
                    nbins.add(n);
                }
            }
        }

        bins.addAll(nbins);
    }

    public void partitionStart() {
        ArrayList<Integer> to_ignore = new ArrayList<>();
        ArrayList<ArrayList<MemoryNode>> bins = new ArrayList<>();
        partition((MemoryNode) this.root, to_ignore, bins);

        boolean[] truths = isPartitioned(bins);
        
        for(Boolean b: truths){
            System.out.println(b);
        }

        //while (!graphIsPartitioned(truths)) {
            for (ArrayList<MemoryNode> x : bins) {
                System.out.println(x);
                System.out.println();
            }
            //rePartition(bins);
            //truths = isPartitioned(bins);
        //}
    }
}
