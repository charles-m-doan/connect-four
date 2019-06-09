package connectfour.ai;

import java.util.ArrayList;
import java.util.LinkedList;

import connectfour.TimeTracker;

public class TrapSearch
	{
	private final int depthLimit; //The number of nodes deep the computer opponent is allowed to search.
	private TimeTracker timeTracker; //Tracks how long the search takes.
	private LinkedList<NodeBoard> primaryCaseQueue = new LinkedList<NodeBoard>(); //The node queue for a breadth first search of the problem space.
	private ArrayList<NodeBoard> subordinateCaseList = new ArrayList<NodeBoard>(); //The list of all generated children.

	//Note: "timeLimit" should be passed as milliseconds.
	public TrapSearch(int depthLimit, long timeLimit)
		{
		super();
		this.depthLimit = depthLimit;
		timeTracker = new TimeTracker(timeLimit);
		}

	public TrapSearch(int depthLimit, TimeTracker timeTracker)
		{
		super();
		this.depthLimit = depthLimit;
		this.timeTracker = timeTracker;
		}

	public void evaluateTraps(NodeBoard nb)
		{
		timeTracker.begin();
		primaryCaseQueue.add(nb);
		subordinateCaseList.add(nb);
		//Create search tree and evaluate primary case.
		while (!primaryCaseQueue.isEmpty())
			{
			NodeBoard parent = primaryCaseQueue.removeFirst();
			evaluatePrimaryCase(parent);
			if (parent.hasChildren())
				{
				primaryCaseQueue.addAll(parent.getChildren());
				subordinateCaseList.addAll(parent.getChildren());
				}
			}

		evaluateSubordinateCases();

		//Cleanup Memory
		//System.out.println("Total Children: " + NodeBoard.childCount);
		//nb.clearChildren();
		primaryCaseQueue = null;
		subordinateCaseList = null;
		}

	/* Primary Case: A parent node is a "trap" for a player if: Among it's
	 * children there is at least one threat node for that player, AND the
	 * player has turn priority on the child board state. (Meaning that the
	 * player would be able to take the available threat.) */
	private void evaluatePrimaryCase(NodeBoard node)
		{
		//Case Evaluation
		BoardEvaluation.evaluateThreats(node);
		int threatCount = node.countChildThreats(node.getPlayer());
		if (threatCount >= 1)
			{
			node.confirmTrap(node.getPlayer());
			}

		//Expansion conditions
		if (node.isTrap())
			{
			//If this node is a trap, it means there is a threat among the children.  Therefore no further searching is needed.
			return;
			}
		else if (limitsReached(node.depth))
			{
			//If the depth or time limits have been reached, no further searching is allowed.
			return;
			}
		else
			{
			//Expand this node for further searching.
			node.expandChildren();
			}
		}

	/* The subordinate cases are trap value alterations that depend on the trap
	 * values of other nodes. The method is recursive. If the trap value of any
	 * node in the tree changes, this could potentially merit a change in the
	 * trap value of the parent node--thereby requiring this method to be run
	 * again. */
	private void evaluateSubordinateCases()
		{
		for (int i = subordinateCaseList.size() - 1; i >= 0; i--)
			{
			evaluateSecondaryCase(subordinateCaseList.get(i));
			evaluateTertiaryCase(subordinateCaseList.get(i));
			}
		}

	/* Secondary case: The trap value of a node changes if all of it's children
	 * are traps. A node is a "settable trap" for the opponent if all of its
	 * children are traps for the opponent. */
	private void evaluateSecondaryCase(NodeBoard node)
		{
		if (node.hasChildren())
			{
			int childCount = node.countChildren();
			int opponent = node.getOpponent();
			int oTrapCount = node.countChildTraps(opponent);

			if (oTrapCount >= childCount)
				{
				node.confirmSettableTrap(opponent);
				}
			}
		}

	/* Tertiary case: The trap value of a node changes if any of its children
	 * are "settable traps." The node is a trap for the owner of the
	 * "settable trap." */
	private void evaluateTertiaryCase(NodeBoard node)
		{
		int settableCount = node.countChildSettables(node.getPlayer());
		if (settableCount >= 1)
			{
			node.confirmTrap(node.getPlayer());
			}
		}

	private boolean limitsReached(int depth)
		{
		if (depth >= depthLimit || timeTracker.isTimeUp())
			{
			return true;
			}
		return false;
		}
	}
