package Phase2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

/**
 *
 * @author SAMSUNG PC
 */
public class Tree
{

    // DefaultMutableTreeNode style=new DefaultMutableTreeNode(" "); 
    String IDregex = "(22\\^)(\\w+)"; //plus because I need to match at least once
    String INTregex = "(23\\^)(\\d+)";
    String ID, fact, operator;
    int i, expressionN, oh, expressionNested;
    double ex = 0.0;
    boolean error = false;
    Pattern patternID, patternINT;
    Matcher m;
    DefaultMutableTreeNode left, right, mid, node, temp;
    int j, k;

    public Tree()
    {
        i = 0;
        expressionN = 0;
    }
//1 Arithmetics

    public DefaultMutableTreeNode assignment(ArrayList<String> list)
    {

        //to preserve precedence of multiplication and division add brackets around them
        for (i = 0; i < list.size(); i++)
        {
            if (list.get(i).equalsIgnoreCase("18") || list.get(i).equalsIgnoreCase("19"))
            {
                j = i - 2;
                k = i - 1;

                if (!list.get(j).equalsIgnoreCase("20") && !list.get(k).equalsIgnoreCase("21"))
                {
                    list.add(j + 1, "20");
                    j = i + 3;
                    list.add(j, "21");
                    i = j;
                }
            }
        }

        i = 0;
        //System.out.println("in assignment");
        //call when 22 is found at beginning of a line
        DefaultMutableTreeNode assigns = new DefaultMutableTreeNode("<assign>");
        //Assuming I just sent the arraylist of the line,

        ID = list.get(i);
        patternID = Pattern.compile(IDregex);
        patternINT = Pattern.compile(INTregex);
        m = patternID.matcher(ID);
        if (m.matches())
        {
            ID = m.group(2);
        } else
        {
            error = true;
        }

       // System.out.println("<assign> done");
        DefaultMutableTreeNode factors = new DefaultMutableTreeNode("<ID> " + ID);
        i++;
        if (error)
        {
            return null;
        } else
        {
            assigns.add(factors);
        }
       // System.out.println("added a factor");

        DefaultMutableTreeNode middleman = new DefaultMutableTreeNode(":=");
        assigns.add(middleman);
        //System.out.println("added a :=");

        if (list.get(i).equalsIgnoreCase("15"))
        {
            i++;
        } else
        {
            error = true;
        }
        DefaultMutableTreeNode subtree = null;
        if (!error)
        {
            if (list.size() == 3)
            {
                subtree = fact(list);
            } else
            {
                subtree = expression(list);
            }
            if (subtree == null)
            {
                i = 0;
                return null;
            }

            assigns.add(subtree);
            if (i < list.size() - 1)
            {
                node = assigns;
                //get last child
                while (node.getChildCount() != 0)
                {
                    node = (DefaultMutableTreeNode) node.getChildAt(2);
                }
                node = (DefaultMutableTreeNode) node.getParent();
                temp = (DefaultMutableTreeNode) node.getParent();
                expressionN++;
                node.removeFromParent();
                DefaultMutableTreeNode exps = new DefaultMutableTreeNode("<exp> " + expressionN);
                expressionN++;
                temp.add(exps);
                exps.add(node);
                mid = operator(list);
                if (mid != null)
                {

                    exps.add(mid);
                    subtree = expression(list);
                    if (subtree != null)
                    {
                        if (subtree.getChildCount() == 1)
                        {
                            exps.add((DefaultMutableTreeNode) subtree.getChildAt(0));
                            expressionN--;
                        } else
                        {
                            exps.add(subtree);
                        }

                    } else
                    {
                        return null;
                    }

                } else
                {
                    return null;
                }
            }
            return assigns;

        } else
        {
            return null;
        }

    }

    public DefaultMutableTreeNode expression(ArrayList<String> list)
    {
        expressionN++;

        DefaultMutableTreeNode exps = new DefaultMutableTreeNode("<exp> " + expressionN);
        DefaultMutableTreeNode leftie = fact(list);
        if (leftie != null)
        {
            exps.add(leftie);
            if (i == list.size())
            {
                return exps;
            }

        } else
        {
            return null;
        }
        DefaultMutableTreeNode opop = operator(list);
        if (opop != null)
        {
            exps.add(opop);
        } else
        {
            return null;
        }
        DefaultMutableTreeNode rightie = fact(list);
        if (rightie != null)
        {
            exps.add(rightie);
        } //else {
        // return null;
        // }

        return exps;
    }

    public DefaultMutableTreeNode fact(ArrayList<String> list)
    {
      //  System.out.println("in fact");
        DefaultMutableTreeNode factors = null;

//Check for nested bruvv
        m = patternID.matcher(list.get(i));
        if (!m.matches() && !patternINT.matcher(list.get(i)).matches())
        {
         
                
            if (Integer.parseInt(list.get(i)) == 20)
            {
                //expressionN++;
                // factors = new DefaultMutableTreeNode("<exp> " + expressionN);
                i++;
                //DefaultMutableTreeNode leftb = new DefaultMutableTreeNode("(");
                //factors.add(leftb);
                // System.out.println("left BB");
                DefaultMutableTreeNode nest = expression(list);
                if (nest == null)
                {
                    // factors.add(nest);
                    return null;
                }

                // DefaultMutableTreeNode rightb = new DefaultMutableTreeNode(")");
                if (i < list.size())
                {
                    if (Integer.parseInt(list.get(i)) == 21)
                    {
                        //  factors.add(rightb);
                        i++;

                    } else
                    {
                        return null;
                    }
                }
                return nest;

            }
        } //not nested
        else
        {
            fact = list.get(i);
          //  System.out.println("       " + fact);

            m = patternID.matcher(fact);

            if (m.matches())
            {
                ID = m.group(2);
            } else
            {
                m = patternINT.matcher(fact);
                if (m.matches())
                {
                    ID = m.group(2);
                } else
                {
                    return null;
                }
            }
            factors = new DefaultMutableTreeNode("<factor> " + ID);
          //  System.out.println("added a factsss");

            i++;
        }
        //error hayraga3 null so  
        return factors;
    }

    public DefaultMutableTreeNode operator(ArrayList<String> list)
    {
        oh = Integer.parseInt(list.get(i));
        i++;
        switch (oh)
        {
            case 16:
                operator = "+";
                break;
            case 17:
                operator = "-";
                break;
            case 18:
                operator = "*";
                break;
            case 19:
                operator = "DIV";
                break;
            default:
                return null;
        }

        DefaultMutableTreeNode op = new DefaultMutableTreeNode(operator);
        return op;

    }

    //2-FOR LOOPSY LOOPLOOOPP
    public DefaultMutableTreeNode loop(ArrayList<String> list)
    {
        i = 0;
        DefaultMutableTreeNode mom = new DefaultMutableTreeNode("for");
        DefaultMutableTreeNode extra = new DefaultMutableTreeNode("FOR");
        //System.out.println("for");

        mom.add(extra);
        ArrayList<String> temp = new ArrayList<String>();
        DefaultMutableTreeNode index = new DefaultMutableTreeNode("<index-exp");
        mom.add(index);
        i++;
        DefaultMutableTreeNode hi;

        //get exps till to
        int j = i;
        while (!list.get(j).equalsIgnoreCase("10"))
        {
            temp.add(list.get(j));
            j++;
        }
        i = 0;
        extra = assignment(temp);
        if (extra == null)
        {
            return null;
        }

        left = (DefaultMutableTreeNode) extra.getChildAt(0);

        right = (DefaultMutableTreeNode) extra.getChildAt(2);

        mid = (DefaultMutableTreeNode) extra.getChildAt(1);

        index.add(left);
        index.add(mid);
        index.add(right);

        i = j++;
        i++;
       // System.out.println("TO");
        //System.out.println("I=" + i);
        extra = new DefaultMutableTreeNode("TO");
        index.add(extra);
        list.remove(list.size() - 1);
//        System.out.println("size=" + list.size());

        extra = fact(list);
        if (extra != null)
        {
            index.add(extra);
        } else
        {
            return null;
        }
        return mom;
    }
  public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}
