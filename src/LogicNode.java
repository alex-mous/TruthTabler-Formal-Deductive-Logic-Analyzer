import java.util.Map;

public class LogicNode {
    LogicNode left; //Left node pointer
    LogicNode right; //Right node pointer
    Character val; //Node value - depends on type
    NodeType type; //Type of node: OPERATOR to run operation on two sub nodes, or PROPOSITION to return the node's value as a proposition ("t"/"f")
    boolean inverted; //Whether this node is inverted or not. Used for PROPOSITION node

    /**
     * Construct a new logic node with value, left and right sub-nodes, type and inversion
     *
     * @param val Value of the node. Either proposition character, operator, or null for group nodes
     * @param left Pointer to left sub-node (required)
     * @param right Pointer to right sub-node (or null if group node)
     * @param type Type of node as given in NodeTypes
     * @param inverted Node inversion (for proposition and group nodes only)
     */
    public LogicNode(Character val, LogicNode left, LogicNode right, NodeType type, boolean inverted) { //Construct a new node
        initialize(val, left, right, type, inverted);
    }

    public LogicNode() {} //Default constructor

    /**
     * Initialize the logic node with value, left and right sub-nodes, type and inversion
     *
     * @param val Value of the node. Either proposition character, operator, or null for group nodes
     * @param left Pointer to left sub-node (required)
     * @param right Pointer to right sub-node (or null if group node)
     * @param type Type of node as given in NodeTypes
     * @param inverted Node inversion (for proposition and group nodes only)
     */
    public void initialize(Character val, LogicNode left, LogicNode right, NodeType type, boolean inverted) {
        this.left = left;
        this.right = right;
        this.val = val;
        this.type = type;
        this.inverted = inverted;
    }

    /**
     * Initialize the logic node with value, left and right sub-nodes, type and inversion from another node
     *
     * @param node Node to copy from
     */
    public void copyFrom(LogicNode node) {
        this.left = node.left;
        this.right = node.right;
        this.val = node.val;
        this.type = node.type;
        this.inverted = node.inverted;
    }

    /**
     * Get the logical valuation of this node (recursively evaluates sub-nodes)
     *
     * @param propMap Map of proposition characters to truth values
     * @throws IllegalArgumentException If the proposition is not defined
     * @return Truth value of this node
     */
    public boolean evaluate(Map<Character, Boolean> propMap) {
        if (this.type == NodeType.OPERATOR) { //Run operator on sub nodes
            return this.inverted != this.runOperator(propMap);
        } else { //Otherwise, this is a proposition
            if (!propMap.containsKey(this.val)) {
                throw new IllegalArgumentException("Proposition " + this.val + " is not defined");
            }
            return this.inverted != propMap.get(this.val);
        }
    }

    /**
     * Run the operator on this node (recursively evaluates sub-nodes, operator stored in val). Used for when this.type == NodeTypes.OPERATOR
     *
     * @param propMap Map of proposition characters to truth values
     * @throws UnsupportedOperationException If the operator is not supported
     * @return Truth value of this node based on the operator
     */
    private boolean runOperator(Map<Character, Boolean> propMap) {
        boolean propA = this.left.evaluate(propMap);
        boolean propB = this.right.evaluate(propMap);
        switch (this.val) { //The val is the operator if this is an OPERATOR node
            case 'v': //Logical AND (inclusive)
                return propA || propB;
            case '&': //Logical AND
                return propA && propB;
            case '⊃': //Logical THEN (conditional)
                return !propA || propB;
            case '≡': //Logical EQUALS (biconditional)
                return propA == propB;
            default:
                throw new UnsupportedOperationException("Logical operator " + this.val + " is not supported");
        }
    }
}
