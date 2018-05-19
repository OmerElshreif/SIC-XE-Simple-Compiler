# SIC-XE-Simple-Compiler
Simple compiler for SIC/XE 
Simple Compiler for SIC/XE Machines that Transform HIGH-LEVEL language to machine level, 
#Phase 1: Lexical Analysis
Lexical analysis involves scanning the program to be compiled, and recognizing the Tokens that make the
source program
This analysis aims to generate a Stream of Tokens, each token is represented by a fixed-length code, the hole
output is represented by a table that contains the line number and Stream of tokens in the line.
The Scanner using a Token coding scheme for recognizing each token and assign a unique code for each
token, The Tokens' Table used in this project can be found at "Leland L. Beck, D. Manjula-System Software_ An Introduction To Systems Programming-Pearson India (1997)"

#Phase 2: Syntactic Analysis
Goal: validate the entire code found in the outputted token table from the lexical analysis (Scanning the code for any error or missing syntax).
During syntactic analysis, the source statements written by the programmer are recognized as language constructs described by the grammer being used.
We may think of this process as building the parse tree for the statements.
The parsing technique used is the Recursive Descent which is a top-down method,
Recursive Descent parser is made up of a procedure for each non-terminal symbol in the grammer, when a procedure is called, it attempts to find a substring of the input, begging with the current token, That can be interpreted as the nonterminal with which the procedure is associated…

#Phase 3: [Code generation & Parse Tree generation]
After validation, the token table is sent to class median and the first token in each line determines the actions taken for recursive descent parsing.

