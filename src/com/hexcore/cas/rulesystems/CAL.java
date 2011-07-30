package com.hexcore.cas.rulesystems;

/* Generic driver frame file for Coco/R for Java
   PDT  30 April 2003 (p.terry@ru.ac.za)
   Modify this to suit your own purposes - hints are given below! */

//  ----------------------- you may need to change the "import" clauses:

import java.io.*;

public class CAL {

	public static void main (String[] args) {
		boolean mergeErrors = false;
		String inputName = null;

		// ------------------------ you may need to process command line parameters:

		for (int i = 0; i < args.length; i++) {
			if (args[i].toLowerCase().equals("-l")) mergeErrors = true;
			else inputName = args[i];
		}
		if (inputName == null) {
			System.err.println("No input file specified");
			System.exit(1);
		}

		int pos = inputName.lastIndexOf('/');
		if (pos < 0) pos = inputName.lastIndexOf('\\');
		String dir = inputName.substring(0, pos+1);

		Scanner.Init(inputName);
		Errors.Init(inputName, dir, mergeErrors);
		//  ----------------------- add other initialization if required:
		Parser.Parse();
		Errors.Summarize();
		//  ----------------------- add other finalization if required:
	}

} // end driver
