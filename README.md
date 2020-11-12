# TruthTabler: Formal Deductive Logic Analyzer
TruthTabler is a Java console app for analyzing formal deductive argument logic. 
This app provides a way for the user to evaluate truth tables of arguments to determine
 validity, evaluate truth tables of simple or complex logical expressions to determine their 
 truth based on that of their propositions, and an expression calculator to calculate the 
 truth value of a single expression based on the value of its propositions. The logical 
symbols ≡, ⊃, &, and v as well as the grouping symbols ( and ) and the negation operator ~.
 The usage of these is specified below under the Syntax Guidelines section, and how to use the different modes
 is specified under Usage.

## Installation
The source code for IntelliJ is provided. Code binaries under Releases soon to be provided.

## Usage
Once the code is running, you will be presented with a menu of modes to choose from. The available modes are argument mode, expression table mode, or expression calculator mode.
Argument mode is for evaluating arguments (in the form of a list of premises and conclusion) in a truth table to determine validity (done automatically). Expression table mode is 
similar to argument mode but only allows for one expression and doesn't determine validity. Finally, expression calculator mode allows the user
to calculate various truth values of an expression based on the truth of its propositions. Please use the help command ($h) for guidelines on the exact usage
of each mode.
### **Syntax Guidelines**
1.   Expressions and symbols MUST be in the correct syntax and order or else the interpreter may not generate expected results.
1.  The symbol ~ may be used to represent logical negation. This symbol may only be used in front of:
     1.   Simple single letter propositions (such as "~A")
     1.   Expression groups (such as "~(AvB)")
1.  The group symbols "(" and ")" must be used together in pairs and contain at least one proposition or expression such as:
     1.   "(~AvB)"
     1.   "((AvB)&~(A&B))"
1.  The operator symbols are ≡, ⊃, &, and v and stand for biconditional, conditional, AND and OR, respectively. These may be used as such:
     1.   "A≡B"
     1.   "Av(B&~C)"
1.  These operator symbols can be used with no more and no fewer than two operands, which may consist of:
     1.   Simple single letter propositions (such as "A")
     1.   Negated simple single letter propositions (such as "~A")
     1.   Expression groups (such as "(AvB)")
     1.   Negated expression groups (such as "~(AvB)")
1.   The symbol = is used for assigning single letter propositions, such as:
     1.  "A=T" will set A to be true
     1.  "B=F" will set B to be false
     
## License
This work is licensed under the MIT License. Please see LICENSE for details.

