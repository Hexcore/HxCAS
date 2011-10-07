package com.hexcore.cas.rulesystems;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import com.hexcore.cas.rulesystems.Parser.AddOpE;
import com.hexcore.cas.rulesystems.Parser.MulOpE;
import com.hexcore.cas.rulesystems.Parser.RelOpE;

public class CodeGen implements org.objectweb.asm.Opcodes
{
	public static ArrayList<byte[]> code = new ArrayList<byte[]>();
	public static ClassWriter cw;
	public static String  name;
	
	private static Label executeBegin;
	private static Label executeEnd;
	private static MethodVisitor executeVisitor;
	private static int varIndex = 3;
	private static int propertyIndex = 1;
	
	private static Label[] frameworkLabels;
	private static int[] frameworkIndices;
	private static Label defaultLabel;
	private static int currentFrameworkIndex = 0;
	
	
	
	static void initClass(String ruleset)
	{
		varIndex = 3;
		currentFrameworkIndex = 0;
		propertyIndex = 1;

		
		
		System.out.println("CAL Compiler: Creating class.");
		name = ruleset;
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		
		//Class initialisation
		String[] interfaces = {"com/hexcore/cas/rulesystems/Rule"};
		cw.visit(V1_6, ACC_PUBLIC | ACC_SUPER, "com/hexcore/cas/rulesystems/"+ruleset+"Rule", null, "java/lang/Object", interfaces);
		
		//Visit constructor
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();
		Label startScope = new Label();
		mv.visitLabel(startScope);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
		mv.visitInsn(RETURN);
		Label endScope = new Label();
		mv.visitLabel(endScope);
		mv.visitLocalVariable("this", "Lcom/hexcore/cas/rulesystems/"+ruleset+"Rule;", null, startScope, endScope, 0);
		mv.visitMaxs(0, 0);
		
		mv.visitEnd();		
	}
	
	public static void initExecute()
	{
		System.out.println("CAL Compiler: Creating default method.");
		executeVisitor = cw.visitMethod(ACC_PUBLIC, "execute", "(Lcom/hexcore/cas/model/Cell;[Lcom/hexcore/cas/model/Cell;)V", null, null);
		executeVisitor.visitCode();
		executeBegin = new Label();
		executeVisitor.visitLabel(executeBegin);
	}
	
	public static void initFramework(int numTypes)
	{
		System.out.println("CAL Compiler: Creating framework.");
		currentFrameworkIndex = 0;
		frameworkIndices = new int[numTypes];
		frameworkLabels = new Label[numTypes];
		for(int i = 0; i < numTypes; i++)
		{
			frameworkLabels[i] = new Label();
		}
		
		defaultLabel = new Label();
		derefProperty(0);
		executeVisitor.visitInsn(D2I);
		executeVisitor.visitLookupSwitchInsn(defaultLabel, frameworkIndices, frameworkLabels);
	}
	
	public static void initType()
	{
		System.out.println("CAL Compiler: Creating Type.");
		IndexOutOfBoundsException ex = new IndexOutOfBoundsException();
		
		try
		{
			if(currentFrameworkIndex >= frameworkLabels.length)
				throw ex;
			
			executeVisitor.visitLabel(frameworkLabels[currentFrameworkIndex]);
			
			if(currentFrameworkIndex == 0)
				executeVisitor.visitFrame(F_APPEND, 1, new Object[]{INTEGER}, 0, null);
			else
				executeVisitor.visitFrame(F_SAME, 0, null, 0, null);
			
			currentFrameworkIndex++;
		}
		catch(IndexOutOfBoundsException e)
		{
			
		}		
	}
	
	public static int declareLocalVariable(String name)
	{
		//executeVisitor.visitLocalVariable(name, "D", null, executeBegin, executeEnd, varIndex);
		return varIndex++;
	}
	
	public static int declareProperty()
	{
		return propertyIndex++;
	}
	
	public static void loadConstant(double value)
	{
		executeVisitor.visitLdcInsn(new Double(value));
	}
	
	
	public static void storeVariable(int index)
	{
		executeVisitor.visitVarInsn(DSTORE, index);
	}
	
	public static void storeProperty(int index)
	{
		executeVisitor.visitVarInsn(ALOAD, 1);
		executeVisitor.visitInsn(SWAP);
		executeVisitor.visitLdcInsn(new Integer(index));
		executeVisitor.visitInsn(SWAP);
		executeVisitor.visitMethodInsn(INVOKEVIRTUAL, "Cell", "setValue", "(ID)V");
	}
	
	
	public static void performPostOp(int index, int delta)
	{
		executeVisitor.visitIincInsn(index, delta);
	}
	
	
	public static void derefVariable(int index)
	{
		executeVisitor.visitVarInsn(DLOAD, index);
	}
	
	public static void derefRef(int index)
	{
		executeVisitor.visitVarInsn(ALOAD, index);
	}
	
	
	public static void derefProperty(int index)
	{
		executeVisitor.visitVarInsn(ALOAD, 1);
		executeVisitor.visitLdcInsn(new Integer(index));
		executeVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/hexcore/cas/model/Cell", "getValue", "(I)D");
	}
	
	public static void performRelationalOp(RelOpE op)
	{
		Label negative = new Label();
		Label end = new Label();
		switch(op)
		{
			case LT:	executeVisitor.visitJumpInsn(IFGE, negative); break;
			case GT:	executeVisitor.visitJumpInsn(IFLE, negative); break;
			case LE:	executeVisitor.visitJumpInsn(IFGT, negative); break;
			case GE:	executeVisitor.visitJumpInsn(IFLT, negative); break;
			case EQ:	executeVisitor.visitJumpInsn(IFNE, negative); break;
			case NE:	executeVisitor.visitJumpInsn(IFEQ, negative); break;
		}
		
		executeVisitor.visitInsn(ICONST_1);
		executeVisitor.visitJumpInsn(GOTO, end);
		executeVisitor.visitLabel(negative);
		executeVisitor.visitInsn(ICONST_0);
		executeVisitor.visitLabel(end);
	}
	
	public static void performAddOp(AddOpE op)
	{
		switch(op)
		{
			case ADD: executeVisitor.visitInsn(DADD); break;
			case SUB: executeVisitor.visitInsn(DSUB); break;
			case OR: executeVisitor.visitInsn(IOR); break;
		}
	}
	
	public static void performMulOp(MulOpE op)
	{
		switch(op)
		{
			case MUL: executeVisitor.visitInsn(DMUL); break;
			case DIV: executeVisitor.visitInsn(DDIV); break;
			case MOD: executeVisitor.visitInsn(DREM); break;
			case AND: executeVisitor.visitInsn(IAND); break;
		}
	}
	
	public static void negate()
	{
		executeVisitor.visitInsn(DNEG);
	}
	
	public static void endType()
	{
		System.out.println("CAL Compiler: Finialising type.");
		executeVisitor.visitJumpInsn(GOTO, defaultLabel);
	}
	
	public static void endExecute()
	{
		System.out.println("CAL Compiler: Finialising default method.");
		executeVisitor.visitLabel(defaultLabel);
		executeVisitor.visitInsn(RETURN);
		executeEnd = new Label();
		executeVisitor.visitLabel(executeEnd);
		executeVisitor.visitLocalVariable("this", "Lcom/hexcore/cas/rulesystems/"+name+"Rule;", null, executeBegin, executeEnd, 0);
		executeVisitor.visitLocalVariable("self", "Lcom/hexcore/cas/model/Cell;", null, executeBegin, executeEnd, 1);
		executeVisitor.visitLocalVariable("neighbours", "[Lcom/hexcore/cas/model/Cell;", null, executeBegin, executeEnd, 2);
		executeVisitor.visitMaxs(0, 0);
		executeVisitor.visitEnd();
	}
	
	
	
	public static void endClass()
	{
		System.out.println("CAL Compiler: Finialising class.");
		//End class
		cw.visitEnd();
		
		//TODO: Remove file saving
		toFile(name);
	}
	
	public static byte[] getCode()
	{
		return cw.toByteArray();
	}
	
	public static void toFile(String fileName)
	{
		try
		{
			FileOutputStream out = new FileOutputStream(new File(name+".class"));
			out.write(cw.toByteArray());
			out.close();
		} catch (IOException e)
		{
			System.err.println("Could not write class file");
		}
	}
}
