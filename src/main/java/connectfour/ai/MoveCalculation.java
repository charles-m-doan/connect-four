package connectfour.ai;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import connectfour.TimeTracker;
import connectfour.control.Game;
import connectfour.control.Mediator;
import connectfour.control.StateRegister;

public class MoveCalculation
	{
	public static final long MINIMUM_MOVE_DURATION = 100;
	public static final long DEFAULT_MOVE_DURATION = 2000;
	public static final int DIFFICULTIES_COMPLETED = 6;

	private MoveCalculation()
		{
		}

	public static int determineMove(StateRegister states)
		{
		TimeTracker timeTracker = new TimeTracker(DEFAULT_MOVE_DURATION);
		int difficulty = Math.min(DIFFICULTIES_COMPLETED, states.match.getPlayerController(states.game.getPlayer()));
		int move = branchDifficulties(states.game, timeTracker, difficulty);
		long timeElapsed = timeTracker.getTimeElapsed(TimeTracker.MILLI);
		long difference = MINIMUM_MOVE_DURATION - timeElapsed;
		if (difference > 0)
			{
			Mediator.sleep(difference);
			}
		return move;
		}

	public static int branchDifficulties(Game game, TimeTracker timeTracker, int difficulty)
		{
		NodeBoard nb = new NodeBoard(game);
		TrapSearch trapSearch = null;
		switch (difficulty)
			{
			//Very Easy
			case 1: {
			BoardEvaluation.evaluateThreats(nb);
			//No evaluations. The computer moves completely at random.
			break;
			}
			//Easy
			case 2: {
			BoardEvaluation.evaluateThreats(nb);
			trapSearch = new TrapSearch(2, timeTracker);
			break;
			}
			//Normal
			case 3: {
			BoardEvaluation.evaluateThreats(nb);
			trapSearch = new TrapSearch(3, timeTracker);
			break;
			}
			//Hard
			case 4: {
			BoardEvaluation.evaluateThreats(nb);
			BoardEvaluation.evaluateRatings(nb);
			trapSearch = new TrapSearch(3, timeTracker);
			break;
			}
			//Very Hard
			case 5: {
			int moveCount = nb.getLegalMoveCount();
			int depth = 4 + (7 - moveCount); //The fewer legal moves there are, the less time it takes to consider additional moves.
			BoardEvaluation.evaluateThreats(nb);
			BoardEvaluation.evaluateRatings(nb);
			trapSearch = new TrapSearch(depth, timeTracker);
			break;
			}
			//Insane
			case 6: {
			int moveCount = nb.getLegalMoveCount();
			int depth = 4 + (int) Math.pow(2, (7 - moveCount)); //The fewer legal moves there are, the less time it takes to consider additional moves.
			BoardEvaluation.evaluateThreats(nb);
			BoardEvaluation.evaluateRatings(nb);
			trapSearch = new TrapSearch(depth, timeTracker);
			break;
			}
			default: {
			break;
			}
			}
		if (trapSearch != null)
			{
			trapSearch.evaluateTraps(nb);
			}
		LinkedList<RankedNode> moves = nb.getRankedNodes();
		nb.clearChildren();
		return getRandomGreatestMove(moves, nb.getPlayer()).getColumn();
		}

	public static RankedNode getRandomMove(LinkedList<RankedNode> nodes)
		{
		if (nodes.size() <= 1)
			{
			return nodes.get(0);
			}
		Random random = new Random(System.nanoTime());
		int index = random.nextInt(nodes.size());
		return nodes.get(index);
		}

	public static RankedNode getRandomGreatestMove(LinkedList<RankedNode> rankedNodes, int player)
		{
		if (rankedNodes.size() <= 1)
			{
			return rankedNodes.get(0);
			}
		NodeOrder order = new NodeOrder(player);
		Collections.sort(rankedNodes, order);
		LinkedList<RankedNode> sortedNodes = new LinkedList<RankedNode>(rankedNodes);
		LinkedList<RankedNode> greatestNodes = new LinkedList<RankedNode>();
		greatestNodes.add(sortedNodes.removeFirst());
		while (!sortedNodes.isEmpty())
			{
			//If there is another node tied for greatest, then add it to the list.
			if (order.compare(greatestNodes.getFirst(), sortedNodes.getFirst()) == 0)
				{
				greatestNodes.add(sortedNodes.removeFirst());
				}
			else
				{
				//Because the list is sorted there is no need to consider the remaining nodes.
				break;
				}
			}
		return getRandomMove(greatestNodes);
		}

	}
