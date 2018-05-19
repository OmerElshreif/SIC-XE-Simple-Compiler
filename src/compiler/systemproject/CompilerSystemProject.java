/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.systemproject;

import Phase2.Parser;
import Phase3.Median;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author Omar
 */
public class CompilerSystemProject
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        Analyzer an = new Analyzer();
        String filename="";
        HashMap<Integer,ArrayList<String>> table;
        if(args[0].equals( "-compile"))
        {
            filename = args[1];
        }
        else
        {
            System.err.println("Invalid command");
            return;
        }
        HashMap<String, Integer> tokens = new HashMap();
        tokens.put("PROGRAM", 1);
        tokens.put("VAR", 2);
        tokens.put("BEGIN", 3);
        tokens.put("END;", 4);
        tokens.put("END.", 5);
        tokens.put("INTEGER", 6);
        tokens.put("FOR", 7);
        tokens.put("READ", 8);
        tokens.put("WRITE", 9);
        tokens.put("TO", 10);
        tokens.put("DO", 11);
        tokens.put(";", 12);
        tokens.put(":", 13);
        tokens.put(",", 14);
        tokens.put(":=", 15);
        tokens.put("+", 16);
        tokens.put("-", 17);
        tokens.put("*", 18);
        tokens.put("DIV", 19);
        tokens.put("(", 20);
        tokens.put(")", 21);
        tokens.put("ID", 22);
        tokens.put("int", 23);
        table = an.tokenizer(tokens,filename);
        
        Parser parser = new Parser(table);
       
        if(table != null )
        {    System.out.println("Code is analyzed Succefully"); 
             if(parser.Parse())
             {
            Median genr = new Median();
          genr.median(table);
                
         
          
                  
   
             }
        }
        else
            System.err.println("Syntax Error");
        
    }

}
