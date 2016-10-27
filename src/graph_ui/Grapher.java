package graph_ui;

import graphs.MemoryManager;
import graphs.MemoryNode;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author alyacarina
 */
public class Grapher extends Panel {

    public MemoryManager nemo;
    public final static int NODE_RADIUS = 15;
    private final static int HALF_NODE_RADIUS = NODE_RADIUS / 2;
    private final String TITLE;
    protected MemoryNode selected;
    private ArrayList<MemoryNode> tempStore;
    private Panel nodeInfo, nodeUtil;
    private Canvas face;
    private boolean knitOrMove;

    // Constructor:
    public Grapher(MemoryManager boss) {
        nemo = boss;
        knitOrMove = false;
        selected = nemo.root;
        tempStore = null;

        TITLE = "Grapher";

        initialize();
    }

    // Add util bar optionally
    public Grapher(MemoryManager boss, boolean utils) {
        nemo = boss;
        knitOrMove = false;
        if (nemo != null) {
            selected = nemo.root;
        } else {
            selected = null;
        }
        tempStore = null;

        TITLE = "Grapher";

        initialize2();
    }

    // Add util bar optionally, change title
    public Grapher(MemoryManager boss, boolean utils, String title) {
        nemo = boss;
        knitOrMove = false;
        if (nemo != null) {
            selected = nemo.root;
        } else {
            selected = null;
        }
        tempStore = null;

        TITLE = title;

        initialize2();
    }

    public void setManager(MemoryManager next_boss){
       nemo = next_boss;
       refresh();
    }
    
    private void initialize() {
        this.setLayout(new BorderLayout());
        nodeInfo = initializeNodeInfoPanel();
        nodeUtil = initializeNodeUtilPanel();
        this.add("South", nodeInfo);
        this.add("North", nodeUtil);
        saveFace();
        this.add("North", new JLabel("<html><font color='darkgray'>" + TITLE
                + "</font></html>"));
        this.setBackground(Color.black);
    }

    private void initialize2() {
        this.setLayout(new BorderLayout());
        nodeInfo = initializeNodeInfoPanel();
        this.add("South", nodeInfo);
        this.setBackground(Color.black);
        saveFace();
    }

    private void saveFace() {
        face = new Canvas() {
            @Override
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                Thread artist = new Thread() {
                    private void paintNode(MemoryNode memory, ArrayList<Integer> to_ignore) {
                        int i = memory.getId();
                        if (to_ignore.contains(i)) {
                            return;
                        }
                        to_ignore.add(i);
                        if (i == selected.getId() && knitOrMove) {
                            g2d.setColor(Color.ORANGE);
                        } else if (i == selected.getId()) {
                            g2d.setColor(Color.CYAN);
                        } else if (knitOrMove) {
                            g2d.setColor(Color.MAGENTA);
                        } else {
                            g2d.setColor(Color.RED);
                        }

                        g2d.drawOval(memory.mouse_x, memory.mouse_y,
                                NODE_RADIUS, NODE_RADIUS);

                        g2d.setColor(Color.LIGHT_GRAY);

                        if (memory.getData() != null) {
                            g2d.drawString(memory.getId() + ": "
                                    + memory.getData().toString(),
                                    memory.mouse_x, memory.mouse_y);
                        } else {
                            g2d.drawString(memory.getId() + "",
                                    memory.mouse_x, memory.mouse_y);
                        }

                        if (memory.edge_color == null) {
                            memory.edge_color
                                    = new Color((int) (255 * Math.random()),
                                            (int) (255 * Math.random()),
                                            (int) (255 * Math.random()));
                        }
                        g2d.setColor(memory.edge_color);
                        for (MemoryNode neighbor : memory.getNeighbors()) {
                            if (neighbor.getId() > memory.getId()) {
                                g2d.drawLine(memory.mouse_x + HALF_NODE_RADIUS,
                                        memory.mouse_y + HALF_NODE_RADIUS,
                                        neighbor.mouse_x + HALF_NODE_RADIUS,
                                        neighbor.mouse_y + HALF_NODE_RADIUS);
                            }
                        }

                        for (MemoryNode x : memory.getNeighbors()) {
                            paintNode(x, to_ignore);
                        }
                    }

                    @Override
                    public void start() {
                        if (nemo == null) {
                            return;
                        } else {
                            paintNode(nemo.root, new ArrayList<>());
                        }
                    }
                };
                artist.start();
            }
        };

        face.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouse_x = e.getX();
                int mouse_y = e.getY();

                MemoryNode temp = null;
                if ((temp = nemo.getNodeAtPosition(mouse_x - HALF_NODE_RADIUS,
                        mouse_y - HALF_NODE_RADIUS)) != null) {

                    if (temp.getId() == selected.getId()) {
                        knitOrMove = !knitOrMove;
                    } else if (knitOrMove) {
                        if (selected.getNodeById(temp.getId()) == null) {
                            knitNodes(temp);
                        } else {
                            selected = temp;
                        }
                    } else {
                        selected = temp;
                    }

                } else if (nemo.root.seek(
                        selected.getId(), new ArrayList<>()) != null) {
                    if (knitOrMove) {
                        moveNode(mouse_x, mouse_y);
                    } else {
                        addNode(mouse_x, mouse_y);
                    }
                }

                overrideableAction(); // to add more functionality to graph, as needed

                face.repaint();
                resetNodeInfoPanel("");
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

        });
        this.add("Center", face);
    }

    private void addNodeImpl(int mouse_x, int mouse_y) {
        MemoryNode node = getNewMemoryNode();
        nemo.addMemoryNode(node, selected.getId());
        node.mouse_x = mouse_x;
        node.mouse_y = mouse_y;
    }
    
    public void knitNodeImpl(MemoryNode temp) {
        nemo.knit(selected.getId(), temp.getId());
    }
    
    public void moveNodeImpl(int mouse_x, int mouse_y) {
        selected.mouse_x = mouse_x;
        selected.mouse_y = mouse_y;
    }
    
    // Overrideable methods
    public void addNode(int mouse_x, int mouse_y) {
        addNodeImpl(mouse_x, mouse_y);
    }

    public void knitNodes(MemoryNode temp) {
        knitNodeImpl(temp);
    }

    public void moveNode(int mouse_x, int mouse_y) {
        moveNodeImpl(mouse_x, mouse_y);
    }

    public void overrideableAction() {
        //nothing
    }

    // other stuff...
    private Panel initializeNodeInfoPanel() {
        Panel subNodeInfo = new Panel();
        if (selected != null) {
            subNodeInfo.add(new JLabel("<html><font color='gray'>"
                    + selected.toString() + "</font></html>"));
        }
        return subNodeInfo;
    }

    private Panel initializeNodeUtilPanel() {
        Panel subNodeUtil = new Panel();

        Button remove = new Button("Remove");
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tempStore = (ArrayList<MemoryNode>) selected.getNeighbors().clone();
                tempStore.add(nemo.removeMemoryNode(selected.getId()));
                if (tempStore.contains(nemo.root)) {
                    tempStore.remove(nemo.root);
                }
                selected = nemo.root;

                face.repaint();
                resetNodeInfoPanel("");
            }

        });
        subNodeUtil.add(remove);

        Button clean = new Button("Clean");
        clean.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nemo.clean(tempStore);
                tempStore = null;
                selected = nemo.root;

                resetNodeInfoPanel("");
                face.repaint();
            }

        });
        subNodeUtil.add(clean);

        Button reset = new Button("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nemo.removeAllMemoryNodes();
                tempStore = null;
                selected = nemo.root;

                resetNodeInfoPanel("");
                face.repaint();
            }

        });
        subNodeUtil.add(reset);

        Button randomize = new Button("Randomize");
        randomize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nemo.generateNewRandom((int) (Math.random() * 10));

                resetNodeInfoPanel("");
                face.repaint();
            }

        });
        subNodeUtil.add(randomize);

        Button lineup = new Button("Line Up");
        lineup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nemo.lineUpNodes();

                resetNodeInfoPanel("");
                face.repaint();
            }

        });
        subNodeUtil.add(lineup);

        Button sleep = new Button("Sleep");
        sleep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nemo.sleep();

                resetNodeInfoPanel("");
                face.repaint();
            }

        });
        subNodeUtil.add(sleep);

        return subNodeUtil;
    }

    private void displayNodeInfoPanelMessage(String message) {
        remove(nodeInfo);
        if (message.equals("")) {
            nodeInfo = initializeNodeInfoPanel();
        } else {
            nodeInfo = new Panel();
            nodeInfo.add(new JLabel("<html><font color='white'>" + message
                    + "</font></html>"));
        }
        add("South", nodeInfo);
        revalidate();
    }

    private void resetNodeInfoPanel(String message) {
        displayNodeInfoPanelMessage(this.selected.toString() + " " + message);
    }

    public void refresh() {
        face.repaint();
        resetNodeInfoPanel("");
    }

    public MemoryNode getSelected() {
        return selected;
    }

    public MemoryNode getNewMemoryNode() {
        return new MemoryNode(new ArrayList<>(), nemo.getNextId());
    }

    public static void main(String[] args) {
        JFrame lookAtMe = new JFrame("MemoryNode Grapher");
        lookAtMe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lookAtMe.setLayout(new BorderLayout());
        lookAtMe.add("Center", new Grapher(new MemoryManager()));
        lookAtMe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        lookAtMe.setVisible(true);
    }
}
