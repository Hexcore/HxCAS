package com.hexcore.cas.rulesystems;

import java.util.Stack;

public class SymbolTable
{
	private Stack<Scope>	scopeStack;
	
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
		noEntry.type = TableEntry.noType;
		return noEntry;
	}
	
	public void prepare()
	{
		TableEntry selfEntry = new TableEntry();
		selfEntry.type = TableEntry.cellType;
		selfEntry.kind = TableEntry.Cell;
		selfEntry.name = "self";
		selfEntry.offset = 1;
		insert(selfEntry);
		
		TableEntry typeEntry = new TableEntry();
		typeEntry.type = TableEntry.intType;
		typeEntry.kind = TableEntry.Property;
		typeEntry.name = "type";
		typeEntry.offset = 0;
		insert(typeEntry);
		
		TableEntry maxEntry = new TableEntry();
		maxEntry.type = TableEntry.doubleType;
		maxEntry.kind = TableEntry.aFunction;
		maxEntry.name = "max";
		maxEntry.argType = TableEntry.doubleType;
		insert(maxEntry);
		
		TableEntry minEntry = new TableEntry();
		minEntry.type = TableEntry.doubleType;
		minEntry.kind = TableEntry.aFunction;
		minEntry.name = "min";
		minEntry.argType = TableEntry.doubleType;
		insert(minEntry);
		
		TableEntry logEntry = new TableEntry();
		logEntry.type = TableEntry.doubleType;
		logEntry.kind = TableEntry.sFunction;
		logEntry.name = "log";
		logEntry.argType = TableEntry.doubleType;
		insert(logEntry);
		
		TableEntry sinEntry = new TableEntry();
		sinEntry.type = TableEntry.doubleType;
		sinEntry.kind = TableEntry.sFunction;
		sinEntry.name = "sin";
		sinEntry.argType = TableEntry.doubleType;
		insert(sinEntry);
		
		TableEntry cosEntry = new TableEntry();
		cosEntry.type = TableEntry.doubleType;
		cosEntry.kind = TableEntry.sFunction;
		cosEntry.name = "cos";
		cosEntry.argType = TableEntry.doubleType;
		insert(cosEntry);
		
		TableEntry lnEntry = new TableEntry();
		lnEntry.type = TableEntry.doubleType;
		lnEntry.kind = TableEntry.sFunction;
		lnEntry.name = "ln";
		lnEntry.argType = TableEntry.doubleType;
		insert(lnEntry);
		
		TableEntry sumEntry = new TableEntry();
		sumEntry.type = TableEntry.doubleType;
		sumEntry.kind = TableEntry.aFunction;
		sumEntry.name = "sum";
		sumEntry.argType = TableEntry.doubleType;
		insert(sumEntry);
		
		TableEntry randomEntry = new TableEntry();
		randomEntry.type = TableEntry.doubleType;
		randomEntry.kind = TableEntry.sFunction;
		randomEntry.name = "random";
		randomEntry.argType = TableEntry.doubleType;
		insert(randomEntry);
		
		TableEntry countEntry = new TableEntry();
		countEntry.type = TableEntry.intType;
		countEntry.kind = TableEntry.aFunction;
		countEntry.name = "count";
		countEntry.argType = TableEntry.intType;
		insert(countEntry);
		
		TableEntry existsEntry = new TableEntry();
		existsEntry.type = TableEntry.boolType;
		existsEntry.kind = TableEntry.sFunction;
		existsEntry.name = "exists";
		existsEntry.argType = TableEntry.cellType;
		insert(existsEntry);
		
		TableEntry neighboursEntry = new TableEntry();
		neighboursEntry.type = TableEntry.cellType + 1;
		neighboursEntry.kind = TableEntry.Cell;
		neighboursEntry.name = "neighbours";
		neighboursEntry.offset = 2;
		insert(neighboursEntry);
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
