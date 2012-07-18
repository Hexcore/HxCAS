package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Class Node
 * 
 * @authors Divan Burger; Megan Duncan; Apurva Kumar
 */

public abstract class Node
{
	public abstract String toString();
	public abstract void write(OutputStream out) throws IOException;
}
