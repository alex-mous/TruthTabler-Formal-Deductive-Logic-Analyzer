/*
Copyright 2020 Alex Mous

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

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