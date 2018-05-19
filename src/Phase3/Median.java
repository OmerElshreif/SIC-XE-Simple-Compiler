/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Phase3;

import Phase2.Tree;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author SAMSUNG PC
 */
public class Median {

    int i, j, start;
    ArrayList<String> list;
    Matcher m;
    Pattern p;
    String regex, s, type;
    CodeGenerator code;
    Tree tree;
    JFrame frame;
    DefaultMutableTreeNode node, temp, te;
    JPanel main;
    DefaultMutableTreeNode root, stats, statsRoot;
    boolean statement = false;
    JScrollPane panelPane;

    public Median() throws IOException {
        tree = new Tree();
        code = new CodeGenerator();
        frame = new JFrame("TREES");
        main = new JPanel();
        panelPane = new JScrollPane(main);
        panelPane.setVerticalScrollBar(new JScrollBar());
        frame.setContentPane(panelPane);

        frame.setSize(200, 200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void median(HashMap<Integer, ArrayList<String>> map) throws IOException {

        regex = "(\\d*)(\\^(\\w+))?";
        p = Pattern.compile(regex);

        for (i = 1; i <= map.size(); i++) {
            statement = false;
            //get list corresponding the line;
            list = map.get(i);
          //  System.out.println("line: " + list);
            s = list.get(0);
            m = p.matcher(s);
            if (m.matches()) {
            //    System.out.println("MATCH??");
            }
            s = m.group(1);
            s = s.replace("^", "");
            start = Integer.parseInt(s);
            //System.err.println("s: " + start);
            switch (start) {

                case 1: //Program 
                {

                    root = new DefaultMutableTreeNode("<prog>");
                    DefaultMutableTreeNode prog = new DefaultMutableTreeNode("PROGRAM");

                    root.add(prog);
                    s = list.get(1);
                    m = p.matcher(s);
                    if (m.matches()) {
                        //System.out.println("MATCH??");
                    }
                    s = m.group(3);
                    DefaultMutableTreeNode name = new DefaultMutableTreeNode("<ID>" + s);
                    DefaultMutableTreeNode progName = new DefaultMutableTreeNode("<name>");
                    root.add(progName);
                    progName.add(name);
                }

                break;
                case 2: //vars
                {
                    DefaultMutableTreeNode var = new DefaultMutableTreeNode("VAR");
                    DefaultMutableTreeNode dec = new DefaultMutableTreeNode("<dec-list>");
                    DefaultMutableTreeNode d = new DefaultMutableTreeNode("<dec>");
                    DefaultMutableTreeNode idlist = new DefaultMutableTreeNode("<id-list>");

                    DefaultMutableTreeNode t = new DefaultMutableTreeNode("<type>");
                    type = list.get(list.size() - 1);

                    t.add(new DefaultMutableTreeNode("INTEGER"));

                    root.add(var);
                    root.add(dec);
                    dec.add(d);
                    dec.add(t);
                    d.add(idlist);
                    i++;
                    list = map.get(i);
                    for (j = 0; j <= list.size() - 3; j = j + 2) {
                        type = list.get(j);
                        m = p.matcher(type);
                        if (m.matches()) {
                        }
                        type = m.group(3);
                        temp = new DefaultMutableTreeNode("<ID> " + type);
                        node = new DefaultMutableTreeNode("<id-list>");
                        idlist.add(node);
                        idlist.add(temp);
                        idlist = node;

                    }
                }
                //call call generator function (for vars definition)
                break;
                case 3: //Begin
                {
                    root.add(new DefaultMutableTreeNode("Begin"));
                    statsRoot = new DefaultMutableTreeNode("<stmt-list>");
                    stats = new DefaultMutableTreeNode("<stmt-list>");
                    temp = new DefaultMutableTreeNode("<stmt-list>");
                    stats.add(temp);

                    temp = new DefaultMutableTreeNode("<stmt>");
                    stats.add(temp);

                    root.add(statsRoot);

                   // System.out.println("STATS ROOT ADDED " + statsRoot);
                }
                break;
                case 5: //END.
                {
                    root.add(new DefaultMutableTreeNode("END."));
                    //System.out.println("Code Generated Successfully");
                    code.bw.close();
                    code.fw.close();
                }
                break;
                //case 6 (INTEGER) shouldn't be encountered in the beginning of a line
                case 7: //FOR LOOP
                {
                    node = tree.loop(list);
                    statement = true;
                    if (node == null)//error
                    {
                     //   System.out.println("ERROR! at line number :" + i);
                        return; //EXIT??
                    } else {
                        //send the tree to code generator
                        main.add(new JLabel("Line" + i), BorderLayout.PAGE_END);
                        main.add(new JPanel().add(new JTree(node)), BorderLayout.PAGE_END);

                        i++;
                        while (!map.get(i).get(0).equalsIgnoreCase("4")) {
                            if (map.get(i).get(0).equalsIgnoreCase("7")) //NESTED
                            {
                                while (!map.get(i).get(0).equalsIgnoreCase("4"))
                                {
                                     if (map.get(i).get(0).startsWith("22^")) //arithmetics
                            {
                                list=map.get(i);
                                list.remove(list.size() - 1); //remove ";"
                            node = tree.assignment(list);
                            statement = true;
                            if (node != null) {
                             //   System.out.println("Tree " + i);
                                main.add(new JLabel("Line" + i), BorderLayout.PAGE_END);
                                main.add(new JPanel().add(new JTree(node)), BorderLayout.PAGE_END);
                                main.updateUI();
                                //call code gen obvs 

                            } else {
                            //    //.out.println("ERROR! at line number :" + i);
                                return; //EXIT
                            }
                            }
                                    i++;
                                }
                            }
                            else if (map.get(i).get(0).startsWith("22^")) //arithmetics
                            {
                                list=map.get(i);
                                list.remove(list.size() - 1); //remove ";"
                            node = tree.assignment(list);
                            statement = true;
                            if (node != null) {
                                //.out.println("Tree " + i);
                                main.add(new JLabel("Line" + i), BorderLayout.PAGE_END);
                                main.add(new JPanel().add(new JTree(node)), BorderLayout.PAGE_END);
                                main.updateUI();
                                //call code gen obvs 

                            } else {
                                //.out.println("ERROR! at line number :" + i);
                                return; //EXIT
                            }
                            }
                            i++;
                       }
                    }

                }

                break;
                case 8://READ
                {

                    statement = true;
                    te = new DefaultMutableTreeNode("<read>");
                    temp = new DefaultMutableTreeNode("read");

                    DefaultMutableTreeNode idlist = new DefaultMutableTreeNode("<id-list>");
                    te.add(temp);
                    te.add(idlist);

                    for (j = 2; j <= list.size() - 3; j = j + 2) {
                        type = list.get(j);
                        m = p.matcher(type);
                        if (m.matches()) {
                        }
                        type = m.group(3);

                        temp = new DefaultMutableTreeNode("<ID> " + type);
                        node = new DefaultMutableTreeNode("<id-list>");
                        idlist.add(node);

                        idlist.add(temp);
                        idlist = node;

                    }
                    node = te;
                }
                //call code gen 
                break;

                case 9: //write
                {

                    statement = true;
                    te = new DefaultMutableTreeNode("<write>");
                    temp = new DefaultMutableTreeNode("WRITE");

                    DefaultMutableTreeNode idlist = new DefaultMutableTreeNode("<id-list>");
                    te.add(temp);
                    te.add(idlist);

                    for (j = 2; j <= list.size() - 3; j = j + 2) {
                        type = list.get(j);
                        m = p.matcher(type);
                        if (m.matches()) {
                        }
                        type = m.group(3);

                        temp = new DefaultMutableTreeNode("<ID> " + type);
                        node = new DefaultMutableTreeNode("<id-list>");
                        idlist.add(node);

                        idlist.add(temp);
                        idlist = node;

                    }
                    node = te;
                }
                //call code gen1
                break;
                case 22: //ARITHMETICS
                {
                    if (!list.get(1).equalsIgnoreCase("14")) {

                        {
                            list.remove(list.size() - 1); //remove ";"
                            node = tree.assignment(list);
                            statement = true;
                            if (node != null) {
                                //.out.println("Tree " + i);
                                main.add(new JLabel("Line" + i), BorderLayout.PAGE_END);
                                main.add(new JPanel().add(new JTree(node)), BorderLayout.PAGE_END);
                                main.updateUI();
                                //call code gen obvs 

                            } else {
                                //.out.println("ERROR! at line number :" + i);
                                return; //EXIT
                            }

                        }
                    }
                }
                break;
                default:
            }
            main.updateUI();
            if (statement) {
                temp = (DefaultMutableTreeNode) stats.getChildAt(0);
                if (temp.getChildCount() == 0) {
                    temp.add(node);

                } else {
                    temp = (DefaultMutableTreeNode) stats.getChildAt(1);
                    if (temp.getChildCount() == 0) {
                        temp.add(node);
                    } else {
                        temp = stats;
                        stats = new DefaultMutableTreeNode("<stmt-list>");
                        stats.add(temp);
                        te = new DefaultMutableTreeNode("<stmt>");
                        te.add(node);
                        stats.add(te);
                    }

                }
            }

        }
        statsRoot.add(stats);

        main.add(new JPanel().add(new JTree(root)), BorderLayout.PAGE_END);
        main.updateUI();

    }

}
