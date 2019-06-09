package connectfour.ai;

import java.util.Comparator;

import connectfour.logic.Node;

public class NodeOrder implements Comparator<RankedNode>
	{
	public final int player;

	public NodeOrder(int player)
		{
		this.player = player;
		}

	public int compare(RankedNode node1, RankedNode node2)
		{
		//THREAT COMPARISON ------------------------------------------------------------------
		/* Threats for the player take priority over threats for the opponent.
		 * Threats for both players take priority over all, and in general,
		 * threats take priority over traps. */
		if (node1.isThreat() && !node2.isThreat())
			{
			return Node.GREATER;
			}
		else if (node2.isThreat() && !node1.isThreat())
			{
			return Node.LESSER;
			}

		if ((node1.threat == RankedNode.BOTH_THREAT) && (node2.threat != RankedNode.BOTH_THREAT))
			{
			return Node.GREATER;
			}
		else if ((node2.threat == RankedNode.BOTH_THREAT) && (node1.threat != RankedNode.BOTH_THREAT))
			{
			return Node.LESSER;
			}

		if (player == 1)
			{
			if ((node1.threat == RankedNode.P1_THREAT) && (node2.threat != RankedNode.P1_THREAT))
				{
				return Node.GREATER;
				}
			else if ((node2.threat == RankedNode.P1_THREAT) && (node1.threat != RankedNode.P1_THREAT))
				{
				return Node.LESSER;
				}
			}
		if (player == 2)
			{
			if ((node1.threat == RankedNode.P2_THREAT) && (node2.threat != RankedNode.P2_THREAT))
				{
				return Node.GREATER;
				}
			else if ((node2.threat == RankedNode.P2_THREAT) && (node1.threat != RankedNode.P2_THREAT))
				{
				return Node.LESSER;
				}
			}

		//TRAP COMPARISON ---------------------------------------------------------------------
		/* Note that if the thread has reached this point it means that neither
		 * node is a threat. Settable traps for either player should be taken
		 * immediately, and ordinary traps for either player should be avoided
		 * if possible. However, the player should take its own traps before
		 * taking the traps of the opponent. */

		if (node1.isSettableTrap() && !node2.isSettableTrap())
			{
			return Node.GREATER;
			}
		else if (!node1.isSettableTrap() && node2.isSettableTrap())
			{
			return Node.LESSER;
			}

		if (!node1.isTrap() && node2.isTrap())
			{
			return Node.GREATER;
			}
		else if (node1.isTrap() && !node2.isTrap())
			{
			return Node.LESSER;
			}

		if (player == 1)
			{
			if ((node1.trap == RankedNode.P1_TRAP) && (node2.trap != RankedNode.P1_TRAP))
				{
				return Node.GREATER;
				}
			else if ((node1.trap != RankedNode.P1_TRAP) && (node2.trap == RankedNode.P1_TRAP))
				{
				return Node.LESSER;
				}
			}
		if (player == 2)
			{
			if ((node1.trap == RankedNode.P2_TRAP) && (node2.trap != RankedNode.P2_TRAP))
				{
				return Node.GREATER;
				}
			else if ((node1.trap != RankedNode.P2_TRAP) && (node2.trap == RankedNode.P2_TRAP))
				{
				return Node.LESSER;
				}
			}

		/* if (player == 1) { if(node1.p1TrapScore > node2.p1TrapScore) { return
		 * Node.GREATER; } else if (node1.p1TrapScore < node2.p1TrapScore) {
		 * return Node.LESSER; } else if(node1.p2TrapScore < node2.p2TrapScore)
		 * { return Node.GREATER; } else if(node1.p2TrapScore >
		 * node2.p2TrapScore) { return Node.LESSER; } } if (player == 2) {
		 * if(node1.p2TrapScore > node2.p2TrapScore) { return Node.GREATER; }
		 * else if (node1.p2TrapScore < node2.p2TrapScore) { return Node.LESSER;
		 * } else if(node1.p1TrapScore < node2.p1TrapScore) { return
		 * Node.GREATER; } else if(node1.p1TrapScore > node2.p1TrapScore) {
		 * return Node.LESSER; } } */

		return node1.compareTo(node2);
		}
	}
