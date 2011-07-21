package com.hexcore.cas.rulesystems;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import com.hexcore.cas.model.Cell;

public class HexcoreVM
{
	//CPU Functional Registers
	int					PC;
	String 				IR;
	
	//Memory
	Stack<Integer>		oStack;
	ArrayList<String>	instructions;
	Cell				currentCell;

	
	//CPU Operational Registers
	int					SP;			//Stack Pointer
	boolean				end;
	
	private enum OpCodes{	LDC, LDA, LDV, STO, DSP,
							ADD, SUB, MUL, DIV, REM,
							CEQ, CNE, CGT, CLT, CLE, CGE,
							AND, OR, NOT,
							BRN, BZE,
							END
						};
	
	
	
	public HexcoreVM()
	{
		oStack = new Stack<Integer>();
		SP = 0;
		end = false;
	}
	
	public void loadRules(String filename)
	{
		try
		{
			Scanner in = new Scanner(new File(filename));
			
			while(in.hasNext())
				instructions.add(in.nextLine());
			
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public void run(Cell cell, Cell[] neighbours)
	{
		PC = 0;
		end = false;
		
		int[] v;
		int[][] n;
		v = cell.getValues();
		n = new int[neighbours.length][v.length];
		
		for(int i = 0; i < neighbours.length; i++)
		{
			n[i] = neighbours[i].getValues();
		}		
		
		while(!end)
		{
			IR = instructions.get(PC);
			execute();
			PC++;
		}
	}
	
	private void execute()
	{
		int index = IR.indexOf(' ');
		
		String operation;
		String operands;
		
		if(index != -1)
		{
			operation = IR.substring(0, index);
			operands = IR.substring(index + 1);
		}
		else
		{
			operation = IR;
		}
		
		
		switch(OpCodes.valueOf(operation))
		{
			case LDC:	break;
			case LDA:	break;
			case LDV:	break;
			case STO:	break;
			case DSP:	break;
			case ADD:	break;
			case SUB:	break;
			case MUL:	break;
			case DIV:	break;
			case REM:	break;
			case CEQ:	break;
			case CNE:	break;
			case CGT:	break;
			case CLT:	break;
			case CLE:	break;
			case CGE:	break;
			case AND:	break;
			case OR:	break;
			case NOT:	break;
			case BRN:	break;
			case BZE:	break;
			
			case END:	end = true;
						break;
		}
	}
}
