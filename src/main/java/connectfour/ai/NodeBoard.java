package connectfour.ai;

import java.util.ArrayList;
import java.util.LinkedList;

import connectfour.logic.Board;
import connectfour.logic.Node;

public class NodeBoard extends Board
	{
	//Trap Search Related
	public static int childCount;
	public final int depth; //The current node depth in the search tree.
	public final NodeBoard parent; //The parent of this NodeBoard.
	private LinkedList<NodeBoard> children = null;
	private int trap = 0;
	private int[] traps = { 0, 0, 0, 0, 0, 0, 0 };

	//Non-search related
	private int[] threats = { 0, 0, 0, 0, 0, 0, 0 };
	private int[] ratings = { 0, 0, 0, 0, 0, 0, 0 };

	public NodeBoard(Board board)
		{
		super(board.firstPlayer);
		childCount = 0;
		this.depth = 0;
		this.parent = null;
		this.turn = board.getTurn();
		multiArrayCopy(board.getGrid(), this.grid);
		singleArrayCopy(board.getColumnHeights(), this.columnHeights);
		}

	public NodeBoard(NodeBoard parent, int move)
		{
		super(parent.firstPlayer);
		this.depth = (parent.depth + 1);
		this.parent = parent;
		this.turn = parent.getTurn();
		multiArrayCopy(parent.getGrid(), this.grid);
		singleArrayCopy(parent.getColumnHeights(), this.columnHeights);
		makeMove(move);
		}

	//Trap Related Methods ---------------------------------------------------------------------------------------------

	//Custom Mutators --------------------------------------------------------------------------------------------------

	public void confirmTrap(int player)
		{
		trap = player;
		//Update the parent's respective "traps" array value to reflect this node's trap value.
		if (hasParent())
			{
			int col = getPrevMove(); //This is the column taken to generate this board state.
			parent.registerChildTrapValue(col, trap);
			}
		}

	public void confirmSettableTrap(int player)
		{
		trap = -(player);
		//Update the parent's respective "traps" array value to reflect this node's trap value.
		if (hasParent())
			{
			int col = getPrevMove(); //This is the column taken to generate this board state.
			parent.registerChildTrapValue(col, trap);
			}
		}

	public void registerChildTrapValue(int col, int value)
		{
		traps[col] = value;
		}

	public void expandChildren()
		{
		children = new LinkedList<NodeBoard>();
		ArrayList<Node> legalNodes = getLegalNodes();
		for (int i = 0; i < legalNodes.size(); i++)
			{
			Node node = legalNodes.get(i);
			children.add(new NodeBoard(this, node.getColumn()));
			}
		childCount += children.size();
		}

	public void updateChildTraps()
		{
		if (hasChildren())
			{
			for (int i = 0; i < children.size(); i++)
				{
				NodeBoard child = children.get(i);
				if (child.getTrap() != 0)
					{
					traps[child.getPrevMove()] = child.getTrap();
					}
				}
			}
		}

	public void clearChildren()
		{
		if (!hasParent())
			{
			this.children = null;
			childCount = 0;
			}
		}

	//Custom Accessors -------------------------------------------------------------------------------------------------

	public boolean isTrap()
		{
		return (trap != 0);
		}

	public boolean hasChildren()
		{
		return !(children == null || children.size() <= 0);
		}

	public boolean hasParent()
		{
		return !(parent == null);
		}

	public int countChildren()
		{
		if (hasChildren())
			{
			return children.size();
			}
		else
			{
			return 0;
			}
		}

	public int countChildTraps(int player)
		{
		int count = 0;
		boolean playerSpecified = (player == 1 || player == 2);
		for (int i = 0; i < traps.length; i++)
			{
			int trap = traps[i];
			if (!playerSpecified)
				{
				if (trap == 1 || trap == 2)
					{
					count++;
					}
				}
			else if (trap == player)
				{
				count++;
				}
			}
		return count;
		}

	public int countChildSettables(int player)
		{
		int count = 0;
		for (int i = 0; i < traps.length; i++)
			{
			int trap = traps[i];
			if (trap == -(player))
				{
				count++;
				}
			}
		return count;
		}

	public double calculateTrapScore(int player)
		{
		double trapScore = 0.0;
		if (this.isTrap())
			{
			if (trap == player)
				{
				trapScore = 7.0;
				}
			}
		else if (hasChildren())
			{
			for (int i = 0; i < children.size(); i++)
				{
				trapScore += children.get(i).calculateTrapScore(player);
				}
			}
		return (trapScore / depth);
		}

	//Non-Trap Related Methods -----------------------------------------------------------------------------------------

	public void registerChildThreat(int col, int player)
		{
		if (threats[col] > 0 && threats[col] != player)
			{
			threats[col] = RankedNode.BOTH_THREAT;
			}
		else
			{
			threats[col] = player;
			}
		}

	public void addRating(int col, int rating)
		{
		this.ratings[col] += rating;
		}

	public int countChildThreats(int player)
		{
		int count = 0;
		boolean playerSpecified = (player == 1 || player == 2);
		for (int i = 0; i < threats.length; i++)
			{
			int threat = threats[i];
			//If a non-player id number is entered, then simply count the number of threats in general.
			if (!playerSpecified)
				{
				if (threat == RankedNode.BOTH_THREAT || threat == RankedNode.P1_THREAT || threat == RankedNode.P2_THREAT)
					{
					count++;
					}
				}
			//Otherwise count the number of threats for the specified player
			else if (threat == player || threat == RankedNode.BOTH_THREAT)
				{
				count++;
				}
			}
		return count;
		}

	public LinkedList<RankedNode> getRankedNodes()
		{
		LinkedList<RankedNode> rankedNodes = new LinkedList<RankedNode>();
		ArrayList<Node> legalNodes = getLegalNodes();
		for (int i = 0; i < legalNodes.size(); i++)
			{
			Node node = legalNodes.get(i);
			int threat = threats[node.getColumn()];
			int rating = ratings[node.getColumn()];
			int trap = traps[node.getColumn()];
			rankedNodes.add(new RankedNode(node, threat, rating, trap));
			}
		return rankedNodes;
		}

	public LinkedList<RankedNode> getTrapScoredRankedNodes()
		{
		LinkedList<RankedNode> rankedNodes = getRankedNodes();
		if (hasChildren())
			{
			for (int i = 0; i < rankedNodes.size(); i++)
				{
				double p1TrapScore = children.get(i).calculateTrapScore(1);
				double p2TrapScore = children.get(i).calculateTrapScore(2);
				rankedNodes.get(i).setP1TrapScore(p1TrapScore);
				rankedNodes.get(i).setP2TrapScore(p2TrapScore);
				}
			}
		return rankedNodes;
		}

	//Standard Getters and Setters -------------------------------------------------------------------------------

	public int getTrap()
		{
		return trap;
		}

	public LinkedList<NodeBoard> getChildren()
		{
		return children;
		}
	}
