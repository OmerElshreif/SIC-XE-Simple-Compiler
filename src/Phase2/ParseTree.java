package Phase2;

import Phase3.CodeGenerator;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author SAMSUNG PC
 */
public class ParseTree {

    int i,j, start;
    ArrayList<String> list;
    Matcher m;
    Pattern p;
    String regex;
    CodeGenerator code;
    Tree tree;
    JFrame frame;
    DefaultMutableTreeNode node;
    JPanel main;

    public ParseTree() throws IOException {
        tree = new Tree();
        code = new CodeGenerator();
        regex = "(\\d+)(\\^\\w*)";
        p = Pattern.compile(regex);
        frame = new JFrame("TREES");
        main = new JPanel();
        frame.setContentPane(main);
        frame.setSize(200, 200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void median(HashMap<Integer, ArrayList<String>> map) throws IOException {
        for (i = 1; i <= map.size(); i++) {
            //get list corresponding the line;
            list = map.get(i);
            m = p.matcher(list.get(0));
            start = Integer.parseInt(m.group(1));
            switch (start) {

                case 1: //Program 
                    //call function to write begin and program name
                    break;
                case 2: //vars
                    //call call generator function (for vars definition)
                    break;
                case 3: //Begin
                    //Do nothing?
                    break;
                case 5: //END.
                    
                { System.out.println("Code Generated Successfully");
                code.bw.close();
                code.fw.close();
                }
                    break;
                //case 6 (INTEGER) shouldn't be encountered in the beginning of a line
                case 7: //FOR LOOP
                { 
                    node = tree.loop(list);
                    if (node == null)//error
                    {
                        System.out.println("ERROR! at line number :" + i);
                        return; //EXIT??
                    } else {
                        //send the tree to code generator
                        main.add(new JLabel("Line" + i), BorderLayout.PAGE_END);
                        main.add(new JPanel().add(new JTree(node)), BorderLayout.PAGE_END);

                        for (j = i; i < list.size(); j++) {
                            list = map.get(i);
                            m = p.matcher(list.get(0));
                            start = Integer.parseInt(m.group(1));
                            if (start==4) //end of loop
                                //call code gen to write the branch
                                break;
                            //else 
                                //call code gen depending on read/write/arithmetics w kida
                            
                        }
                        i=j;
                    }
                }
                       break;
                case 8://READ
                       //call code gen 
                       break;

                
                case 9: //write
                    //call code gen
                    break;
                case 22: //ARITHMETICS
                {
                    list.remove(list.size()-1); //remove ";"
                    node=tree.assignment(list);
                    if (node!=null)
                    {
                        main.add(new JLabel("Line" + i), BorderLayout.PAGE_END);
                        main.add(new JPanel().add(new JTree(node)), BorderLayout.PAGE_END);
                        
                        //call code gen obvs 
                    }
                    else 
                    {
                         System.out.println("ERROR! at line number :" + i);
                         return; //EXIT
                    }
                        
                }
                    break;
                default:
            }

        }
    }

}
