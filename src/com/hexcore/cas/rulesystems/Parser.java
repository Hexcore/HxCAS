package com.hexcore.cas.rulesystems;

import library.*;
import java.util.ArrayList;

import java.io.*;

public class Parser {
	static final int _EOF = 0;
	static final int _number = 1;
	static final int _identifier = 2;
	// terminals
	static final int EOF_SYM = 0;
	static final int number_Sym = 1;
	static final int identifier_Sym = 2;
	static final int quit_Sym = 3;
	static final int equal_Sym = 4;
	static final int semicolon_Sym = 5;
	static final int print_Sym = 6;
	static final int comma_Sym = 7;
	static final int barbar_Sym = 8;
	static final int andand_Sym = 9;
	static final int plus_Sym = 10;
	static final int minus_Sym = 11;
	static final int bang_Sym = 12;
	static final int true_Sym = 13;
	static final int false_Sym = 14;
	static final int lparen_Sym = 15;
	static final int rparen_Sym = 16;
	static final int star_Sym = 17;
	static final int slash_Sym = 18;
	static final int percent_Sym = 19;
	static final int less_Sym = 20;
	static final int lessequal_Sym = 21;
	static final int greater_Sym = 22;
	static final int greaterequal_Sym = 23;
	static final int equalequal_Sym = 24;
	static final int bangequal_Sym = 25;
	static final int NOT_SYM = 26;
	// pragmas

	static final int maxT = 26;

	static final boolean T = true;
	static final boolean x = false;
	static final int minErrDist = 2;

	public static Token token;    // last recognized token   /* pdt */
	public static Token la;       // lookahead token
	static int errDist = minErrDist;

	static Pair O;
static Table symTable;
static final int noType = 0, intType = 1, boolType = 2;

static class Entity
{
	public String name;
	public int value;
	public int type;
	
	public Entity(String name, int value, int type)
	{
		this.name = name;
		this.value = value;
		this.type = type;
	}
}

static class Table
{
	public ArrayList<Entity> symbols;
	
	
	public Table()
	{
		symbols = new ArrayList<Entity>();
	}
	
	public Entity getSymbol(String name)
	{
		for(int i = 0; i < symbols.size(); i++)
			if(symbols.get(i).name.equals(name))
				return symbols.get(i);
		return null;
	}
	
	public void addSymbol(Entity e)
	{
		symbols.add(e);
	}
}

static class Pair
{
	public int value;
	public int type;
}



static int toInt(boolean b) {
// return 0 or 1 according as b is false or true
  return b ? 1 : 0;
}

static boolean toBool(int i) {
// return false or true according as i is 0 or 1
  return i == 0 ? false : true;
}



	static void SynErr (int n) {
		if (errDist >= minErrDist) Errors.SynErr(la.line, la.col, n);
		errDist = 0;
	}

	public static void SemErr (String msg) {
		if (errDist >= minErrDist) Errors.Error(token.line, token.col, msg); /* pdt */
		errDist = 0;
	}

	public static void SemError (String msg) {
		if (errDist >= minErrDist) Errors.Error(token.line, token.col, msg); /* pdt */
		errDist = 0;
	}

	public static void Warning (String msg) { /* pdt */
		if (errDist >= minErrDist) Errors.Warn(token.line, token.col, msg);
		errDist = 0;
	}

	public static boolean Successful() { /* pdt */
		return Errors.count == 0;
	}

	public static String LexString() { /* pdt */
		return token.val;
	}

	public static String LookAheadString() { /* pdt */
		return la.val;
	}

	static void Get () {
		for (;;) {
			token = la; /* pdt */
			la = Scanner.Scan();
			if (la.kind <= maxT) { ++errDist; break; }

			la = token; /* pdt */
		}
	}

	static void Expect (int n) {
		if (la.kind==n) Get(); else { SynErr(n); }
	}

	static boolean StartOf (int s) {
		return set[s][la.kind];
	}

	static void ExpectWeak (int n, int follow) {
		if (la.kind == n) Get();
		else {
			SynErr(n);
			while (!StartOf(follow)) Get();
		}
	}

	static boolean WeakSeparator (int n, int syFol, int repFol) {
		boolean[] s = new boolean[maxT+1];
		if (la.kind == n) { Get(); return true; }
		else if (StartOf(repFol)) return false;
		else {
			for (int i=0; i <= maxT; i++) {
				s[i] = set[syFol][i] || set[repFol][i] || set[0][i];
			}
			SynErr(n);
			while (!s[la.kind]) Get();
			return StartOf(syFol);
		}
	}

	static void CAL() {
		symTable = new Table();
		while (la.kind == identifier_Sym || la.kind == print_Sym) {
			if (la.kind == print_Sym) {
				Print();
			} else {
				Assignment();
			}
		}
		Expect(quit_Sym);
	}

	static void Print() {
		Expect(print_Sym);
		O = Expression();
		if(O.type == boolType) IO.writeLine(toBool(O.value)); else  IO.writeLine(O.value);
		while (WeakSeparator(comma_Sym, 1, 2)) {
			O = Expression();
			if(O.type == boolType) IO.writeLine(toBool(O.value)); else  IO.writeLine(O.value);
		}
		while (!(la.kind == EOF_SYM || la.kind == semicolon_Sym)) {SynErr(27); Get();}
		Expect(semicolon_Sym);
	}

	static void Assignment() {
		String Name = "";
		Name = Variable();
		Expect(equal_Sym);
		O = Expression();
		while (!(la.kind == EOF_SYM || la.kind == semicolon_Sym)) {SynErr(28); Get();}
		Expect(semicolon_Sym);
		if(symTable.getSymbol(Name) == null) symTable.addSymbol(new Entity(Name, O.value, O.type)); else symTable.getSymbol(Name).value = O.value;
	}

	static String Variable() {
		String Name;
		Expect(identifier_Sym);
		Name = token.val;
		return Name;
	}

	static Pair Expression() {
		Pair O;
		Pair ExpO;
		O = AndExp();
		while (la.kind == barbar_Sym) {
			Get();
			ExpO = AndExp();
			if(ExpO.type != boolType || O.type != boolType) SemError("O.type Mismatch");
			if(!((new Integer(O.value)).equals(0)) || !((new Integer(ExpO.value)).equals(0))) O.value = 1; else O.value = 0;
			O.type = boolType;
			
		}
		return O;
	}

	static Pair AndExp() {
		Pair O;
		Pair ExpO;
		O = EqlExp();
		while (la.kind == andand_Sym) {
			Get();
			ExpO = EqlExp();
			if(ExpO.type != boolType || O.type != boolType) SemError("O.type Mismatch");
			if(!((new Integer(O.value)).equals(0)) && !((new Integer(ExpO.value)).equals(0))) O.value = 1; else O.value = 0;
			O.type = boolType;
			
		}
		return O;
	}

	static Pair EqlExp() {
		Pair O;
		Pair ExpO;
		O = RelExp();
		char type = 0;
		while (la.kind == equalequal_Sym || la.kind == bangequal_Sym) {
			type = EqlOp();
			ExpO = RelExp();
			if(ExpO.type != O.type) SemError("O.type Mismatch");
			switch(type)
			{
			case '=': if(O.value == ExpO.value) O.value = 1; else O.value = 0; break;
			case '!': if(O.value != ExpO.value) O.value = 1; else O.value = 0; break;
			};
			O.type = boolType;
			
		}
		return O;
	}

	static Pair RelExp() {
		Pair O;
		Pair ExpO;
		O = AddExp();
		int type = 0;
		if (StartOf(3)) {
			type = RelOp();
			ExpO = AddExp();
			if(ExpO.type != O.type) SemError("O.type Mismatch");
			switch(type)
			{	case 1: if(O.value < ExpO.value) O.value = 1; else O.value = 0; break;
			case 2: if(O.value <= ExpO.value) O.value = 1; else O.value = 0; break;
			case 3: if(O.value > ExpO.value) O.value = 1; else O.value = 0; break;
			case 4: if(O.value >= ExpO.value) O.value = 1; else O.value = 0; break;
			};
			O.type = boolType;
			
		}
		return O;
	}

	static char EqlOp() {
		char type;
		type = 0;
		if (la.kind == equalequal_Sym) {
			Get();
			type = '=';
		} else if (la.kind == bangequal_Sym) {
			Get();
			type = '!';
		} else SynErr(29);
		return type;
	}

	static Pair AddExp() {
		Pair O;
		Pair ExpO;
		O = MultExp();
		char type = 0;
		while (la.kind == plus_Sym || la.kind == minus_Sym) {
			type = AddOp();
			ExpO = MultExp();
			if(ExpO.type != intType || O.type != intType) SemError("O.type Mismatch");
			switch(type){case '+': O.value += ExpO.value; break; case '-': O.value -= ExpO.value; break;};
			
			
		}
		return O;
	}

	static int RelOp() {
		int type;
		type = 0;
		if (la.kind == less_Sym) {
			Get();
			type = 1;
		} else if (la.kind == lessequal_Sym) {
			Get();
			type = 2;
		} else if (la.kind == greater_Sym) {
			Get();
			type = 3;
		} else if (la.kind == greaterequal_Sym) {
			Get();
			type = 4;
		} else SynErr(30);
		return type;
	}

	static Pair MultExp() {
		Pair O;
		Pair ExpO;
		O = UnaryExp();
		char type = 0;
		while (la.kind == star_Sym || la.kind == slash_Sym || la.kind == percent_Sym) {
			type = MulOp();
			ExpO = UnaryExp();
			if(ExpO.type != intType || O.type != intType) SemError("O.type Mismatch");
			switch(type){case '*': O.value *= ExpO.value; break; case '/': if(ExpO.value != 0) O.value /= ExpO.value; break; case '%': O.value %= ExpO.value; break;};
			
		}
		return O;
	}

	static char AddOp() {
		char type;
		type = 0;
		if (la.kind == plus_Sym) {
			Get();
			type = '+';
		} else if (la.kind == minus_Sym) {
			Get();
			type = '-';
		} else SynErr(31);
		return type;
	}

	static Pair UnaryExp() {
		Pair O;
		O = new Pair();
		if (StartOf(4)) {
			O = Factor();
		} else if (la.kind == plus_Sym) {
			Get();
			O = UnaryExp();
			if(O.type != intType) SemError("O.typeMismatch");
		} else if (la.kind == minus_Sym) {
			Get();
			O = UnaryExp();
			if(O.type != intType) SemError("O.typeMismatch"); O.value = -O.value;
		} else if (la.kind == bang_Sym) {
			Get();
			O = UnaryExp();
			if(O.type != boolType) SemError("O.typeMismatch"); if((new Integer(O.value)).equals(0)) O.value = 1; else O.value = 0;
		} else SynErr(32);
		return O;
	}

	static char MulOp() {
		char type;
		type = 0;
		if (la.kind == star_Sym) {
			Get();
			type = '*';
		} else if (la.kind == slash_Sym) {
			Get();
			type = '/';
		} else if (la.kind == percent_Sym) {
			Get();
			type = '%';
		} else SynErr(33);
		return type;
	}

	static Pair Factor() {
		Pair O;
		int Value = 0; String Name = ""; O = new Pair();
		if (la.kind == identifier_Sym) {
			Name = Variable();
			O.value = symTable.getSymbol(Name).value; O.type = symTable.getSymbol(Name).type;
		} else if (la.kind == number_Sym) {
			Value = Number();
			O.value = Value; O.type = intType;
		} else if (la.kind == true_Sym) {
			Get();
			O.value = 1; O.type = boolType;
		} else if (la.kind == false_Sym) {
			Get();
			O.value = 0; O.type = boolType;
		} else if (la.kind == lparen_Sym) {
			Get();
			O = Expression();
			Expect(rparen_Sym);
		} else SynErr(34);
		return O;
	}

	static int Number() {
		int Value;
		Expect(number_Sym);
		Value = Integer.parseInt(token.val);
		return Value;
	}



	public static void Parse() {
		la = new Token();
		la.val = "";
		Get();
		CAL();
		Expect(EOF_SYM);

	}

	private static boolean[][] set = {
		{T,x,x,x, x,T,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x},
		{x,T,T,x, x,x,x,x, x,x,T,T, T,T,T,T, x,x,x,x, x,x,x,x, x,x,x,x},
		{x,x,x,x, x,T,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x},
		{x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, T,T,T,T, x,x,x,x},
		{x,T,T,x, x,x,x,x, x,x,x,x, x,T,T,T, x,x,x,x, x,x,x,x, x,x,x,x}

	};

} // end Parser

/* pdt - considerable extension from here on */

class ErrorRec {
	public int line, col, num;
	public String str;
	public ErrorRec next;

	public ErrorRec(int l, int c, String s) {
		line = l; col = c; str = s; next = null;
	}

} // end ErrorRec

class Errors {

	public static int count = 0;                                     // number of errors detected
	public static String errMsgFormat = "file {0} : ({1}, {2}) {3}"; // 0=file 1=line, 2=column, 3=text
	static String fileName = "";
	static String listName = "";
	static boolean mergeErrors = false;
	static PrintWriter mergedList;

	static ErrorRec first = null, last;
	static boolean eof = false;

	static String getLine() {
		char ch, CR = '\r', LF = '\n';
		int l = 0;
		StringBuffer s = new StringBuffer();
		ch = (char) Buffer.Read();
		while (ch != Buffer.EOF && ch != CR && ch != LF) {
			s.append(ch); l++; ch = (char) Buffer.Read();
		}
		eof = (l == 0 && ch == Buffer.EOF);
		if (ch == CR) {  // check for MS-DOS
			ch = (char) Buffer.Read();
			if (ch != LF && ch != Buffer.EOF) Buffer.pos--;
		}
		return s.toString();
	}

	static private String Int(int n, int len) {
		String s = String.valueOf(n);
		int i = s.length(); if (len < i) len = i;
		int j = 0, d = len - s.length();
		char[] a = new char[len];
		for (i = 0; i < d; i++) a[i] = ' ';
		for (j = 0; i < len; i++) {a[i] = s.charAt(j); j++;}
		return new String(a, 0, len);
	}

	static void display(String s, ErrorRec e) {
		mergedList.print("**** ");
		for (int c = 1; c < e.col; c++)
			if (s.charAt(c-1) == '\t') mergedList.print("\t"); else mergedList.print(" ");
		mergedList.println("^ " + e.str);
	}

	public static void Init (String fn, String dir, boolean merge) {
		fileName = fn;
		listName = dir + "listing.txt";
		mergeErrors = merge;
		if (mergeErrors)
			try {
				mergedList = new PrintWriter(new BufferedWriter(new FileWriter(listName, false)));
			} catch (IOException e) {
				Errors.Exception("-- could not open " + listName);
			}
	}

	public static void Summarize () {
		if (mergeErrors) {
			ErrorRec cur = first;
			Buffer.setPos(0);
			int lnr = 1;
			String s = getLine();
			while (!eof) {
				mergedList.println(Int(lnr, 4) + " " + s);
				while (cur != null && cur.line == lnr) {
					display(s, cur); cur = cur.next;
				}
				lnr++; s = getLine();
			}
			if (cur != null) {
				mergedList.println(Int(lnr, 4));
				while (cur != null) {
					display(s, cur); cur = cur.next;
				}
			}
			mergedList.println();
			mergedList.println(count + " errors detected");
			mergedList.close();
		}
		switch (count) {
			case 0 : System.out.println("Parsed correctly"); break;
			case 1 : System.out.println("1 error detected"); break;
			default: System.out.println(count + " errors detected"); break;
		}
		if (count > 0 && mergeErrors) System.out.println("see " + listName);
	}

	public static void storeError (int line, int col, String s) {
		if (mergeErrors) {
			ErrorRec latest = new ErrorRec(line, col, s);
			if (first == null) first = latest; else last.next = latest;
			last = latest;
		} else printMsg(fileName, line, col, s);
	}

	public static void SynErr (int line, int col, int n) {
		String s;
		switch (n) {
			case 0: s = "EOF expected"; break;
			case 1: s = "number expected"; break;
			case 2: s = "identifier expected"; break;
			case 3: s = "\"quit\" expected"; break;
			case 4: s = "\"=\" expected"; break;
			case 5: s = "\";\" expected"; break;
			case 6: s = "\"print\" expected"; break;
			case 7: s = "\",\" expected"; break;
			case 8: s = "\"||\" expected"; break;
			case 9: s = "\"&&\" expected"; break;
			case 10: s = "\"+\" expected"; break;
			case 11: s = "\"-\" expected"; break;
			case 12: s = "\"!\" expected"; break;
			case 13: s = "\"true\" expected"; break;
			case 14: s = "\"false\" expected"; break;
			case 15: s = "\"(\" expected"; break;
			case 16: s = "\")\" expected"; break;
			case 17: s = "\"*\" expected"; break;
			case 18: s = "\"/\" expected"; break;
			case 19: s = "\"%\" expected"; break;
			case 20: s = "\"<\" expected"; break;
			case 21: s = "\"<=\" expected"; break;
			case 22: s = "\">\" expected"; break;
			case 23: s = "\">=\" expected"; break;
			case 24: s = "\"==\" expected"; break;
			case 25: s = "\"!=\" expected"; break;
			case 26: s = "??? expected"; break;
			case 27: s = "this symbol not expected in Print"; break;
			case 28: s = "this symbol not expected in Assignment"; break;
			case 29: s = "invalid EqlOp"; break;
			case 30: s = "invalid RelOp"; break;
			case 31: s = "invalid AddOp"; break;
			case 32: s = "invalid UnaryExp"; break;
			case 33: s = "invalid MulOp"; break;
			case 34: s = "invalid Factor"; break;
			default: s = "error " + n; break;
		}
		storeError(line, col, s);
		count++;
	}

	public static void SemErr (int line, int col, int n) {
		storeError(line, col, ("error " + n));
		count++;
	}

	public static void Error (int line, int col, String s) {
		storeError(line, col, s);
		count++;
	}

	public static void Warn (int line, int col, String s) {
		storeError(line, col, s);
	}

	public static void Exception (String s) {
		System.out.println(s);
		System.exit(1);
	}

	private static void printMsg(String fileName, int line, int column, String msg) {
		StringBuffer b = new StringBuffer(errMsgFormat);
		int pos = b.indexOf("{0}");
		if (pos >= 0) { b.replace(pos, pos+3, fileName); }
		pos = b.indexOf("{1}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, line); }
		pos = b.indexOf("{2}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, column); }
		pos = b.indexOf("{3}");
		if (pos >= 0) b.replace(pos, pos+3, msg);
		System.out.println(b.toString());
	}

} // end Errors
