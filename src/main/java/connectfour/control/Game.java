package connectfour.control;

import java.util.LinkedList;

import connectfour.logic.Board;
import connectfour.logic.Node;

public class Game extends Board
	{
	private boolean active = true; //Set to true if the game is active. (I.E. if the game is waiting on the move of a player)
	private boolean draw = false; //Set to true if the game was a draw.
	private int winner = 1; //Stores the player number of the winner of this game.
	LinkedList<Node> winNodes = new LinkedList<Node>(); //Stores the (column, row) pairs of the winning piece positions in the game matrix.

	public Game(Match match)
		{
		super(match.firstMover);
		}

	public boolean isActive()
		{
		return active;
		}

	public boolean isDraw()
		{
		return draw;
		}

	public boolean isWon()
		{
		return ((active == false) && (draw == false));
		}

	public int getWinner()
		{
		return winner;
		}

	//------------------------------------------------------------------------------------

	public void confirmDraw()
		{
		this.draw = true;
		this.active = false;
		}

	public void specifyWinner(int winner)
		{
		this.winner = winner;
		this.active = false;
		this.draw = false;
		}

	public LinkedList<Node> getWinNodes()
		{
		return winNodes;
		}

	public void clearWinNodes()
		{
		winNodes.clear();
		}

	public int[] getWinCoordinates()
		{
		int[] coordinates = { 0, 0, 0, 0 };
		if (winNodes != null)
			{
			coordinates[0] = winNodes.get(0).getColumn();
			coordinates[1] = winNodes.get(0).getRow();
			coordinates[2] = winNodes.get(winNodes.size() - 1).getColumn();
			coordinates[3] = winNodes.get(winNodes.size() - 1).getRow();
			}
		return coordinates;
		}

	}
