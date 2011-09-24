package com.hexcore.cas.rulesystems;

import java.util.ArrayList;

public class CodeGen
{
	public static ArrayList<byte[]> code = new ArrayList<byte[]>();
	
	public static void loadConst()
	{
		byte op = (byte) HexcoreVM.OpCodes.LDC.ordinal();
		byte[] instr = new byte[1];
		instr[0] = op;
		code.add(instr);	
	}
}
