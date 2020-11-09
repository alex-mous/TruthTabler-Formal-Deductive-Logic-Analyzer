import java.util.*;

public class TruthTabler {
    public static void main(String[] args) { //Console interaction
        Scanner stdin = new Scanner(System.in);
        Map<Character, Boolean> propMap = new HashMap<>();
        while (true) {
            System.out.print("Please enter an expression (use '=' to set a proposition to T/F and q to quit): ");
            String expr = stdin.next();
            if (expr.length() == 1 && expr.charAt(0) == 'q') { //Exit on q
                break;
            } else if (expr.length() == 3 && expr.charAt(1) == '=') { //Set propositions if "=" used
                propMap.put(expr.charAt(0), expr.toLowerCase().charAt(2) == 't');
            } else { //Otherwise evaluate an expression
                LogicNode rootNode = buildTree(expr); //Parse expression into a form of binary tree
                assert rootNode != null; //Ensure that the expression evaluated correctly
                System.out.println("Expression evaluation: " + rootNode.evaluate(propMap)); //Then evaluate the tree
            }
        }
    }

    /*
Syntax and Interpreter Documentation
    Syntax Guidelines:
        Expressions and symbols MUST be in the correct syntax and order or else the interpreter may not generate expected results.

        The symbol ~ may be used to represent logical negation. This symbol may only be used in front of:
            a) Simple single letter propositions (such as "~A")
            b) Expression groups (such as "~(AvB)")

        The group symbols "(" and ")" must be used together in pairs and contain at least one proposition or expression such as:
            a) "(~AvB)"
            b) "((AvB)&~(A&B))"

        The operator symbols are ≡, ⊃, &, and v and stand for biconditional, conditional, AND and OR, respectively. These may be used as such:
            a) "A≡B"
            b) "Av(B&~C)"

        These operator symbols can be used with no more and no fewer than two operands, which may consist of:
            a) Simple single letter propositions (such as "A")
            b) Negated simple single letter propositions (such as "~A")
            c) Expression groups (such as "(AvB)")
            d) Negated expression groups (such as "~(AvB)")

         The symbol = is used for assigning single letter propositions, such as:
            a) "A=T" will set A to be true
            b) "B=F" will set B to be false

     Interpreter usage:
        All propositions must be declared before trying to evaluate any expressions.
        Failure to do this will result in an error.
        Any incorrect syntax may also lead to an error or hung up program, so please ensure that your parentheses match and are using only legal operators.
     */


    /**
     * Build a tree from the raw input string and return the root node
     *
     * @param raw Raw input string
     * @throws IllegalStateException If the expression is unable to be parsed fully
     * @return Root node of the output tree
     */
    public static LogicNode buildTree(String raw) { //Build a tree based off of the expression in raw and the map of propositions to truth values in propMap
        if (raw.length() > 0) {
            //Parse the left side
            LogicNode left = new LogicNode();
            raw = getNode(raw, left);

            if (raw.length() > 0) { //If there is any string left, continue to the right side
                // Get the operator
                char op = raw.charAt(0);
                raw = raw.substring(1);

                //Now parse the right side
                LogicNode right = new LogicNode();
                raw = getNode(raw, right);

                if (raw.length() != 0) {
                    throw new IllegalStateException("Couldn't parse expression fully. Remaining part: " + raw);
                }
                return new LogicNode(op, left, right, NodeType.OPERATOR, false);
            } else {
                return left; //Set a group node
            }
        } else { //Base case - no node to return
            return null;
        }
    }

    /**
     * Get the end index of a group starting at character index 0
     *
     * @param raw Raw input string
     * @return Index of end of group
     */
    public static int getGroupEnd(String raw) { //Get the end index of the group
        Stack<Character> groups = new Stack<>();
        int i = 0;
        do {
            if (raw.charAt(i) == '(') {
                groups.push('(');
            } else if (raw.charAt(i) == ')') {
                groups.pop();
            }
            i++;
        } while (groups.size() > 0);
        return i-1;
    }

    /**
     * Get a node from the string
     *
     * @param raw Raw input string
     * @param newNode New node to overwrite
     * @throws IllegalStateException If the raw string isn't complete
     * @return Raw string with parsed part removed
     */
    public static String getNode(String raw, LogicNode newNode) { //
        boolean inverted = false;
        if (raw.charAt(0) == '~') { //Inversion
            inverted = true;
            raw = raw.substring(1);
        }
        if (raw.charAt(0) == '(') { //Start of group - parse until end
            LogicNode temp = buildTree(raw.substring(1, getGroupEnd(raw)));
            if (temp == null) {
                throw new IllegalStateException("Unable to correctly parse expression. Please check the syntax.");
            }
            newNode.copyFrom(temp);
            newNode.inverted = inverted;
            raw = raw.substring(getGroupEnd(raw)+1); //Trim to no longer include the first half
        } else { //Simple proposition
            newNode.initialize(raw.charAt(0), null, null, NodeType.PROPOSITION, inverted);
            raw = raw.substring(1);
        }
        return raw;
    }

}