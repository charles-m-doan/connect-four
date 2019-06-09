package connectfour.ai;

import java.util.ArrayList;

import connectfour.logic.Board;
import connectfour.logic.Node;

public class BoardEvaluation
	{
	private BoardEvaluation()
		{
		}

	//Rating Evaluation -----------------------------------------------------------------------

	public static void evaluateRatings(NodeBoard nb)
		{
		ArrayList<Node> legalNodes = nb.getLegalNodes();
		for (int i = 0; i < legalNodes.size(); i++)
			{
			evaluateNodeRating(nb, nb.getGrid(), legalNodes.get(i), nb.getPlayer());
			evaluateNodeRating(nb, nb.getGrid(), legalNodes.get(i), nb.getOpponent());
			}
		}

	private static void evaluateNodeRating(NodeBoard nb, int[][] grid, Node node, int player)
		{
		int opponent = Board.invertPID(player);
		evaluateNodeRatingByRow(nb, grid, node, player, opponent);
		evaluateNodeRatingByColumn(nb, grid, node, player, opponent);
		evaluateNodeRatingByUpSlope(nb, grid, node, player, opponent);
		evaluateNodeRatingByDownSlope(nb, grid, node, player, opponent);
		}

	private static void evaluateNodeRatingByRow(NodeBoard nb, int[][] grid, Node node, int player, int opponent)
		{
		int connect = 0;
		int rating = 0;
		int x = node.getColumn();
		int y = node.getRow();
		int start = Math.max(0, x - 3);
		int end = Math.min(3, x) + 1;
		for (int i = start; i < end; i++)
			{
			connect = 0;
			for (int j = 0; j < 4; j++)
				{
				if (grid[j + i][y] == player)
					{
					connect++;
					}
				else if (grid[j + i][y] == opponent)
					{
					connect = 0;
					break;
					}
				if (j >= 3 && connect <= 2)
					{
					rating += (int) Math.pow(2, connect);
					}
				}
			}
		nb.addRating(x, rating);
		}

	private static void evaluateNodeRatingByColumn(NodeBoard nb, int[][] grid, Node node, int player, int opponent)
		{
		int connect = 0;
		int rating = 0;
		int x = node.getColumn();
		int y = node.getRow();
		int start = 0;
		for (int i = 0; i < y; i++)
			{
			if (grid[x][i] == opponent)
				{
				start = i + 1;
				}
			}
		if (start > 2)
			{
			return;
			}
		connect = y - start;
		if (connect <= 2)
			{
			rating += (int) Math.pow(2, connect);
			}
		nb.addRating(x, rating);
		}

	private static void evaluateNodeRatingByUpSlope(NodeBoard nb, int[][] grid, Node node, int player, int opponent)
		{
		int connect = 0;
		int rating = 0;
		int x = node.getColumn();
		int y = node.getRow();
		int adjust = Math.min(x, y);
		int xStart = x - adjust;
		int yStart = y - adjust;
		int xEnd = xStart + Math.min(6 - xStart, 5 - yStart);
		int start = Math.max(xStart, x - 3);
		int end = Math.min(xEnd - 3, x);
		for (int i = start; i <= end; i++)
			{
			connect = 0;
			for (int j = 0; j < 4; j++)
				{
				if (grid[j + i][yStart + i + j - xStart] == player)
					{
					connect++;
					}
				else if (grid[j + i][yStart + i + j - xStart] == opponent)
					{
					connect = 0;
					break;
					}
				if (j >= 3 && connect <= 2)
					{
					rating += (int) Math.pow(2, connect);
					}
				}
			}
		nb.addRating(x, rating);
		}

	private static void evaluateNodeRatingByDownSlope(NodeBoard nb, int[][] grid, Node node, int player, int opponent)
		{
		int connect = 0;
		int rating = 0;
		int x = node.getColumn();
		int y = node.getRow();
		int adjust = Math.min(x, 5 - y);
		int xStart = x - adjust;
		int yStart = y + adjust;
		int xEnd = xStart + Math.min(6 - xStart, yStart);
		int start = Math.max(xStart, x - 3);
		int end = Math.min(xEnd - 3, x);
		for (int i = start; i <= end; i++)
			{
			connect = 0;
			for (int j = 0; j < 4; j++)
				{
				if (grid[j + i][yStart - i - j + xStart] == player)
					{
					connect++;
					}
				else if (grid[j + i][yStart - i - j + xStart] == opponent)
					{
					connect = 0;
					break;
					}
				if (j >= 3 && connect <= 2)
					{
					rating += (int) Math.pow(2, connect);
					}
				}
			}
		nb.addRating(x, rating);
		}

	//Threat Evaluation ------------------------------------------------------------------------

	public static void evaluateThreats(NodeBoard nb)
		{
		if (nb.getTurn() < 6)
			{
			return;
			}
		evaluatePlayerThreats(nb, nb.getPlayer());
		evaluatePlayerThreats(nb, nb.getOpponent());
		}

	private static void evaluatePlayerThreats(NodeBoard nb, int player)
		{
		int opponent = Board.invertPID(player);
		ArrayList<Node> legalNodes = nb.getLegalNodes();
		for (int i = 0; i < legalNodes.size(); i++)
			{
			evaluateNodeThreatByRow(nb, nb.getGrid(), legalNodes.get(i), player, opponent);
			evaluateNodeThreatByColumn(nb, nb.getGrid(), legalNodes.get(i), player, opponent);
			evaluateNodeThreatByUpSlope(nb, nb.getGrid(), legalNodes.get(i), player, opponent);
			evaluateNodeThreatByDownSlope(nb, nb.getGrid(), legalNodes.get(i), player, opponent);
			}
		}

	private static void evaluateNodeThreatByRow(NodeBoard nb, int[][] grid, Node node, int player, int opponent)
		{
		int connect = 0;
		int x = node.getColumn();
		int y = node.getRow();
		int start = Math.max(0, x - 3);
		int end = Math.min(3, x) + 1;
		for (int i = start; i < end; i++) //The range between max(0,x-3) and min(3,x) restricts the loop from checking columns that can't count toward a connect 4.
			{
			connect = 0;
			for (int j = 0; j < 4; j++)
				{
				if (grid[j + i][y] == player)
					{
					connect++;
					}
				else if (grid[j + i][y] == opponent)
					{
					connect = 0;
					break;
					}
				if (j >= 3 && connect >= 3)
					{
					nb.registerChildThreat(x, player);
					}
				}
			}
		}

	private static void evaluateNodeThreatByColumn(NodeBoard nb, int[][] grid, Node node, int player, int opponent)
		{
		int connect = 0;
		int x = node.getColumn();
		int y = node.getRow();
		int start = 0;
		for (int i = 0; i < y; i++) //finds the highest position of an opponent's piece in this column.
			{
			if (grid[x][i] == opponent)
				{
				start = i + 1;
				}
			}
		if (start > 2) //If start is greater than 2, then a connect 4 cannot be made vertically using this column.
			{
			return;
			}
		connect = y - start; //This is the number of stacked consecutive pieces for the player in this column.
		if (connect >= 3)
			{
			nb.registerChildThreat(x, player);
			}
		}

	private static void evaluateNodeThreatByUpSlope(NodeBoard nb, int[][] grid, Node node, int player, int opponent)
		{
		int connect = 0;
		int x = node.getColumn();
		int y = node.getRow();
		int adjust = Math.min(x, y);
		int xStart = x - adjust;
		int yStart = y - adjust;
		int xEnd = xStart + Math.min(6 - xStart, 5 - yStart);
		int start = Math.max(xStart, x - 3);
		int end = Math.min(xEnd - 3, x);
		for (int i = start; i <= end; i++)
			{
			connect = 0;
			for (int j = 0; j < 4; j++)
				{
				if (grid[j + i][yStart + i + j - xStart] == player)
					{
					connect++;
					}
				else if (grid[j + i][yStart + i + j - xStart] == opponent)
					{
					connect = 0;
					break;
					}
				if (j >= 3 && connect >= 3)
					{
					nb.registerChildThreat(x, player);
					}
				}
			}
		}

	private static void evaluateNodeThreatByDownSlope(NodeBoard nb, int[][] grid, Node node, int player, int opponent)
		{
		int connect = 0;
		int x = node.getColumn();
		int y = node.getRow();
		int adjust = Math.min(x, 5 - y);
		int xStart = x - adjust;
		int yStart = y + adjust;
		int xEnd = xStart + Math.min(6 - xStart, yStart);
		int start = Math.max(xStart, x - 3);
		int end = Math.min(xEnd - 3, x);
		for (int i = start; i <= end; i++)
			{
			connect = 0;
			for (int j = 0; j < 4; j++)
				{
				if (grid[j + i][yStart - i - j + xStart] == player)
					{
					connect++;
					}
				else if (grid[j + i][yStart - i - j + xStart] == opponent)
					{
					connect = 0;
					break;
					}
				if (j >= 3 && connect >= 3)
					{
					nb.registerChildThreat(x, player);
					}
				}
			}
		}
	}
