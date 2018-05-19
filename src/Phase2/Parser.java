/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Phase2;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Parser
{

    HashMap<Integer, ArrayList<String>> table;  //Contains all the Program Tokens
    ArrayList<String> line; //Will be containing the program LINE by Line for Checking...
    int index, lineIndex;
    ArrayList<String> reserve;
    ArrayList<String> functionParameters;
    ArrayList<String> code = new ArrayList();
    ArrayList<String> assignCode = new ArrayList();
    ArrayList<String> functions = new ArrayList();
    String registerA = "";
    String registerT = "";
    String programName = "";
    int forIndex = 0;
    int assignindex = 0;
    Stack<String> operands = new Stack();
    Stack<String> ops = new Stack();
    ArrayList<String> regT = new ArrayList();
    int regTIndex = 0;

    public Parser(HashMap<Integer, ArrayList<String>> table)
    {
        this.table = table;
    }

    public boolean Parse() throws FileNotFoundException, UnsupportedEncodingException
    {
        boolean found = false;
        lineIndex = 1;
        index = 0;

        line = table.get(lineIndex);
        while (line.size() == 0)
        {//System.out.println("adding line"+line);
            lineIndex++;
            line = table.get(lineIndex);
        }
        if (line.get(index).equals("1"))
        {
            System.out.println("Program Check  ");
            index++;
            if (line.get(index).matches("22^\\w*"));
            {

                programName = line.get(index).split("\\^")[1];
                lineIndex++;             //Advance for the next line to check the VAR and list of variables;
                line = table.get(lineIndex);
                index = 0;               //Start of the line;
                while (line.size() == 0)
                {//System.out.println("adding line"+line);
                    lineIndex++;
                    line = table.get(lineIndex);
                }
                if (line.get(index).equals("2"))
                {
                    lineIndex++;
                    line = table.get(lineIndex);
                    while (line.size() == 0)
                    {
                        //  System.out.println("adding line"+line);
                        lineIndex++;
                        line = table.get(lineIndex);
                    }
                    index = 0;

                    if (decList())
                    {
                        lineIndex++;
                        line = table.get(lineIndex);
                        while (line.size() == 0)
                        {
                            lineIndex++;
                            line = table.get(lineIndex);
                        }
                        index = 0;

                        found = line.get(index).equals("3");
                        index++;
                        //found = true;
                    }

                }

            }

            while (found && lineIndex < (table.size() - 1))
            {

                lineIndex++;
                index = 0;
                //Advance for the next line to check the VAR and list of variables;
                line = table.get(lineIndex);
                //     while(line.size()==0)
                //  {
                //  lineIndex++;
                //    line = table.get(lineIndex);
                //}
                System.out.println("line no." + lineIndex + " Contains: " + line);

                found = stmt();

            }
            // System.out.println("Out of while loop, line: ");
            lineIndex++;
            index = 0;

            line = table.get(lineIndex);
            while (line != null && line.size() == 0)
            {
                lineIndex++;
                line = table.get(lineIndex);
            }
            // System.out.println("Final line is:  " + line);
            found = line.get(index).equals("5");

        }
        if (found)
        {
            writeCode();
            for (int i = 0; i < reserve.size(); i++)
            {
                code.add(reserve.get(i) + "     RESW  1");
            }

            for (int i = 0; i < code.size(); i++)
            {
                System.out.println(code.get(i));
            }

        } else
        {
            System.err.println("Compile Error");
        }

        return found;
    }

    //X,Y,Z : Integer
    private boolean decList()
    {
        boolean found = false;
        //  System.out.println("line:" + line.get(index));
        reserve = new ArrayList();
        if (idList(reserve))
        {
            //index++;

            if (line.get(index).equals("13"))
            {   // System.out.println("if1");
                index++;
                if (line.get(index).equals("6"))
                {
                    found = true;
                }
            }

        }
        if (found)
        {
            //    System.out.println("dec-list is True");
        } else
        {
            System.out.println("dec-list is False");
        }
        return found;

    }

    public boolean stmt()
    {
        boolean found = false;
        //Index of the caharacter that we are cheking in each line.
        // System.out.println(line);
        if (line.get(index) != null && line.get(index).equals("8") || line.get(index).equals("9"))                   //Checking the First Token in each line to determing it's type
        {
            if (line.get(index).equals("8"))
            {
                if (!functions.contains("XREAD"))
                {
                    functions.add("XREAD");
                }
                code.add("+JSUB XREAD");
            } else if (line.get(index).equals("9"))
            {
                if (!functions.contains("XWRITE"))
                {
                    functions.add("XWRITE");
                }
                code.add("+JSUB XWRITE");
            }
            index++;

            found = ReadOrWrite();
        } else if (line.get(index).matches("22\\^\\w*"))
        {
            assignCode.add(line.get(index).split("\\^")[1]);
            index++;
            found = assign();
        } else if (line.get(index).equals("7"))
        {
            index++;
            found = For();
            if (found)
            {
                String j1 = new String();
                String j2 = new String();
                j1 = "L" + forIndex;
                forIndex++;
                j2 = "L" + forIndex;
                reserve.add(j1);
                reserve.add(j2);

                forIndex++;
                if (isNumeric(assignCode.get(1)))
                {
                    code.add("LDA  #" + assignCode.get(1));
                } else
                {
                    code.add("LDA  " + assignCode.get(1));
                }
                code.add(j1 + "   STA  " + assignCode.get(0));
                if (isNumeric(assignCode.get(2)))
                {
                    code.add("COMP  #" + assignCode.get(2));
                } else
                {
                    code.add("COMP  " + assignCode.get(2));
                }
                code.add("JGT  " + j2);

                String vari = assignCode.get(0);
                String value = assignCode.get(1);
                assignCode = new ArrayList();
                lineIndex++;
                index = 0;
                line = table.get(lineIndex);
                while (line.size() == 0)
                {
                    //System.out.println("adding line"+line);
                    lineIndex++;
                    line = table.get(lineIndex);
                }
                if (line.get(index).equals("3"))
                {
                    //code.add("LOOP" + forIndex);
                    forIndex++;
                    lineIndex++;
                    index = 0;
                    line = table.get(lineIndex);
                    while (line.size() == 0)
                    {//System.out.println("adding line"+line);
                        lineIndex++;
                        line = table.get(lineIndex);
                    }

                    while (line.get(index) != null && !line.get(index).equals("4") && found)
                    {
                        //int temp = new int;
                        found = stmt();

                        // System.out.println("Line " + line);
                        // if (lineIndex == temp)
                        // {
                        lineIndex++;
                        // }

                        index = 0;
                        line = table.get(lineIndex);
                        while (line.size() == 0)
                        {//System.out.println("adding line"+line);
                            lineIndex++;
                            line = table.get(lineIndex);
                        }
                        //   System.out.println("Lineto Check in while  " + line);

                    }
                    //if( line.get(index).equals("4") && found)
                }
                code.add("LDA  " + vari);
                code.add("ADD  #1");
                code.add("J   " + j1);
                code.add(j2);

            }

        }

        if (found)
        {
            // System.out.println("vari"+reserve);
        } else
        {
            System.out.println("stmt is false");
        }
        return found;
    }

    //For checking the READ Fuction : READ(IDLIST) , IDLIST is the Parameters, 
    private boolean ReadOrWrite()
    {

        boolean found = false;

        if (line.get(index).equals("20")) //Check for the '(' after READ
        {
            index++;
            functionParameters = new ArrayList();
            if (idList(functionParameters)) //Now we need to check ID-List between the pracets so we call the function that checks Id-List
            {
                //index++; //if the Id list is Ok, then we advance for the next token which is ")"
                if (line.get(index).equals("21"))
                {
                    index++;
                    found = (line.get(index).equals("12"));   //checking for ;
                }
            }

        }
        if (found)
        {
            //  System.out.println("READORWRITE is True");
            code.add("WORD  " + functionParameters.size());

            for (int i = 0; i < functionParameters.size(); i++)
            {
                code.add("WORD  " + functionParameters.get(i));
            }

        } else
        {
            System.out.println("READORWRITE is False");
        }

        return found;

    }

    //For cheking for the ID-List
    private boolean idList(ArrayList<String> reserve)
    {
        boolean found = false;

        if (line.get(index).trim().matches("22\\^\\w+"))
        {

            reserve.add(line.get(index).trim().split("\\^")[1]); //adding the Variables to list to generate RESW

            found = true;
            index++;

            //System.out.println("line:" + line.get(index));
            while (line.get(index).equals("14") && found && index < line.size() - 2)
            { // System.out.println("line: in" + line.get(index));
                index++;

                if (line.get(index).matches("22\\^\\w+"))
                {
                    reserve.add(line.get(index).trim().split("\\^")[1]); //adding the Variables to list to generate RESW

                    index++;
                } else
                {
                    found = false;
                }
            }

        }
        if (found)
        {
            //System.out.println("ID-List is True, : " + line.get(index));
        } else
        {
            System.out.println("ID-List is Flase");
        }

        return found;

    }

    private boolean assign()
    {
        boolean found = false;
        if (line.get(index).equals("15"))
        {
            assignCode.add("=");

            index++;

            if (EXP())
            {

                // index++;
                found = true;
            }

        }
        if (found)
        {
            //  System.out.println(assignCode);
            if (assignCode.get(2).matches("\\d+") && assignCode.size() == 3)
            {
                code.add("LDA  #" + assignCode.get(2));

            } else
            {
                assignGenerate();
            }
            code.add("STA   " + assignCode.get(0));
            assignCode = new ArrayList();

        } else
        {
            System.out.println("Assign error");
        }
        return found;
    }

    private boolean EXP()
    {
        boolean found = false;

        if (term())
        {

            found = true;
            // index++;

            while (line.get(index).equals("16") || line.get(index).equals("17") && found)
            {
                if (line.get(index).equals("16"))
                {
                    assignCode.add("ADD");
                } else
                {
                    assignCode.add("SUB");
                }
                index++;
                //  System.out.println(line.get(index));
                found = term();

            }
        }
        return found;

    }

    private boolean term()
    {
        boolean found = false;

        if (factor())
        {
            found = true;
            // index++;

            while (line.get(index).equals("18") || line.get(index).equals("19") && found)
            {
                if (line.get(index).equals("18"))
                {
                    assignCode.add("MULT");
                } else
                {
                    assignCode.add("DIV");
                }
                index++;

                found = factor();

            }
        }
        if (found)
        {
            //   System.out.println("Term Success");
        } else
        {
            System.out.println("Term Faild");
        }
        return found;
    }

    //Factor = id || int || ( EXP ) ;
    private boolean factor()
    {
        boolean found = false;

        if (line.get(index).trim().matches("23\\^\\w+") || line.get(index).trim().matches("22\\^\\w*"))
        {
            // System.out.println("Hey: " + line.get(index));

            assignCode.add(line.get(index).split("\\^")[1]);
            found = true;
            index++;
        } else if (line.get(index).equals("20"))
        {
            System.out.println("Found (");
            index++;
            if (EXP())
            { System.out.println("Found )");
               

                found = line.get(index).equals("21"); //for the ")"      
            }
        }
        if (found)
        {
            //System.out.println("Factor Success");
        } else
        {
            System.out.println("Factor Faild");
        }
        return found;
    }

    private boolean For()
    {
        boolean found = false;

        if (EXP())
        {

            if (line.get(index).equals("15"))
            {
                index++;

                if (EXP())
                {
                    //index++;
                    //  System.out.println("For :  " + line.get(index));
                    if (line.get(index).equals("10"))
                    {

                        index++;
                        // System.out.println("For :  " + line.get(index));
                        if (EXP())
                        {
                            //index++;
                            if (line.get(index).equals("11"))
                            {
                                found = true;
                            }
                        }
                    }
                }
            }
        }
        if (found)
        {

        }

        return found;
    }

    private void assignGenerate()
    {
        String temp;
        String srcA;
        String srcB;
        String operation = "";
        for (int i = 2; i < assignCode.size(); i++)
        {
            temp = assignCode.get(i);

            if (temp.equals("ADD") || temp.equals("SUB") || temp.equals("MULT") || temp.equals("DIV"))
            {
                while (!ops.empty() && hasPrecedence(temp, ops.peek()))
                {
                    operation = ops.pop();
                    srcB = operands.pop();
                    srcA = operands.pop();
                    if (registerA.equals("")) //it means That Register A is Empty
                    {
                        code.add("LDA  " + srcA);
                        code.add(operation + "  " + srcB);
                        operands.push("A");
                        registerA = "A";
                    } else if ("A".equals(srcA)) //Register a contains SRC A

                    {
                        code.add(operation + "  " + srcB); //Register a contains SRC B
                        operands.push("A");
                    } else if ("A".equals(srcB) && !operation.equals("SUB") && !operation.equals("DIV"))

                    {
                        code.add(operation + "  " + srcA);
                        operands.push("A");
                    } else
                    {
                        code.add("STA  T" + regTIndex); //Register A contains something else, we use temp register
                        if (!operands.empty() && operands.peek().equals("A"))
                        {
                            operands.pop();
                        }
                        operands.push("T" + regTIndex);
                        regTIndex++;
                        code.add("LDA  " + srcA);
                        code.add(operation + "    " + srcB);
                        operands.push("A");
                    }

                }
                ops.push(temp);
            } else if (temp.matches("\\w+|\\d+") || temp.equals("("))
            {
                if(temp.trim().equals("("))
                    ops.push(temp);
                else
                if (isNumeric(temp))
                {
                    operands.push("#" + temp);
                } else
                {
                    operands.push(temp);
                }
            } else if (temp.equals(")"))
            {
                while (!ops.peek().equals("("))
                {
                    operation = ops.pop();
                    srcB = operands.pop();
                    srcA = operands.pop();
                    if (registerA.equals("")) //it means That Register A is Empty
                    {
                        code.add("LDA  " + srcA);
                        code.add(operation + "  " + srcB);
                        operands.push("A");
                        registerA = "A";
                    } else if ("A".equals(srcA)) //Register a contains SRC A

                    {
                        code.add(operation + "  " + srcB); //Register a contains SRC B
                        operands.push("A");
                    } else if ("A".equals(srcB) && !operation.equals("SUB") && !operation.equals("DIV"))

                    {
                        code.add(operation + "  " + srcA);
                        operands.push("A");
                    } else if ("A".equals(srcB) && operation.equals("SUB") || operation.equals("DIV"))
                    {

                    } else
                    {
                        code.add("STA  T" + regTIndex); //Register A contains something else, we use temp register
                        if (!operands.empty() && operands.peek().equals("A"))
                        {
                            operands.pop();
                        }
                        operands.push("T" + regTIndex);
                        regTIndex++;
                        code.add("LDA  " + srcA);
                        code.add(operation + "   " + srcB);
                        operands.push("A");
                    }

                }
                ops.pop();
            }

        }

        while (!ops.empty())
        {
            operation = ops.pop();
            srcB = operands.pop();
            srcA = operands.pop();
            if (registerA.equals("")) //it means That Register A is Empty
            {
                code.add("LDA  " + srcA);
                code.add(operation + "  " + srcB);
                operands.push("A");
                registerA = "A";
            } else if ("A".equals(srcA)) //Register a contains SRC A

            {
                code.add(operation + "  " + srcB); //Register a contains SRC B
                operands.push("A");
            } else if ("A".equals(srcB) && !operation.equals("SUB") && !operation.equals("DIV"))

            {
                code.add(operation + "  " + srcA);
                operands.push("A");
            } else if ("A".equals(srcB) && operation.equals("SUB") || operation.equals("DIV"))
            {

            } else
            {
                code.add("STA  T" + regTIndex); //Register A contains something else, we use temp register
                if (!operands.empty() && operands.peek().equals("A"))
                {
                    operands.pop();
                }
                operands.push("T" + regTIndex);
                regTIndex++;
                code.add("LDA  " + srcA);
                code.add(operation + "   " + srcB);
                operands.push("A");
            }

        }
        registerA = "";
    }

    private boolean hasPrecedence(String temp, String peek)
    {
        if ((temp.equals("MULT") || temp.equals("DIV")) && peek.equals("ADD") || peek.equals("SUB"))
        {
            return false;
        } else
        {
            return true;
        }

    }

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public void writeCode() throws FileNotFoundException, UnsupportedEncodingException
    {
        PrintWriter writer = new PrintWriter("MachineCode.asm", "UTF-8");

        writer.println(programName + "    " + "START    0");
        writer.print("    EXTREF    ");
        for (int i = 0; i < functions.size() - 1; i++)
        {
            writer.print(functions.get(i) + ",");
        }

        writer.print(functions.get(functions.size() - 1));
        writer.println();
        writer.println("     STL   RETADR");
        for (int i = 0; i < code.size(); i++)
        {
            if (code.get(i).matches("\\s*L\\d+\\s+\\w+\\s+\\w+\\s*"))
            {
                writer.println(code.get(i));

                // System.out.println("Found Sequence 1");
            } else if (code.get(i).matches("\\s*L\\d+\\s*"))
            {
                writer.print(code.get(i));
                i++;
                writer.println("   " + code.get(i));
                // System.out.println("Found Sequence 2");
            } else
            {
                writer.println("     " + code.get(i));
            }

        }

        for (int i = 0; i < reserve.size(); i++)
        {
            writer.println(reserve.get(i) + "   RESW   1");
        }

        writer.close();
    }
}
