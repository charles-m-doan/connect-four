package connectfour;

import java.text.DecimalFormat;
import java.util.LinkedList;

import connectfour.ai.BoardEvaluation;
import connectfour.ai.NodeBoard;
import connectfour.ai.RankedNode;
import connectfour.ai.TrapSearch;
import connectfour.control.Match;
import connectfour.control.StateRegister;

public class DebugMessages
	{

	private DebugMessages()
		{
		}

	public static void printVals(StateRegister states)
		{
		if (states == null || !states.isGameEstablished())
			{
			return;
			}
		NodeBoard nb = new NodeBoard(states.game);
		BoardEvaluation.evaluateThreats(nb);
		BoardEvaluation.evaluateRatings(nb);
		TrapSearch trapSearch = new TrapSearch(5, 2000);
		trapSearch.evaluateTraps(nb);
		System.out.println("Turn: " + states.game.getTurn() + " Player " + states.game.getPlayer());
		//System.out.println(nodeStatsToString(nb.getRankedNodes()) + Match.LINE_SEPARATOR);
		System.out.println(nodeStatsToString(nb.getTrapScoredRankedNodes()) + Match.LINE_SEPARATOR);
		nb.clearChildren();
		}

	public static String nodeStatsToString(LinkedList<RankedNode> nodes)
		{
		int[] threats = new int[7];
		int[] ratings = new int[7];
		int[] traps = new int[7];
		double[] p1TrapScores = new double[7];
		double[] p2TrapScores = new double[7];
		for (int i = 0; i < 7; i++)
			{
			threats[i] = -1;
			ratings[i] = -1;
			traps[i] = -1;
			p1TrapScores[i] = -1.0;
			p2TrapScores[i] = -1.0;
			}
		for (int i = 0; i < nodes.size(); i++)
			{
			threats[nodes.get(i).getColumn()] = nodes.get(i).getThreat();
			ratings[nodes.get(i).getColumn()] = nodes.get(i).getRating();
			traps[nodes.get(i).getColumn()] = nodes.get(i).getTrap();
			p1TrapScores[nodes.get(i).getColumn()] = nodes.get(i).getP1TrapScore();
			p2TrapScores[nodes.get(i).getColumn()] = nodes.get(i).getP2TrapScore();
			}
		String string = "Threats: " + intArrayToString(threats);
		string += Match.LINE_SEPARATOR + "Ratings: " + intArrayToString(ratings);
		string += Match.LINE_SEPARATOR + "  Traps: " + intArrayToString(traps);
		string += Match.LINE_SEPARATOR + "   P1TS: " + doubleArrayToString(p1TrapScores);
		string += Match.LINE_SEPARATOR + "   P2TS: " + doubleArrayToString(p2TrapScores);
		return string;
		}

	public static String intArrayToString(int[] a)
		{
		String aStr = "[ ";
		for (int i = 0; i < a.length; i++)
			{
			aStr += a[i] + " ";
			}
		aStr += "]";
		return aStr;
		}

	public static String doubleArrayToString(double[] a)
		{
		DecimalFormat df = new DecimalFormat("##.####");
		String aStr = "[ ";
		for (int i = 0; i < a.length; i++)
			{
			aStr += df.format(a[i]) + " ";
			}
		aStr += "]";
		return aStr;
		}
	}
