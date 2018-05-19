package Phase3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.tree.DefaultMutableTreeNode;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author SAMSUNG PC
 */
public class CodeGenerator {

    int A, S, T;
    //A -->right
    //S-->left
    ArrayList<Integer> Parents;
    String ff;
    DefaultMutableTreeNode node, left, mid, right;
    String l, r;
    Pattern p;
    Matcher m;
    String FILENAME;
   public BufferedWriter bw;
   public FileWriter fw;
    String factorR = "<factor>\\s*(\\w+)"; //plus because I need to match at least once
    String expR = "<exp>\\s*(\\d+)";
    File file;
    boolean leftNested, rightNested;
    boolean number;
    int i;

//CLOSE BOTH LATERRRR
    public CodeGenerator() throws IOException {
        FILENAME = "SICXE.txt";
        file = new File(FILENAME);
        fw = new FileWriter(file.getAbsoluteFile(), true);
        bw = new BufferedWriter(fw);
        bw.flush();
A=0;
    }

    public boolean Arithmetic(DefaultMutableTreeNode tree) throws IOException {

        //get last parent
        node=tree;
        while (node.getChildCount()!=0) {
            node =(DefaultMutableTreeNode) node.getChildAt(2);
        }
        System.out.println("LAST NODE  " + node);
        System.out.println("Parent  " + node.getParent());
        node = (DefaultMutableTreeNode) node.getParent();

        while (!node.isRoot()) {
            leftNested = rightNested = false;
           
            left = (DefaultMutableTreeNode) node.getChildAt(0);
            mid = (DefaultMutableTreeNode) node.getChildAt(1);
            right = (DefaultMutableTreeNode) node.getChildAt(2);
            
           
                
            if (Pattern.compile(expR).matcher(right.toString()).matches()) 
                rightNested = true;
            

            if (Pattern.compile(expR).matcher(left.toString()).matches()) 
                leftNested = true;
            

            if (!rightNested && !leftNested) //I store nested left in S and nested right in A
            {
                
                ff = factor(right);
                bw.write("LDA " + ff);
                
                System.out.println("LDA " + ff);
                bw.newLine();

                ff = factor(left);
                print (ff,mid);

                System.out.println("NODE"+node.toString());
                A=exs(node);
               
            }
           else if (rightNested && !leftNested)
            {
               
                  ff = factor(left);
                  print (ff,mid);

                A=exs(node);
                     

            }
           
          else  if (leftNested&&!rightNested)
            {
                
                 if (A==0)
                bw.write("LDA #0" );
                bw.newLine();
                
                ff = factor(right);
                print (ff,mid);

                S=exs(node);
                  node = (DefaultMutableTreeNode) node.getParent();
brackets(node);
            }
          else  if (leftNested&&rightNested)
            {
                if (mid.toString().equalsIgnoreCase("+")) {
                    bw.write("ADDR S,A ");
                                    System.out.println("ADDR S,A");

                    bw.newLine();
                } else if (mid.toString().equalsIgnoreCase("-")) {
                       bw.write("SUBR S,A ");
                                    System.out.println("SUBR S,A");

                    bw.newLine();
                } else if (mid.toString().equalsIgnoreCase("*")) {
                      bw.write("MULR S,A");
                                    System.out.println("MULR S,A");
                    bw.newLine();
                } else if (mid.toString().equalsIgnoreCase("-")) {
                    bw.write("DIVR S,A");
                                    System.out.println("DIVR S,A");

                    bw.newLine();
                }
                  node = (DefaultMutableTreeNode) node.getParent();
brackets(node);
            }
            

            //right
            node = (DefaultMutableTreeNode) node.getParent();

        }
        if (node.toString().equalsIgnoreCase("<assign>")) {
            ff = factor(left);
            bw.write("STA " + ff);
            bw.newLine();
        }
        return true;

    }

    public String factor(DefaultMutableTreeNode node) throws IOException {

        p = Pattern.compile(factorR);
        number = true;
        m = p.matcher(node.toString());
        if (m.matches()) {

            ff = m.group(1);
            try {
                Integer.parseInt(ff);
            } catch (NumberFormatException e) {
                number = false;
            }
            if (number) {
                ff = "#" + ff;
            }

        }
        return ff;
    }
    public int exs (DefaultMutableTreeNode node)
    {
        p=Pattern.compile(expR);
        m=p.matcher(node.toString());
        if (!m.matches())
            System.out.println("WHY");
        
        l=m.group(1);
        i=Integer.parseInt(l);
        return i;
    }
    public void print (String ff,DefaultMutableTreeNode mid) throws IOException
    {
        
                if (mid.toString().equalsIgnoreCase("+")) {
                    bw.write("ADD " + ff);
                                    System.out.println("ADD " + ff);

                    bw.newLine();
                } else if (mid.toString().equalsIgnoreCase("-")) {
                    bw.write("SUB " + ff);
                                    System.out.println("SUB " + ff);

                    bw.newLine();
                } else if (mid.toString().equalsIgnoreCase("*")) {
                    bw.write("MUL " + ff);
                                    System.out.println("MUL " + ff);

                    bw.newLine();
                } else if (mid.toString().equalsIgnoreCase("-")) {
                    bw.write("DIV " + ff);
                                    System.out.println("DIV " + ff);

                    bw.newLine();
                }
    }
    public void brackets(DefaultMutableTreeNode node)
    {
            
                if (node.getNextSibling()!=null) //left child
                    S=exs(node);
                else 
                    A=exs(node);
            
    }
}
