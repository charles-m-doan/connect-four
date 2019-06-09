package connectfour.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import connectfour.control.Game;
import connectfour.control.StateRegister;

public class GameDisplayPanel extends JPanel
	{
	private static final String[] MESSAGES = { "Connect Four", "A Project by C. Matthew Doan", "Click \"New Match\" to get started!", "Match Set. Click \"New Game!\"", "Game Completed: No winner Declared." };
	private static final Color GREY = new Color(150, 150, 150);
	private static final Color WHITE = new Color(255, 255, 255);
	private static final Color BLACK = new Color(0, 0, 0);
	private static final Color RED = new Color(255, 0, 0);
	private static final Color GREEN = new Color(0, 255, 0);
	private static final Color PALE_COPPER = new Color(220, 170, 55);
	private BufferedImage gameCanvas = null;

	public GameDisplayPanel()
		{
		super();
		}

	protected void paintComponent(Graphics g)
		{
		if (gameCanvas != null)
			{
			Rectangle r = g.getClipBounds();
			g.drawImage(gameCanvas, r.x, r.y, r.width, r.height, this);
			}
		}

	public void renderGameState(StateRegister states)
		{
		clearGameCanvas();
		if (states == null)
			{
			createWelcomeScreen();
			return;
			}
		Graphics2D g2D = gameCanvas.createGraphics();
		if (states.match == null)
			{
			drawXCenteredScaledMessage(MESSAGES[2], g2D, RED, BLACK, 0.8, 1.0 / 3.0);
			return;
			}
		if (states.game == null)
			{
			drawXCenteredScaledMessage(MESSAGES[3], g2D, RED, BLACK, 0.8, 1.0 / 3.0);
			return;
			}
		int width = this.getWidth();
		int height = this.getHeight();
		int xDist = width / 7;
		int yDist = height / 6;
		int indentX = xDist / 14;
		int indentY = yDist / 14;
		AffineTransform trans = AffineTransform.getTranslateInstance(0.0, (double) height);
		trans.scale(1.0, -1.0);
		g2D.setTransform(trans);
		drawGrid(g2D, width, height, xDist, yDist);
		drawPieces(g2D, states.game.getGrid(), states.match.p1Color, states.match.p2Color, xDist, yDist, indentX, indentY);
		if (!states.game.isActive())
			{
			drawOutcome(g2D, states.game, width, height, xDist, yDist);
			}
		}

	private void clearGameCanvas()
		{
		this.gameCanvas = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		}

	private void createWelcomeScreen()
		{
		Graphics2D g2D = gameCanvas.createGraphics();
		g2D.setColor(BLACK);
		g2D.fillRect(gameCanvas.getMinX(), gameCanvas.getMinY(), this.getWidth(), this.getHeight());
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawXCenteredScaledMessage(MESSAGES[0], g2D, RED, BLACK, 0.8, 1.0 / 3.0);
		drawXCenteredScaledMessage(MESSAGES[1], g2D, RED, BLACK, 0.75, 2.0 / 3.0);
		}

	/* Draws a string of variable length centered on the x axis,covering a % of
	 * the total x resolution as specified by "widthRatio". The height position
	 * of the string is a % of the total y resolution as specified by
	 * "heightRatio." (This is how far DOWN the message is.) The string will
	 * also be encapsulated in a solid color rectangle if the "rectangleColor"
	 * argument is non-null. */
	private void drawXCenteredScaledMessage(String message, Graphics2D g2D, Color rectangleColor, Color stringColor, double widthFraction, double heightRatio)
		{
		int width = this.getWidth();
		int height = this.getHeight();
		widthFraction = Math.min(widthFraction, 1.0);
		heightRatio = Math.min(heightRatio, 0.8);
		int fontSize = determineFontSize(message, g2D, width, widthFraction);
		g2D.setFont(new Font("Arial", Font.BOLD, fontSize));

		int messageWidth = g2D.getFontMetrics().stringWidth(message);
		int messageHeight = g2D.getFontMetrics().getHeight();
		int messageX = ((width - messageWidth) / 2);
		int messageY = (int) (heightRatio * height);
		if (rectangleColor != null)
			{
			g2D.setColor(rectangleColor);
			g2D.fillRect(messageX - (messageWidth / 10), messageY - messageHeight, messageWidth * 6 / 5, messageHeight * 3 / 2);
			}
		g2D.setColor(stringColor);
		g2D.drawString(message, messageX, messageY);
		} //End Method.

	private int determineFontSize(String message, Graphics2D g2D, double width, double widthFraction)
		{
		int fontSize = 32;
		double desiredWidth = (width * widthFraction);
		g2D.setFont(new Font("Arial", Font.BOLD, fontSize));
		double messageWidth = g2D.getFontMetrics().stringWidth(message);
		int pointer = 0;
		int[] sizes = { 0, 0, 0 };
		int fontAdjust = 32;
		while (messageWidth > desiredWidth || messageWidth < desiredWidth)
			{
			sizes[pointer] = fontSize;
			if (messageWidth > desiredWidth)
				{
				fontSize -= fontAdjust;
				}
			else if (messageWidth < desiredWidth)
				{
				fontSize += fontAdjust;
				}
			g2D.setFont(new Font("Arial", Font.BOLD, fontSize));
			messageWidth = g2D.getFontMetrics().stringWidth(message);
			if (sizes[0] == sizes[2]) //If this occurs, it means the font size has begun to alternate between values.  Approximation achieved.
				{
				if (fontAdjust <= 1)
					{
					break;
					}
				fontAdjust = fontAdjust / 2;
				}
			pointer++;
			pointer = pointer % 3;
			}
		return fontSize;
		}

	private void drawOutcome(Graphics2D g2D, Game game, int width, int height, int xDist, int yDist)
		{
		AffineTransform trans = AffineTransform.getTranslateInstance(0.0, (double) height);
		if (game.isDraw())
			{
			trans = AffineTransform.getTranslateInstance(0.0, 0.0);
			g2D.setTransform(trans);
			drawXCenteredScaledMessage(MESSAGES[4], g2D, BLACK, PALE_COPPER, 0.6, 1.0 / 3.0);
			}
		else
			{
			int[] winCoordinates = game.getWinCoordinates();
			g2D.setStroke(new BasicStroke(1 + ((width + height) / 200)));
			g2D.setColor(GREEN);
			trans.scale(1.0, -1.0);
			g2D.setTransform(trans);
			g2D.drawLine(winCoordinates[0] * xDist + xDist / 2, winCoordinates[1] * yDist + yDist / 2, winCoordinates[2] * xDist + xDist / 2, winCoordinates[3] * yDist + yDist / 2);

			trans = AffineTransform.getTranslateInstance(0.0, 0.0);
			g2D.setTransform(trans);
			drawXCenteredScaledMessage("Player " + game.getWinner() + " wins!", g2D, BLACK, GREEN, 0.6, 1.0 / 3.0);
			}
		} //End Method.

	private void drawGrid(Graphics2D g2D, int width, int height, int xDist, int yDist)
		{
		g2D.setColor(WHITE);
		g2D.fillRect(gameCanvas.getMinX(), gameCanvas.getMinY(), width, height);
		g2D.setColor(GREY);
		g2D.setStroke(new BasicStroke(1 + ((width + height) / 350)));
		for (int i = 0; i < 8; i++)
			{
			g2D.drawLine(i * xDist, 0, i * xDist, height);
			if (i < 7)
				{
				g2D.drawLine(0, i * yDist, width, i * yDist);
				}
			}
		} //End Method.

	private void drawPieces(Graphics2D g2D, int[][] grid, Color p1Color, Color p2Color, int xDist, int yDist, int indentX, int indentY)
		{
		for (int col = 0; col < 7; col++)
			{
			for (int row = 0; row < 6; row++)
				{
				switch (grid[col][row])
					{
					case 1: {
					g2D.setColor(p1Color);
					g2D.fillOval(col * xDist + indentX, row * yDist + indentY, xDist - 2 * indentX, yDist - 2 * indentY);
					break;
					}
					case 2: {
					g2D.setColor(p2Color);
					g2D.fillOval(col * xDist + indentX, row * yDist + indentY, xDist - 2 * indentX, yDist - 2 * indentY);
					break;
					}
					default: {
					break;
					}
					}
				}
			}
		} //End Method.
	}
