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
	static final int ruleset_Sym = 3;
	static final int lbrace_Sym = 4;
	static final int rbrace_Sym = 5;
	static final int colourset_Sym = 6;
	static final int property_Sym = 7;
	static final int semicolon_Sym = 8;
	static final int pointpoint_Sym = 9;
	static final int colon_Sym = 10;
	static final int rgblparen_Sym = 11;
	static final int comma_Sym = 12;
	static final int rparen_Sym = 13;
	static final int type_Sym = 14;
	static final int equal_Sym = 15;
	static final int lparen_Sym = 16;
	static final int point_Sym = 17;
	static final int lbrack_Sym = 18;
	static final int rbrack_Sym = 19;
	static final int if_Sym = 20;
	static final int else_Sym = 21;
	static final int var_Sym = 22;
	static final int plus_Sym = 23;
	static final int minus_Sym = 24;
	static final int equalequal_Sym = 25;
	static final int bangequal_Sym = 26;
	static final int greater_Sym = 27;
	static final int less_Sym = 28;
	static final int greaterequal_Sym = 29;
	static final int lessequal_Sym = 30;
	static final int barbar_Sym = 31;
	static final int star_Sym = 32;
	static final int slash_Sym = 33;
	static final int percent_Sym = 34;
	static final int andand_Sym = 35;
	static final int plusplus_Sym = 36;
	static final int minusminus_Sym = 37;
	static final int NOT_SYM = 38;
	// pragmas

	static final int maxT = 38;

	static final boolean T = true;
	static final boolean x = false;
	static final int minErrDist = 2;

	public static Token token;    // last recognized token   /* pdt */
	public static Token la;       // lookahead token
	static int errDist = minErrDist;

	static enum RelOpE{LT, GT, LE, GE, EQ, NE, UN};
static enum AddOpE{ADD, SUB, OR, UN};
static enum MulOpE{MUL, DIV, MOD, AND, UN};
static enum PostOpE{INC, DEC, UN};



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
		RuleSet();
		if (la.kind == colourset_Sym) {
			ColourSet();
		}
	}

	static void RuleSet() {
		Expect(ruleset_Sym);
		Expect(identifier_Sym);
		Expect(lbrace_Sym);
		Property();
		while (la.kind == property_Sym) {
			Property();
		}
		TypeSpec();
		while (la.kind == type_Sym) {
			TypeSpec();
		}
		Expect(rbrace_Sym);
	}

	static void ColourSet() {
		Expect(colourset_Sym);
		Expect(identifier_Sym);
		Expect(lbrace_Sym);
		while (la.kind == property_Sym) {
			PropertySpec();
		}
		Expect(rbrace_Sym);
	}

	static void Property() {
		Expect(property_Sym);
		Expect(identifier_Sym);
		Expect(semicolon_Sym);
	}

	static void TypeSpec() {
		Expect(type_Sym);
		Expect(identifier_Sym);
		Expect(colon_Sym);
		Expect(number_Sym);
		Expect(lbrace_Sym);
		while (StartOf(1)) {
			Statement();
		}
		Expect(rbrace_Sym);
	}

	static void PropertySpec() {
		Expect(property_Sym);
		Expect(identifier_Sym);
		Expect(lbrace_Sym);
		RangeSet();
		while (la.kind == number_Sym) {
			RangeSet();
		}
		Expect(rbrace_Sym);
	}

	static void RangeSet() {
		Expect(number_Sym);
		Expect(pointpoint_Sym);
		Expect(number_Sym);
		Expect(colon_Sym);
		Colour();
		Expect(semicolon_Sym);
	}

	static void Colour() {
		Expect(rgblparen_Sym);
		DoubleConst();
		Expect(comma_Sym);
		DoubleConst();
		Expect(comma_Sym);
		DoubleConst();
		Expect(rparen_Sym);
	}

	static void DoubleConst() {
		Expect(number_Sym);
		if (la.kind == point_Sym) {
			Get();
			Expect(number_Sym);
		}
	}

	static void Statement() {
		if (la.kind == lbrace_Sym) {
			Block();
		} else if (la.kind == identifier_Sym) {
			AssignCall();
		} else if (la.kind == if_Sym) {
			IfStatement();
		} else if (la.kind == var_Sym) {
			VarDeclaration();
		} else SynErr(39);
	}

	static void Block() {
		Expect(lbrace_Sym);
		while (StartOf(1)) {
			Statement();
		}
		Expect(rbrace_Sym);
	}

	static void AssignCall() {
		Designator();
		PostOpE T = PostOpE.UN;
		if (la.kind == equal_Sym) {
			Get();
			Expression();
		} else if (la.kind == plusplus_Sym || la.kind == minusminus_Sym) {
			T  = PostOp();
		} else if (la.kind == lparen_Sym) {
			Get();
			Arguments();
			Expect(rparen_Sym);
		} else SynErr(40);
		Expect(semicolon_Sym);
	}

	static void IfStatement() {
		Expect(if_Sym);
		Expect(lparen_Sym);
		Expression();
		Expect(rparen_Sym);
		Statement();
		if (la.kind == else_Sym) {
			Get();
			Statement();
		}
	}

	static void VarDeclaration() {
		Expect(var_Sym);
		OneVar();
		while (la.kind == comma_Sym) {
			Get();
			OneVar();
		}
		Expect(semicolon_Sym);
	}

	static void Designator() {
		Expect(identifier_Sym);
		if (la.kind == point_Sym || la.kind == lbrack_Sym) {
			if (la.kind == point_Sym) {
				Get();
				Attribute();
			} else {
				Get();
				Expression();
				Expect(rbrack_Sym);
			}
		}
	}

	static void Expression() {
		AddExp();
		RelOpE T = RelOpE.UN;
		if (StartOf(2)) {
			T  = RelOp();
			AddExp();
		}
	}

	static PostOpE PostOp() {
		PostOpE type;
		type = PostOpE.UN;	
		if (la.kind == plusplus_Sym) {
			Get();
			type = PostOpE.INC;	
		} else if (la.kind == minusminus_Sym) {
			Get();
			type = PostOpE.DEC;	
		} else SynErr(41);
		return type;
	}

	static void Arguments() {
		if (StartOf(3)) {
			Expression();
			while (la.kind == comma_Sym) {
				Get();
				Expression();
			}
		}
	}

	static void Attribute() {
		Designator();
	}

	static void OneVar() {
		Expect(identifier_Sym);
		if (la.kind == equal_Sym) {
			Get();
			Expression();
		}
	}

	static void AddExp() {
		if (la.kind == plus_Sym || la.kind == minus_Sym) {
			if (la.kind == plus_Sym) {
				Get();
			} else {
				Get();
			}
		}
		Term();
		AddOpE T = AddOpE.UN;
		while (la.kind == plus_Sym || la.kind == minus_Sym || la.kind == barbar_Sym) {
			T  = AddOp();
			Term();
		}
	}

	static RelOpE RelOp() {
		RelOpE type;
		type = RelOpE.UN;	
		switch (la.kind) {
		case equalequal_Sym: {
			Get();
			type = RelOpE.EQ;	
			break;
		}
		case bangequal_Sym: {
			Get();
			type = RelOpE.NE;	
			break;
		}
		case greater_Sym: {
			Get();
			type = RelOpE.GT;	
			break;
		}
		case less_Sym: {
			Get();
			type = RelOpE.LT;	
			break;
		}
		case greaterequal_Sym: {
			Get();
			type = RelOpE.GE;	
			break;
		}
		case lessequal_Sym: {
			Get();
			type = RelOpE.LE;	
			break;
		}
		default: SynErr(42); break;
		}
		return type;
	}

	static void Term() {
		Factor();
		MulOpE T = MulOpE.UN;
		while (StartOf(4)) {
			T  = MulOp();
			Factor();
		}
	}

	static AddOpE AddOp() {
		AddOpE type;
		type = AddOpE.UN;	
		if (la.kind == plus_Sym) {
			Get();
			type = AddOpE.ADD;	
		} else if (la.kind == minus_Sym) {
			Get();
			type = AddOpE.SUB;	
		} else if (la.kind == barbar_Sym) {
			Get();
			type = AddOpE.OR;	
		} else SynErr(43);
		return type;
	}

	static void Factor() {
		if (la.kind == identifier_Sym) {
			Designator();
			if (la.kind == lparen_Sym) {
				Get();
				Arguments();
				Expect(rparen_Sym);
			}
		} else if (la.kind == number_Sym) {
			Constant();
		} else if (la.kind == lparen_Sym) {
			Get();
			Expression();
			Expect(rparen_Sym);
		} else SynErr(44);
	}

	static MulOpE MulOp() {
		MulOpE type;
		type = MulOpE.UN;	
		if (la.kind == star_Sym) {
			Get();
			type = MulOpE.MUL;	
		} else if (la.kind == slash_Sym) {
			Get();
			type = MulOpE.DIV;	
		} else if (la.kind == percent_Sym) {
			Get();
			type = MulOpE.MOD;	
		} else if (la.kind == andand_Sym) {
			Get();
			type = MulOpE.AND;	
		} else SynErr(45);
		return type;
	}

	static void Constant() {
		DoubleConst();
	}



	public static void Parse() {
		la = new Token();
		la.val = "";
		Get();
		CAL();
		Expect(EOF_SYM);

	}

	private static boolean[][] set = {
		{T,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x},
		{x,x,T,x, T,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, T,x,T,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x},
		{x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,T,T,T, T,T,T,x, x,x,x,x, x,x,x,x},
		{x,T,T,x, x,x,x,x, x,x,x,x, x,x,x,x, T,x,x,x, x,x,x,T, T,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x},
		{x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, T,T,T,T, x,x,x,x}

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
			case 3: s = "\"ruleset\" expected"; break;
			case 4: s = "\"{\" expected"; break;
			case 5: s = "\"}\" expected"; break;
			case 6: s = "\"colourset\" expected"; break;
			case 7: s = "\"property\" expected"; break;
			case 8: s = "\";\" expected"; break;
			case 9: s = "\"..\" expected"; break;
			case 10: s = "\":\" expected"; break;
			case 11: s = "\"rgb(\" expected"; break;
			case 12: s = "\",\" expected"; break;
			case 13: s = "\")\" expected"; break;
			case 14: s = "\"type\" expected"; break;
			case 15: s = "\"=\" expected"; break;
			case 16: s = "\"(\" expected"; break;
			case 17: s = "\".\" expected"; break;
			case 18: s = "\"[\" expected"; break;
			case 19: s = "\"]\" expected"; break;
			case 20: s = "\"if\" expected"; break;
			case 21: s = "\"else\" expected"; break;
			case 22: s = "\"var\" expected"; break;
			case 23: s = "\"+\" expected"; break;
			case 24: s = "\"-\" expected"; break;
			case 25: s = "\"==\" expected"; break;
			case 26: s = "\"!=\" expected"; break;
			case 27: s = "\">\" expected"; break;
			case 28: s = "\"<\" expected"; break;
			case 29: s = "\">=\" expected"; break;
			case 30: s = "\"<=\" expected"; break;
			case 31: s = "\"||\" expected"; break;
			case 32: s = "\"*\" expected"; break;
			case 33: s = "\"/\" expected"; break;
			case 34: s = "\"%\" expected"; break;
			case 35: s = "\"&&\" expected"; break;
			case 36: s = "\"++\" expected"; break;
			case 37: s = "\"--\" expected"; break;
			case 38: s = "??? expected"; break;
			case 39: s = "invalid Statement"; break;
			case 40: s = "invalid AssignCall"; break;
			case 41: s = "invalid PostOp"; break;
			case 42: s = "invalid RelOp"; break;
			case 43: s = "invalid AddOp"; break;
			case 44: s = "invalid Factor"; break;
			case 45: s = "invalid MulOp"; break;
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
