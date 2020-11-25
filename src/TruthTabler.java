/*
Copyright 2020 Alex Mous

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

import java.util.*;

public class TruthTabler {
    private static final String VALID_SYMBOL_REGEX = "^[\\(\\)~&v≡:>⊃(A-Z)]+$";

    public static void main(String[] args) { //Console interaction
        if (args.length > 0) {
            runArguments(args);
        } else {
            interactivePrompt();
        }
    }

    /**
     * Parse the arguments and run the appropriate commands
     *
     * @param args List of string arguments
     */
    public static void runArguments(String[] args) {
        //Parse arguments and run commands accordingly
        char collecting = '0'; //Flag to use to collect parameters
        ArrayList<String> premises = new ArrayList<>();
        String conclusion = null;
        String expression = null;
        for (String arg: args) {
            if (arg.charAt(0) == '-' && arg.length() > 2) {
                switch (arg.charAt(1)) {
                    case 'h': //Help
                        printConsoleHelp();
                        break;
                    case 'p': //Premise
                        collecting = 'p';
                        break;
                    case 'c': //Conclusion
                        collecting = 'c';
                        break;
                    case 'e': //Expression
                        collecting = 'e';
                        break;
                    default:
                        printConsoleHelp();
                        throw new IllegalArgumentException("Invalid parameter: '" + arg + "'");
                }
            } else {
                switch (collecting) {
                    case 'p': //Premises
                        premises.addAll(Arrays.asList(arg.split(","))); //Add each comma separated premise to the list
                        break;
                    case 'c': //Conclusion
                        conclusion = arg;
                        break;
                    case 'e':
                        expression = arg;
                        break;
                    default:
                        throw new IllegalArgumentException("Please specify a parameter first: '" + arg + "'");
                }
            }
        }
        if (premises.size() > 0 && conclusion != null) { //Argument
            System.out.println(evaluateArgument(premises, conclusion));
        } else if (expression != null) {
            System.out.println(evaluateExpression(expression));
        }
    }

    /**
     * Evaluate an argument for all possible inputs and return it's truth table and validity in a human-readable string
     *
     * @param premises ArrayList of argument premises
     * @param conclusion String of argument conclusion
     * @return Human-readable string of truth table and validity
     */
    public static String evaluateArgument(ArrayList<String> premises, String conclusion) {
        String res = "";


        return res;
    }

    /**
     * Evaluate an expression for all possible inputs and return it's truth table in a human-readable string
     *
     * @param expression String of expression
     * @return Human-readable string of truth table
     */
    public static String evaluateExpression(String expression) {
        String res = "";


        return res;
    }

    /**
     * Interactive console prompt mode
     */
    public static void interactivePrompt() {
        Scanner stdin = new Scanner(System.in);
        Map<Character, Boolean> propMap = new HashMap<>(); //Map for propositions (used in c mode)
        char mode = 'c'; //Default to calculator mode
        printConsoleInfo();
        printHelp(mode); //Print out help for default mode
        while (true) {
            System.out.print("(" + mode + ") > ");
            String expr = stdin.next();
            if (expr.equalsIgnoreCase("$q")) { //Exit on q
                break;
            } else if (expr.equalsIgnoreCase("$h")) { //Print help
                printHelp(mode); //Print out mode help
                printHelp('\0'); //Then, print out default help
            } else if (expr.equalsIgnoreCase("$c")) { //Expression calculator mode
                mode = 'c';
                printHelp(mode);
                propMap = new HashMap<>(); //Reset propMap
            } else if (expr.equalsIgnoreCase("$e")) { //Expression truth table mode
                mode = 'e';
                printHelp(mode);
            } else if (expr.equalsIgnoreCase("$a")) { //Argument evaluation/truth table mode
                mode = 'a';
                printHelp(mode);
            } else { //Run appropriate function based on mode
                switch (mode) {
                    case 'a': //Argument
                        System.out.println(runArgumentTable(expr));
                        break;
                    case 'e': //Evaluation of expression truth table
                        System.out.println(runExpressionTable(expr));
                        break;
                    case 'c': //Expression calculator mode
                        System.out.println(runExpressionCalculator(expr, propMap));
                        break;
                }
            }
        }
    }

    /**
     * Run a command for expression truth table mode
     *
     * @param expr Expression
     * @return String message
     */
    public static String runExpressionTable(String expr) {
        if (expr.matches(VALID_SYMBOL_REGEX)) { //Otherwise, test that the expression matches a logical one and evaluate it
            //First, build a map of propositions with all true values
            Map<Character, Boolean> propMap = new LinkedHashMap<>(); //Linked hash map to preserve key order
            for (Character c: expr.toCharArray()) { //Iterate over each character
                if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) && c != 'v') { //Add letter propositions
                    propMap.put(c, true);
                }
            }
            //Next, build the expression
            LogicNode rootNode;
            try {
                rootNode = buildTree(expr); //Parse expression into a form of binary tree
                if (rootNode == null) { //Ensure that the expression evaluated correctly
                    return "Error: invalid expression. Please try again.";
                }
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
            //Finally, evaluate the expression for possible proposition combination in propMap
            String[] args = Arrays.copyOf(String.valueOf(propMap.keySet()).split(""), propMap.size()+1); //Create an array of truth table parts to use in generating decoration for the table
            args[propMap.size()] = expr;

            String res = ""; //Resulting table
            final int[] dividers = new int[]{propMap.size()-1}; //Dividers for table
            res += getLine('╔', '╗', '═', '╦', args, dividers);
            res += "║";
            for (Character c: propMap.keySet()) {
                res += " " + c + " ║";
            }
            res += "║ " + expr + " ║\n";
            res += getLine('║','║','═','╬', args, dividers);
            for (int i=0; i<Math.pow(2,propMap.size()); i++) { //Possible number of different combinations is 2^n where n is the number of propositions
                int k = propMap.size()-1;
                res += "║";
                for (Character key: propMap.keySet()) {
                    propMap.put(key, (i & (1 << k)) == 0);
                    res += " " + (propMap.get(key) ? "T" : "F") + " ║";
                    k--;
                }
                res += "║";
                if (expr.length() > 1) { //Larger than single letter proposition
                    res += String.format(" %" + (expr.length()+1)/2 + "s%" + (expr.length()/2) + "s ║", (rootNode.evaluate(propMap) ? "T" : "F"), "") + "\n"; //Pad string correctly so that centered
                } else {
                    res += String.format(" %s ║", (rootNode.evaluate(propMap) ? "T" : "F")) + "\n";
                }
                if (i<Math.pow(2,propMap.size())-1) { //Middle loops
                    res += getLine('║','║','═','╬', args, dividers);
                } else { //Last loop
                    res += getLine('╚','╝','═','╩', args, dividers);
                }
            }
            return res;
        } else {
            return "Command/expression not recognized. Please check the syntax or use $h for help.";
        }
    }

    /**
     * Run a command for argument truth table mode
     *
     * @param expr Expression
     * @return String message
     */
    public static String runArgumentTable(String expr) {
        if (expr.matches("^[\\(\\)\\~\\&v≡>⊃(A-Z),]+$")){ //Otherwise, test that the expression matches a logical one and evaluate it
            String[] argument = expr.split(",");

            //First, build a map of propositions with all true values
            Map<Character, Boolean> propMap = new LinkedHashMap<>(); //Linked hash map to preserve key order
            for (Character c: expr.toCharArray()) { //Iterate over each character
                if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) && c != 'v') { //Add letter propositions
                    propMap.put(c, true);
                }
            }
            //Next, build the expressions
            LogicNode[] rootNodes = new LogicNode[argument.length];
            for (int i=0; i<argument.length; i++) {
                try {
                    rootNodes[i] = buildTree(argument[i]); //Parse expression into a form of binary tree
                    if (rootNodes[i] == null) { //Ensure that the expression evaluated correctly
                        return "Error: invalid expression: " + argument[i] + ". Please try again.";
                    }
                } catch (Exception e) {
                    return "Error: (at expression: " + argument[i] + ")" + e.getMessage();
                }
            }

            //Then, generate an array to store the argument results
            boolean[] argumentResults = new boolean[(int)Math.pow(2,propMap.size())];

            //Finally, evaluate the expression for possible proposition combination in propMap
            String[] args = Arrays.copyOf(String.valueOf(propMap.keySet()).split(""), propMap.size()+argument.length); //Create an array of truth table parts to use in generating decoration for the table
            for (int i=0; i<argument.length; i++) {
                args[i+propMap.size()] = argument[i];
            }

            String res = ""; //Resulting table
            final int[] dividers = new int[]{propMap.size()-1, propMap.size()+argument.length-2}; //Dividers for table
            res += getLine('╔', '╗', '═', '╦', args, dividers);
            res += "║";
            for (Character c: propMap.keySet()) {
                res += " " + c + " ║";
            }
            res += "║";
            for (int i=0; i<argument.length; i++) {
                res += " " + argument[i];
                if (i==argument.length-2) { //Conclusion double bar
                    res += " ║║";
                } else {
                    res += " ║";
                }
            }
            res += "\n";
            res += getLine('║','║','═','╬',args, dividers);
            for (int i=0; i<Math.pow(2,propMap.size()); i++) { //Possible number of different combinations is 2^n where n is the number of propositions
                int k = propMap.size()-1;
                res += "║";
                for (Character key: propMap.keySet()) {
                    propMap.put(key, (i & (1 << k)) == 0);
                    res += " " + (propMap.get(key) ? "T" : "F") + " ║";
                    k--;
                }
                argumentResults[i] = true;
                for (int j=0; j<argument.length; j++) { //Iterate over arguments
                    boolean argumentVal = rootNodes[j].evaluate(propMap);
                    if (j == 0 || j == argument.length-1) { //Beginning or before last add double bars
                        res += "║";
                    }
                    if (j == argument.length-1) { //Set the argument results
                        argumentResults[i] = !argumentResults[i] || argumentVal; //Ensure that the premises cannot be all true and the conclusion false (premises could be false and conclusion true though)
                    } else {
                        argumentResults[i] &= argumentVal; //AND results if premise
                    }
                    if (argument[j].length() > 1) { //Larger than single letter proposition
                        res += String.format(" %" + (argument[j].length()+1)/2 + "s%" + (argument[j].length()/2) + "s ║", (argumentVal ? "T" : "F"), ""); //Pad string correctly so that centered
                    } else {
                        res += String.format(" %s ║", (argumentVal ? "T" : "F"));
                    }

                }
                res += "\n";
                if (i<Math.pow(2,propMap.size())-1) { //Middle loops
                    res += getLine('║','║','═','╬',args, dividers);
                } else { //Last loop
                    res += getLine('╚','╝','═','╩',args, dividers);
                }
            }
            boolean validity = true; //Argument validity
            for (boolean arg: argumentResults) { //Ensure all results were valid
                validity &= arg;
            }
            res += "Validity: " + (validity ? "Valid" : "Invalid") + "\n";
            return res;
        } else {
            return "Command/expression not recognized. Please check the syntax or use $h for help. Note that the premises and the conclusion must each be separated by commas and the conclusion is the last item.";
        }
    }

    /**
     * Get a line divider for a truth table
     *
     * @param start Start char
     * @param end End char
     * @param filler Filler char
     * @param divider Divider char
     * @param props Parts in table
     * @param specialDivider Special divider index in props
     * @return String of dividers
     */
    public static String getLine(char start, char end, char filler, char divider, String[] props, int[] specialDivider) {
        String res = "" + start;
        int j = 0;
        for (String prop: props) {
            res += filler;
            for (int i=0; i<prop.length(); i++) {
                res += filler;
            }
            res += filler + "" + divider;
            for (int dividerVal: specialDivider) {
                if (dividerVal == j) {
                    res += divider;
                }
            }
            j++;
        }
        res = res.substring(0,res.length()-1); //Remove redundant divider
        res += end + "\n";
        return res;
    }

    /**
     * Run a command for expression calculator mode based on the expression
     *
     * @param expr Expression
     * @param propMap Proposition map
     * @return String message
     */
    public static String runExpressionCalculator(String expr, Map<Character, Boolean> propMap) {
        if (expr.length() == 3 && expr.charAt(1) == '=') { //Set propositions if "=" used
            if (expr.toLowerCase().charAt(2) != 't' && expr.toLowerCase().charAt(2) != 'f') { //Ensure that value is either t or f
                return "Error: propositions must be either true or false (T/F)";
            }
            propMap.put(expr.charAt(0), expr.toLowerCase().charAt(2) == 't');
            return "";
        } else if (expr.matches(VALID_SYMBOL_REGEX)){ //Otherwise, test that the expression matches a logical one and evaluate it
            try {
                LogicNode rootNode = buildTree(expr); //Parse expression into a form of binary tree
                if (rootNode == null) { //Ensure that the expression evaluated correctly
                    return "Error: invalid expression. Please try again.";
                }
                return "Expression evaluation: " + rootNode.evaluate(propMap); //Then evaluate the tree
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        } else {
            return "Command/expression not recognized. Please check the syntax or use $h for help.";
        }
    }

    /**
     * Print out the console help page
     */
    public static void printConsoleHelp() {
        System.out.println("TruthTabler - Formal Logic Expression and Argument Truth Table Evaluator\n" +
                "Version 1.1.0\n\n");
        printHelp('\0');
    }

    /**
     * Print out the help information
     */
    public static void printHelp(char mode) {
        switch (mode) {
            case 'c':
                System.out.println("Expression calculator mode. Calculate expression truth values using defined propositons. Usage:\n" +
                        "\tSet propositions using the syntax [A-Z]=[T/F] to set a proposition letter A to Z to either True or False\n" +
                        "\tEvaluate expressions (only containing propositions set with above method and valid symbols), such as 'Av(B&C)'");
                break;
            case 'a':
                System.out.println("Argument calculator mode. Print argument truth tables and determine validity. Usage:\n" +
                        "\tEnter premises and conclusion as a comma-separated list, such as 'AvB,B>A,B&A'");
                break;
            case 'e':
                System.out.println("Expression table mode. Print expression truth table. Usage:\n" +
                        "\tEnter expressions only containing proposition letters and valid symbols, such as 'Av(B&C)'");
                break;
            default: //Default help
                System.out.println(
                        "Usage:\n\t" +
                                "Enter an expression, command, argument, or proposition\n" +
                                "\tCommands:\n" +
                                "\t\t$h (this help)\n" +
                                "\t\t$q (quit)\n" +
                                "\tExpressions:\n" +
                                "\t\tMust only contain defined propositions and valid logical symbols\n" +
                                "\t\tValid logical symbols are limited to ( and ) for grouping, v for OR, & for AND, ≡ or : for biconditional, ⊃ or > for conditional, and ~ for NOT\n" +
                                "\t\tGrouping symbols ( and ) must be used so that each operator (excluding ~) has no more and no less than two operands (for example, (P&Q)&R is valid, but P&Q&R is not)\n" +
                                "\t\tNegation ~ may be used before groups or propositions, but never before another operator (for example, ~P&Q and ~(PvQ)&R are valid, but P~&Q is not)\n" +
                                "\tArguments:\n" +
                                "\t\tEnter a list of premises (expressions as detailed above) separated by commas. Append the conclusion to this list with another comma as a separator following the same principles for expressions as above\n" +
                                "\tPropositions:\n" +
                                "\t\tUse '=' to defined a proposition to T/F\n" +
                                "\t\tPropositions must be single letters (such as p or Q), and are case sensitive" +
                                "\t\tPropositions may be redeclared later on during runtime"
                );
        }

    }

    /**
     * Print out the information for the interactive console
     */
    public static void printConsoleInfo() {
        System.out.println(
            "TruthTabler - Formal Logic Expression and Argument Truth Table Evaluator\n" +
            "Version 1.2.0\n\n" +
            "Copyright 2020 Alex Mous\n" +
            "Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
            "of this software and associated documentation files (the \"Software\"), to deal\n" +
            "in the Software without restriction, including without limitation the rights\n" +
            "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
            "copies of the Software, and to permit persons to whom the Software is\n" +
            "furnished to do so, subject to the following conditions:\n" +
            "\n" +
            "The above copyright notice and this permission notice shall be included in all\n" +
            "copies or substantial portions of the Software.\n" +
            "\n" +
            "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
            "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
            "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
            "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
            "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
            "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\n" +
            "SOFTWARE.\n\n" +
            "*** Interactive Prompt ***" +
                    "\nCommands:\n\t$h for help\n\t$q to quit\n" +
                    "\t$c for expression calculator mode (default)\n" +
                    "\t$a for argument evaluation mode\n" +
                    "\t$e for expression table mode\n\n" +
                    "Valid symbols:\n\tGrouping: '(' and ')'\n\tNegation: '~'\n" +
                    "\tConjunction: '&'\n\tConditional: '⊃' or '>'\n\tDisjunction: 'v'\n" +
                    "\tBiconditional: '≡' or ':'\n\tPropositions: letters 'A'-'Z'\n"
        );
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
                    throw new IllegalStateException("Couldn't parse expression fully. Remaining part: " + raw + ". Please check your syntax.");
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
            newNode.inverted = inverted != newNode.inverted;
            raw = raw.substring(getGroupEnd(raw)+1); //Trim to no longer include the first half
        } else { //Simple proposition
            newNode.initialize(raw.charAt(0), null, null, NodeType.PROPOSITION, inverted);
            raw = raw.substring(1);
        }
        return raw;
    }

}