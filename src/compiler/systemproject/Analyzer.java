/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.systemproject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Omar
 */
public class Analyzer
{

    final private String keywordOrvariables = "\\s*((\\w+)\\s*,?\\s*)+(\\w*)\\s*;?.?:?\\s*(INTEGER)?\\s*";
    final private String assignment = "\\s*(\\w+)\\s*(:=)\\s*(\\(?(\\w*)\\s*(\\+|-|\\*|DIV)?\\s*\\)?)+\\)?;"; //G1 for first ID, G2 for := 
    final private String functions = "\\s*(\\w*)\\((\\w*,?)+\\);";
    final private String forstm = "\\s*FOR\\s+(\\w+)\\s*\\:\\=\\s*(\\w+)\\s*TO\\s*(\\w+)\\s*DO\\s*";
    private ArrayList<String> lineTokens;
    public HashMap<Integer, ArrayList<String>> table;

    public HashMap tokenizer(HashMap tokens,String filename) throws FileNotFoundException, IOException
    {

       // Scanner in = new Scanner(System.in);
       // String fileName;
        table = new HashMap();
        lineTokens = new ArrayList<String>();
        ArrayList<String> temp = new ArrayList();
        System.out.println("PLease input file name to be Compiled: ");
       // fileName = in.nextLine();
        String line;
        String identifier = "";
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        line = reader.readLine().trim();
        while (line!=null && line.trim().length() ==0)
        {
            line = reader.readLine();
        }
        System.out.println("Line: "+ line);
        Pattern keyORvariable = Pattern.compile(keywordOrvariables);
        Pattern assignmentp = Pattern.compile(assignment);
        Pattern functionp = Pattern.compile(functions);
        Pattern forpattern = Pattern.compile(forstm);

        Matcher m = keyORvariable.matcher(line);
        //  System.err.println("Start");
        int lineNumber = 1;
        while (line != null)
        {  
            line = line.trim();
            m = keyORvariable.matcher(line);
            if (m.matches())

            {

                if (isVariable(line))

                {
                    String[] lenght = line.split(",|:");
                    for (int i = 0; i < lenght.length - 1; i++)
                    {
                        lineTokens.add("22^" + lenght[i].replaceAll(" ", ""));
                        if (i < lenght.length - 2)
                        {
                            lineTokens.add(tokens.get(",").toString());
                        }
                    }
                    lineTokens.add(tokens.get(":").toString());
                    lineTokens.add("6");

                } else

                {
                    if (line.trim().split(" ").length > 1)
                    { //System.err.println(line);
                        lineTokens.add(tokens.get(line.split(" ")[0]).toString());
                        lineTokens.add("22^" + line.split(" ")[1].replace(" ", ""));
                    } else
                    {
                      //  System.err.println(line);
                        lineTokens.add(tokens.get(line.replace(" ", "")).toString());
                    }

                }

            } else
            {
                m = assignmentp.matcher(line);
                if (m.matches())
                {
                    temp = new ArrayList<>(Arrays.asList(line.split(":=")));
                    lineTokens.add("22^" + temp.get(0).trim());
                    lineTokens.add(tokens.get(":=").toString());
                    for (int i = 0; i < temp.get(1).length(); i++)
                    {
                        if (temp.get(1).toCharArray()[i] == ' ' || temp.get(1).toCharArray()[i] == '+' || temp.get(1).toCharArray()[i] == '-' || temp.get(1).toCharArray()[i] == '*' || temp.get(1).toCharArray()[i] == ';' || temp.get(1).toCharArray()[i] == '(' || temp.get(1).toCharArray()[i] == ')')
                        {
                            if (isNumeric(identifier.trim()))
                            {
                                lineTokens.add("23^" + identifier.replace(" ", ""));
                                identifier = "";
                            } else if (identifier.trim().equals("DIV"))
                            {
                                lineTokens.add(tokens.get("DIV").toString());
                                identifier = "";
                            } else if (!identifier.trim().equals(""))
                            {
                                lineTokens.add("22^" + identifier.replace(" ", ""));
                                identifier = "";
                            }
                            if (temp.get(1).toCharArray()[i] != ' ' )
                            {
                                lineTokens.add(tokens.get(String.valueOf(temp.get(1).toCharArray()[i])).toString());
                            }

                        } else
                        {
                            identifier += temp.get(1).toCharArray()[i];
                        }

                    }

                } else
                {
                    m = functionp.matcher(line);
                    if (m.matches())
                    {
                     //   System.out.println(m.group(1));
                        lineTokens.add(tokens.get(m.group(1).replace(" ", "")).toString());
                        lineTokens.add(tokens.get("(").toString());
                        temp = new ArrayList(Arrays.asList(line.split("\\(")));

                        for (int i = 0; i < temp.get(1).length(); i++)
                        {
                            if (temp.get(1).toCharArray()[i] == ',' || temp.get(1).toCharArray()[i] == ')' || temp.get(1).toCharArray()[i] == ';')
                            {
                                if (!identifier.trim().equals(""))
                                {
                                    lineTokens.add("22^" + identifier.trim());
                                    identifier = "";
                                }

                                lineTokens.add(tokens.get(String.valueOf(temp.get(1).toCharArray()[i])).toString());

                            } else
                            {
                                identifier += temp.get(1).toCharArray()[i];
                            }

                        }

                    } else
                    {
                        m = forpattern.matcher(line);
                        if (m.matches())
                        {
                            lineTokens.add(tokens.get("FOR").toString());
                            lineTokens.add(tokens.get("ID").toString() + "^" + m.group(1));
                            lineTokens.add(tokens.get(":=").toString());
                            if (isNumeric(m.group(2)))
                            {
                                lineTokens.add("23" + "^" + m.group(2));
                            } else
                            {
                                lineTokens.add(tokens.get("ID").toString() + "^" + m.group(2));
                            }
                            lineTokens.add(tokens.get("TO").toString());
                            if (isNumeric(m.group(3)))
                            {
                                lineTokens.add("23" + "^" + m.group(3));
                            } else
                            {
                                lineTokens.add(tokens.get("ID").toString() + "^" + m.group(3));
                            }
                            lineTokens.add(tokens.get("DO").toString());

                            //  System.out.println(line+": " + m.group(1) + " " +m.group(2)+ " "+m.group(3));
                        }
                         else
                    {
                            System.err.println("Syntax Error At Line: " + line);
                            return null;
                            }
                    }
                    
                }
            }
           
            
            
                    
                    
            table.put(lineNumber, lineTokens);
            lineNumber++;
            lineTokens = new ArrayList();
            line = reader.readLine();
            while (line != null && line.trim().length()==0 )
            {
                line = reader.readLine();
                
            }
            
            System.out.println("Line: "+ line);
        }

    

        for (int i = 1; i < lineNumber; i++)
        {

         
            System.out.println(i);
                if(table.get(i).contains(" "))
                {
                    table.remove(i);
                }
                else
                for (int j = 0; j < table.get(i).size(); j++)
                {
                 System.out.println("      " + table.get(i).toArray()[j]);

                }
            
        }
        return table;
    }

    private boolean isVariable(String line)
    {
        for (char c : line.toCharArray())
        {
            if (c == ',' || c == ':')
            {
                return true;
            }
        }
        return false;

    }

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

}
