package com.hexcore.cas.rulesystems;

import java.util.Stack;

/**
 * Class SymbolTable

 * @authors Karl Zoller
 */

public class SymbolTable
{
	private Stack<Scope> scopeStack;
	
	public SymbolTable()
	{
		scopeStack = new Stack<SymbolTable.Scope>();
	}
	
	public void pushScope()
	{
		Scope newScope = new Scope();
		
		if(scopeStack.size() > 0)
			newScope.outer = scopeStack.peek();
		else
			newScope.outer = null;
		
		newScope.firstEntry = null;
		scopeStack.push(newScope);
	}
	
	public void popScope()
	{
		scopeStack.pop();
	}
	
	public TableEntry find(String name)
	{
		Scope currentScope = scopeStack.peek();
		
		while(currentScope != null)
		{
			TableEntry currentEntry = currentScope.firstEntry;
			
			while(currentEntry != null)
			{
				if(currentEntry.name.equals(name))
				{
					return currentEntry; 
				}
				currentEntry = currentEntry.nextEntry;
			}
			currentScope = currentScope.outer;
		}
		TableEntry noEntry = new TableEntry();
		noEntry.type = TableEntry.Type.NONE;
		return noEntry;
	}
	
	public void prepare()
	{
		TableEntry selfEntry = new TableEntry();
		selfEntry.type = TableEntry.Type.CELL;
		selfEntry.kind = TableEntry.Kind.CELL;
		selfEntry.name = "self";
		selfEntry.offset = 1;
		insert(selfEntry);
		
		TableEntry typeEntry = new TableEntry();
		typeEntry.type = TableEntry.Type.DOUBLE;
		typeEntry.kind = TableEntry.Kind.PROPERTY;
		typeEntry.name = "type";
		typeEntry.offset = 0;
		insert(typeEntry);
		
		TableEntry maxEntry = new TableEntry();
		maxEntry.type = TableEntry.Type.DOUBLE;
		maxEntry.kind = TableEntry.Kind.METHOD;
		maxEntry.name = "max";
		maxEntry.arguments = new ArgList(TableEntry.Type.DOUBLE_ARR);
		insert(maxEntry);
		
		TableEntry minEntry = new TableEntry();
		minEntry.type = TableEntry.Type.DOUBLE;
		minEntry.kind = TableEntry.Kind.METHOD;
		minEntry.name = "min";
		minEntry.arguments = new ArgList(TableEntry.Type.DOUBLE_ARR);
		insert(minEntry);
		
		TableEntry logEntry = new TableEntry();
		logEntry.type = TableEntry.Type.DOUBLE;
		logEntry.kind = TableEntry.Kind.METHOD;
		logEntry.name = "log";
		logEntry.arguments = new ArgList(TableEntry.Type.DOUBLE);
		insert(logEntry);
		
		TableEntry sinEntry = new TableEntry();
		sinEntry.type = TableEntry.Type.DOUBLE;
		sinEntry.kind = TableEntry.Kind.METHOD;
		sinEntry.name = "sin";
		sinEntry.arguments = new ArgList(TableEntry.Type.DOUBLE);
		insert(sinEntry);
		
		TableEntry cosEntry = new TableEntry();
		cosEntry.type = TableEntry.Type.DOUBLE;
		cosEntry.kind = TableEntry.Kind.METHOD;
		cosEntry.name = "cos";
		cosEntry.arguments = new ArgList(TableEntry.Type.DOUBLE);
		insert(cosEntry);
		
		TableEntry lnEntry = new TableEntry();
		lnEntry.type = TableEntry.Type.DOUBLE;
		lnEntry.kind = TableEntry.Kind.METHOD;
		lnEntry.name = "ln";
		lnEntry.arguments = new ArgList(TableEntry.Type.DOUBLE);
		insert(lnEntry);
		
		TableEntry sumEntry = new TableEntry();
		sumEntry.type = TableEntry.Type.DOUBLE;
		sumEntry.kind = TableEntry.Kind.METHOD;
		sumEntry.name = "sum";
		sumEntry.arguments = new ArgList(TableEntry.Type.DOUBLE_ARR);
		insert(sumEntry);
		
		TableEntry randomEntry = new TableEntry();
		randomEntry.type = TableEntry.Type.DOUBLE;
		randomEntry.kind = TableEntry.Kind.METHOD;
		randomEntry.name = "random";
		randomEntry.arguments = new ArgList(TableEntry.Type.DOUBLE);
		insert(randomEntry);
		
		TableEntry roundEntry = new TableEntry();
		roundEntry.type = TableEntry.Type.INT;
		roundEntry.kind = TableEntry.Kind.METHOD;
		roundEntry.name = "round";
		roundEntry.arguments = new ArgList(TableEntry.Type.DOUBLE);
		insert(roundEntry);
		
		TableEntry countEntry = new TableEntry();
		countEntry.type = TableEntry.Type.INT;
		countEntry.kind = TableEntry.Kind.METHOD;
		countEntry.name = "count";
		countEntry.arguments = new ArgList(TableEntry.Type.DOUBLE_ARR);
		insert(countEntry);
		
		TableEntry existsEntry = new TableEntry();
		existsEntry.type = TableEntry.Type.BOOL;
		existsEntry.kind = TableEntry.Kind.METHOD;
		existsEntry.name = "exists";
		existsEntry.arguments = new ArgList(TableEntry.Type.CELL);
		insert(existsEntry);
		
		TableEntry neighboursEntry = new TableEntry();
		neighboursEntry.type = TableEntry.Type.CELL_ARR;
		neighboursEntry.kind = TableEntry.Kind.CELL;
		neighboursEntry.name = "neighbours";
		neighboursEntry.offset = 2;
		insert(neighboursEntry);
		
		TableEntry moveEntry = new TableEntry();
		moveEntry.type = TableEntry.Type.VOID;
		moveEntry.kind = TableEntry.Kind.BEHAVIOUR;
		moveEntry.name = "move";
		moveEntry.arguments = new ArgList(TableEntry.Type.BOOL, TableEntry.Type.DOUBLE, TableEntry.Type.INT, TableEntry.Type.CELL, TableEntry.Type.CELL_ARR);
		insert(moveEntry);
		
		TableEntry acceptEntry = new TableEntry();
		acceptEntry.type = TableEntry.Type.VOID;
		acceptEntry.kind = TableEntry.Kind.BEHAVIOUR;
		acceptEntry.name = "accept";
		acceptEntry.arguments = new ArgList(TableEntry.Type.BOOL, TableEntry.Type.INT, TableEntry.Type.CELL, TableEntry.Type.CELL_ARR);
		insert(acceptEntry);
		
		TableEntry propagateEntry = new TableEntry();
		propagateEntry.type = TableEntry.Type.VOID;
		propagateEntry.kind = TableEntry.Kind.BEHAVIOUR;
		propagateEntry.name = "propagate";
		propagateEntry.arguments = new ArgList(TableEntry.Type.BOOL, TableEntry.Type.DOUBLE, TableEntry.Type.CELL, TableEntry.Type.CELL_ARR);
		insert(propagateEntry);
		
		TableEntry pulsarEntry = new TableEntry();
		pulsarEntry.type = TableEntry.Type.VOID;
		pulsarEntry.kind = TableEntry.Kind.BEHAVIOUR;
		pulsarEntry.name = "pulsar";
		pulsarEntry.arguments = new ArgList(TableEntry.Type.BOOL, TableEntry.Type.DOUBLE, TableEntry.Type.DOUBLE, TableEntry.Type.DOUBLE, TableEntry.Type.CELL, TableEntry.Type.CELL_ARR);
		insert(pulsarEntry);
		
		TableEntry setMovFlag = new TableEntry();
		setMovFlag.type = TableEntry.Type.VOID;
		setMovFlag.kind = TableEntry.Kind.BEHAVIOUR;
		setMovFlag.name = "setMovementFlag";
		setMovFlag.arguments = new ArgList(TableEntry.Type.DOUBLE, TableEntry.Type.CELL, TableEntry.Type.CELL_ARR);
		insert(setMovFlag);
		
		TableEntry appendEntry = new TableEntry();
		appendEntry.type = TableEntry.Type.DOUBLE_ARR;
		appendEntry.kind = TableEntry.Kind.METHOD;
		appendEntry.name = "append";
		appendEntry.arguments = new ArgList(TableEntry.Type.DOUBLE_ARR, TableEntry.Type.DOUBLE);
		insert(appendEntry);
		
		TableEntry insertEntry = new TableEntry();
		insertEntry.type = TableEntry.Type.DOUBLE_ARR;
		insertEntry.kind = TableEntry.Kind.METHOD;
		insertEntry.name = "insert";
		insertEntry.arguments = new ArgList(TableEntry.Type.DOUBLE_ARR, TableEntry.Type.DOUBLE, TableEntry.Type.DOUBLE);
		insert(insertEntry);
		
		TableEntry deleteEntry = new TableEntry();
		deleteEntry.type = TableEntry.Type.DOUBLE_ARR;
		deleteEntry.kind = TableEntry.Kind.METHOD;
		deleteEntry.name = "delete";
		deleteEntry.arguments = new ArgList(TableEntry.Type.DOUBLE_ARR, TableEntry.Type.DOUBLE);
		insert(deleteEntry);
		
		TableEntry deleteAllEntry = new TableEntry();
		deleteAllEntry.type = TableEntry.Type.DOUBLE_ARR;
		deleteAllEntry.kind = TableEntry.Kind.METHOD;
		deleteAllEntry.name = "deleteAll";
		deleteAllEntry.arguments = new ArgList(TableEntry.Type.DOUBLE_ARR, TableEntry.Type.DOUBLE);
		insert(deleteAllEntry);
	}
	
	public void insert(TableEntry entry)
	{
		Scope currentScope = scopeStack.peek();
		entry.nextEntry = currentScope.firstEntry;
		currentScope.firstEntry = entry;
	}
	
	public class Scope
	{
		public Scope outer;
		public TableEntry firstEntry;
	}
	
	public String toString()
	{
		String str = "SymbolTable:\n";
		Scope currentScope = scopeStack.peek();
		
		while(currentScope != null)
		{
			str += "SCOPE: ";
			TableEntry currentEntry = currentScope.firstEntry;
			
			while(currentEntry != null)
			{
				str += currentEntry.name + "->" + currentEntry.type + " ";
				currentEntry = currentEntry.nextEntry;
			}
			str += "\n";
			currentScope = currentScope.outer;
		}
		return str;
	}
}
