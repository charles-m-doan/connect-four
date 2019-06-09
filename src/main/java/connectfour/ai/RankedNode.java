package connectfour.ai;

import connectfour.logic.Node;

public class RankedNode extends Node
	{
	//Constants
	public static final int P1_THREAT = 1;
	public static final int P2_THREAT = 2;
	public static final int BOTH_THREAT = 3;

	public static final int P2_SETTABLE = -2;
	public static final int P1_SETTABLE = -1;
	public static final int NOT_TRAP = 0;
	public static final int P1_TRAP = 1;
	public static final int P2_TRAP = 2;

	/* A threat is a node that, if taken by the player to whom it belongs, will
	 * result in a win for that player. 0 = not a threat, 1 = player 1 threat, 2
	 * = player 2 threat, and 3 = a threat for both players. */
	protected int threat = 0;

	/* An "opportunity" rating for the node. It is a composite sum of how many
	 * potential connect 4's it blocks and contributes to. For any given
	 * potential connect 4, the more pieces that are already present, the more
	 * highly the node is scored. */
	protected int rating = 0;

	/* A trap is a node that, if taken by the opponent of the player to whom it
	 * belongs, will eventually lead to a loss for the opponent. The player to
	 * whom the trap belongs will be referred to as the "owner." A node is a
	 * trap if it's children (i.e. the next set of legal moves after the node is
	 * taken) follow either of two rules. 1st Rule: Among the children there
	 * exists at least one threat node belonging to the owner at that board
	 * state, AND the "owner" is the "player." 2nd Rule: Every child node is a
	 * trap for the owner of the potential trap. */
	protected int trap = 0;

	protected double p1TrapScore = 0.0;
	protected double p2TrapScore = 0.0;

	public RankedNode(int column, int row)
		{
		super(column, row);
		}

	public RankedNode(Node node, int threat, int rating, int trap)
		{
		super(node.getColumn(), node.getRow());
		this.threat = threat;
		this.rating = rating;
		this.trap = trap;
		}

	//Getters and Setters --------------------------------------------------------------------

	public int getThreat()
		{
		return threat;
		}

	public int getRating()
		{
		return rating;
		}

	public int getTrap()
		{
		return trap;
		}

	//Trap Score related-------------------------------------

	public double getP1TrapScore()
		{
		return p1TrapScore;
		}

	public void setP1TrapScore(double p1TrapScore)
		{
		this.p1TrapScore = p1TrapScore;
		}

	public double getP2TrapScore()
		{
		return p2TrapScore;
		}

	public void setP2TrapScore(double p2TrapScore)
		{
		this.p2TrapScore = p2TrapScore;
		}

	//Custom Accessors -----------------------------------------------------------------------

	public boolean isThreat()
		{
		return (threat != 0);
		}

	public boolean isTrap()
		{
		return (trap != 0);
		}

	public boolean isSettableTrap()
		{
		return (trap == P1_SETTABLE || trap == P2_SETTABLE);
		}

	//Override Methods -----------------------------------------------------------------------

	public int compareTo(RankedNode node) throws ClassCastException
		{
		if (!(node instanceof RankedNode))
			{
			throw new ClassCastException("A MoveNode object expected.");
			}
		if (rating > node.rating)
			{
			return GREATER;
			}
		else if (rating < node.rating)
			{
			return LESSER;
			}
		return EQUAL;
		}

	public String toString()
		{
		return "[" + column + "," + row + "] Threat: " + threat + ", Rating: " + rating + ", Trap: " + trap;
		}
	}
