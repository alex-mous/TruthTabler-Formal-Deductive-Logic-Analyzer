## Introduction
TruthTabler is a Java console app for analyzing formal deductive argument logic. The current version provides an interactive console that allows the user to set logical propositions and test expressions made up of these propositions and the logical symbols ≡, ⊃, &, and v as well as the grouping symbols ( and ) and the negation operator ~. The usage of these is specified below under the Syntax Guidelines section.

## Installation
The source code for IntelliJ is provided. The code release binaries are also available under Releases.

## Usage
Once the code is running, set propositions as follows:
`A=T` would set assign a True value to A, and `B=F` would assign a False value to B. Propositions are allowed to be reassigned later during runtime. To evaluate expressions, simply enter a valid expression as detailed under Syntax Guidlines such as: `(AvB)&~(A&B)`, which is the exclusive OR and therefore should return a True value for the values of A and B that we set earlier.

*All propositions must be declared before trying to evaluate any expressions.
Failure to do this will result in an error.
Any incorrect syntax may also lead to an error or hung up program, so please ensure that your parentheses match and are using only legal operators.*

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

