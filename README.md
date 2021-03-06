HxCAS
=====

[![Build Status](https://travis-ci.org/Hexcore/HxCAS.png?branch=master)](https://travis-ci.org/Hexcore/HxCAS)

Introduction
------------
HxCAS is a Cellular Automata Simulator and Editor. HxCAS simulates 
2-dimensional cellular automata where each cell can have multiple
properties.

More information can be found in the [wiki](https://github.com/Hexcore/HxCAS/wiki)

Features
--------
 * 2D and 3D visualisation
 * Distributed calculation
 * Automatic client discovery
 * Domain-specific language for cell rules - [language reference](https://github.com/Hexcore/HxCAS/wiki/CAL-Language-Reference)
 * Multiple properties per cell
 * Homogeneous and Heterogeneous worlds
 * Square, Hexagonal and Triangular cell shapes

Compilation Prerequisites
-------------------------
The project includes Eclipse project settings. The project has been
successfully compiled with the following versions of Eclipse:

 * Helios
 * Indigo
 * Juno
 
HxCAS has been successfully compiled with the following versions of Java:

 * 1.6
 * 1.7
 
HxCAS has been successfully compiled and includes native libraries for the
following platforms:

 * Windows 64bit
 * Linux 64bit

Compilation Types
-----------------
HxCAS can be compiled in two ways from Eclipse:

 1. Standard source compile
 2. Full ANT compile
 
Under normal circumstances a standard compile is all that is needed.
Under default circumstances Eclipse will perform a compile on the code
automatically in the background. Successful compilation can be confirmed
by clicking on the "run" button in Eclipse.
 
A full ANT compile can be performed by running the build.xml file with ANT.
This is only necessary when the file: CoCo/CAL.atg has been changed or any of
CoCo's generated files need to be restored. These files are:

 * src/com/hexcore/cas/rulesystems/CAL.java
 * src/com/hexcore/cas/rulesystems/Parser.java
 * src/com/hexcore/cas/rulesystems/Scanner.java
 
Upon invocation ANT will run CoCo on the CAL.atg grammar file generating the 
abovementioned source files and move them into their correct locations before 
performing a full compile on all source.
 
Running HxCAS
-------------
The main HxCAS program can be started in Eclipse by running Server.java.
Outside of Eclipse Server.bat (Windows) or Server.sh (Linux) can be run.
