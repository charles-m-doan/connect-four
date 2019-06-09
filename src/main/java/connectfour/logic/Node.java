package connectfour.logic;

public class Node implements Comparable<Node>
	{
	//NOTE: These values are set for "descending" order.  That is, the greatest elements should appear earliest in a sorted list.
	public final static int GREATER = -1;
	public final static int LESSER = 1;
	public final static int EQUAL = 0;

	protected int column; //The column of the grid (i.e. the x coordinate).
	protected int row; //The row of the grid (i.e. the y coordinate).

	public Node(int column, int row)
		{
		super();
		this.column = column;
		this.row = row;
		}

	public int getColumn()
		{
		return column;
		}

	public void setColumn(int column)
		{
		this.column = column;
		}

	public int getRow()
		{
		return row;
		}

	public void setRow(int row)
		{
		this.row = row;
		}

	public String toString()
		{
		return "[" + column + ", " + row + "]";
		}

	public int compareTo(Node node) throws ClassCastException
		{
		if (!(node instanceof Node))
			{
			throw new ClassCastException("A MoveNode object expected.");
			}
		if (column < node.column)
			{
			return GREATER;
			}
		else if (column > node.column)
			{
			return LESSER;
			}
		return EQUAL;
		}
	}