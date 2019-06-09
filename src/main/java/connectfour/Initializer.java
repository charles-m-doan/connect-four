package connectfour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintWriter;

import connectfour.control.Mediator;
import connectfour.control.StateRegister;
import connectfour.ui.InterfaceFrame;

public class Initializer
	{

	public static void main(String[] args)
		{
		StateRegister states = null;
		PrintWriter commandOut = null;
		BufferedReader commandIn = null;
		InterfaceFrame interfaceFrame = null;
		Mediator mediator = null;

		try
			{
			PipedWriter commandPipeOut = new PipedWriter();
			PipedReader commandPipeIn = new PipedReader(commandPipeOut);
			commandOut = new PrintWriter(commandPipeOut);
			commandIn = new BufferedReader(commandPipeIn);
			}
		catch (IOException ex)
			{
			ex.printStackTrace();
			System.exit(0);
			}

		interfaceFrame = new InterfaceFrame(commandOut);
		mediator = new Mediator(states, commandIn, interfaceFrame);
		interfaceFrame.setVisible(true);
		mediator.start();
		}
	}