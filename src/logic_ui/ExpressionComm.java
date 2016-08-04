package logic_ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import logic.AdequateConnective;
import logic.Formula;
import logic.LogicManager;
import logic.Prop;
import logic.Implication;
import ui_general_utils.EnterKeyListener;

/**
 *
 * @author alyacarina
 */
public class ExpressionComm extends Panel {
    public static final char DARROW = '|'; // Symbol for adequate connective
    public static final char OPENB = '(';
    public static final char CLOSEB = ')';

    public static final String END = "end";
    public static final String EQUIVALENT = "=?";
    public static final String REPLACE = "&";
    public static final String ENTAILS = "=>";
    
    public static final String VALID = "Formula is a tautology.";
    public static final String SATISFIABLE = "Formula is satisfiable.";
    public static final String UNSATISFIABLE = "Formula is unsatisfiable.";

    public static final String INVALID = "Invalid formula syntax.";
    public static final String IOEX = "IOException. Exiting.";
    public static final String NULLEX = "Bad Syntax. Exiting.";
    
    private static final Color INPUT_BG = new Color(100, 0, 10);
    private static final Color OUTPUT_BG = new Color(150, 0, 15);
    private static final Color FG = new Color(255, 255, 255);
    
    private TextArea inputTaker, logger;
    private final LogicManager boss;
    
    private ArrayList<Prop> bin;
    
    public ExpressionComm(LogicManager boss, LogicGrapher logiGraph){
        bin = new ArrayList<>();
        this.boss = boss;
        initialize(logiGraph);
    }
    
    private void initialize(LogicGrapher logiGraph){
        setLayout(new BorderLayout());

        inputTaker = new TextArea("", 1, 25, TextArea.SCROLLBARS_NONE);
        inputTaker.setEditable(true);
        inputTaker.setBackground(INPUT_BG);
        inputTaker.setForeground(FG);
        inputTaker.addKeyListener(new EnterKeyListener() {
            @Override
            public void doThis(KeyEvent e) {
                String command = inputTaker.getText();
                while(command.contains("\n")){
                    command = command.substring(0, command.indexOf("\n"))
                            + command.substring(command.indexOf("\n")+1);
                }
                
                String returned;
                returned = respond(command);
                if(!returned.equals("")){
                    logger.append(returned + "\n");
                }
                
                inputTaker.setText("");
            }
        });
        add("North", inputTaker);

        logger = new TextArea("", 5, 25, TextArea.SCROLLBARS_VERTICAL_ONLY);
        logger.setEditable(false);
        logger.setBackground(OUTPUT_BG);
        logger.setForeground(FG);
        add("Center", logger);
    }

    // To reset variables
    public void clear() {
        bin = new ArrayList<>();
    }

    // Interpreter for infix notation expressions containing only the adequate connective
    public Formula getFormulaFromLine(String line) {
        Pattern var = Pattern.compile("^[a-z]+$");
        Matcher m_var = var.matcher(line);
        if (m_var.matches()) {

            for (Prop p : bin) {
                if (p.getName().equals(line)) {
                    return new Formula(p);
                }
            }

            Prop p = new Prop(line);
            bin.add(p);
            return new Formula(p);
        }

        Pattern brace_split = Pattern.compile("^\\((.+)\\)$");
        Pattern brace_split2 = Pattern.compile("^(\\(.+\\)|[a-z]+)\\|(\\(.+\\)|[a-z]+)$");
        Pattern brace_split3 = Pattern.compile("^\\((\\(.+\\)|[a-z]+)\\|(\\(.+\\)|[a-z]+)\\)$");
        Matcher m_brace_split = brace_split.matcher(line);
        Matcher m_brace_split2 = brace_split2.matcher(line);
        Matcher m_brace_split3 = brace_split3.matcher(line);
        if ((m_brace_split.matches() && !m_brace_split2.matches())
                || m_brace_split3.matches()) {
            return getFormulaFromLine(line.substring(1, line.length() - 1));
        } else if (m_brace_split2.matches()) {
            int l = line.length() - 1;
            int split = 0;

            int closed = 0;
            int open = 0;
            while (split <= l) {
                char c = line.charAt(split);
                if (c == ')') {
                    closed++;
                } else if (c == '(') {
                    open++;
                } else if (c == '|' && open == closed) {
                    break;
                }
                split++;
            }

            String f1 = line.substring(0, split);
            String f2 = line.substring(split + 1);

            return new Formula(
                    new Formula[]{getFormulaFromLine(f1),
                        getFormulaFromLine(f2)},
                    new AdequateConnective());
        }

        return null;
    }
    
    // Outputs truth table
    private String stringifyTruthTable(Formula phi){
        String output = "";
        
        boolean[][] truthTable = phi.generateTruthTable();
        String[] names = phi.getVariableNamesInOrder();
        int col_width = 10;
        for (String name : names) {
            output += name;
            for (int j = name.length(); j <= col_width; j++) {
                output += " ";
            }
        }
        output += phi;
        output += "\n";
        for (boolean[] b : truthTable) {
            for (int i = 0; i < b.length; i++) {
                output += b[i] + "";
                for (int j = (b[i] + "").length(); j < col_width; j++) {
                    output += " ";
                }
            }
            output += "\n";
        }
        
        if(phi.isValid()){
            output+=VALID;
        } else if(phi.isSatisfiable()){
            output+=SATISFIABLE;
        } else {
            output+=UNSATISFIABLE;
        }
        
        return output;
    }

    // String output of evaluating a line of input
    // User MUST use infix notation
    public String respond(String line) {
        int index;
        if ((index = line.indexOf(EQUIVALENT)) != -1) {
            String s1 = line.substring(0, index);
            s1 = s1.trim();
            String s2 = line.substring(index + EQUIVALENT.length());
            s2 = s2.trim();

            Formula f1 = getFormulaFromLine(s1);
            Formula f2 = getFormulaFromLine(s2);
            return Formula.equivalent(f1, f2) + "\n";
        }
        
    if ((index = line.indexOf(ENTAILS)) != -1) {
            String s1 = line.substring(0, index);
            s1 = s1.trim();
            String s2 = line.substring(index + ENTAILS.length());
            s2 = s2.trim();

            Formula f1 = getFormulaFromLine(s1);
            Formula f2 = getFormulaFromLine(s2);
      
            return stringifyTruthTable(new Implication(f1, f2)) + "\n";
        }

        int index2 = 0;
        if(((index = line.indexOf(REPLACE))>0)
                && (index2 = line.substring(index+1).indexOf(REPLACE))>0){
            Formula phi1 = getFormulaFromLine(line.substring(0, index));
            Formula phi2 = getFormulaFromLine(line.substring(index+1, index+index2+1));
            Formula phi3 = getFormulaFromLine(line.substring(index+index2+2));
            
            return phi1.replaceInputBy(phi2, phi3).toString();
        }
        
        Formula phi = getFormulaFromLine(line);

        if (phi == null) {
            return INVALID + "\n";
        }
        
        boss.addLogicNode(phi);

        String output = stringifyTruthTable(phi);
        
        output+="\n";
        
        return output;
    }

    public static void main(String[] args) {
        LogicManager lm = new LogicManager();
        ExpressionComm x = new ExpressionComm(lm, new LogicGrapher(lm));

        try (BufferedReader systemin
                = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = systemin.readLine()) != null
                    && !(line.equals(END))) {
                System.out.println(x.respond(line));
            }
        } catch (IOException e) {
            System.out.println(IOEX);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            System.out.println(NULLEX);
        }

    }
}
