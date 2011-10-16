package com.hexcore.cas.rulesystems;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import com.hexcore.cas.rulesystems.Parser.AddOpE;
import com.hexcore.cas.rulesystems.Parser.MulOpE;
import com.hexcore.cas.rulesystems.Parser.RelOpE;
import com.hexcore.cas.rulesystems.TableEntry;

public class CodeGen implements org.objectweb.asm.Opcodes
{
	public static ArrayList<byte[]> code = new ArrayList<byte[]>();
	public static ClassWriter cw;
	public static String  name;
	
	private static Label executeBegin;
	private static Label executeEnd;
	private static MethodVisitor executeVisitor;
	private static int varIndex = 5;
	private static int propertyIndex = 1;
	private static int numProperties = 1;
	
	private static Label[] frameworkLabels;
	private static int[] frameworkIndices;
	private static Label defaultLabel;
	private static int currentFrameworkIndex = 0;
	private static boolean debugEnabled = true;
	
	private static void debug(String msg)
	{
		if(debugEnabled)
			System.out.println(msg);
	}
	
	static void initClass(String ruleset)
	{
		debug("initClass");
		varIndex = 5;
		currentFrameworkIndex = 0;
		propertyIndex = 1;
		numProperties = 1;

		name = ruleset;
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES + ClassWriter.COMPUTE_MAXS);
		
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
		debug("initExecute");
		executeVisitor = cw.visitMethod(ACC_PUBLIC, "run", "(Lcom/hexcore/cas/model/Cell;[Lcom/hexcore/cas/model/Cell;)V", null, null);
		executeVisitor.visitCode();
		executeBegin = new Label();
		executeVisitor.visitLabel(executeBegin);
	}
	
	public static void initFramework(int numTypes)
	{
		debug("initFramework: " + numTypes + " types");
		currentFrameworkIndex = 0;
		frameworkIndices = new int[numTypes];
		frameworkLabels = new Label[numTypes];
		for(int i = 0; i < numTypes; i++)
		{
			frameworkLabels[i] = new Label();
		}
		
		defaultLabel = new Label();
		
		//Current index required
		derefRef(1);			//Self
		derefProperty(0);		//Self.type
		
		executeVisitor.visitInsn(D2I);
		executeVisitor.visitLookupSwitchInsn(defaultLabel, frameworkIndices, frameworkLabels);
	}
	
	public static void initType()
	{
		debug("initType");
		IndexOutOfBoundsException ex = new IndexOutOfBoundsException();
		
		try
		{
			if(currentFrameworkIndex >= frameworkLabels.length)
				throw ex;
			
			executeVisitor.visitLabel(frameworkLabels[currentFrameworkIndex]);
			
			currentFrameworkIndex++;
		}
		catch(IndexOutOfBoundsException e)
		{
			
		}		
	}
	
	public static int declareLocalVariable(String name)
	{
		debug("Declare var index: " + varIndex);
		//executeVisitor.visitLocalVariable(name, "D", null, executeBegin, executeEnd, varIndex);
		int result = varIndex;
		varIndex += 2;
		return result;
	}
	
	public static int declareProperty()
	{
		debug("Declare property index: " + propertyIndex);
		numProperties++;
		return propertyIndex++;
	}
	
	public static void loadConstant(double value)
	{
		debug("Loading constant: " + value);
		executeVisitor.visitLdcInsn(new Double(value));
	}
	
	
	public static void storeVariable(int index)
	{
		debug("Storing at: " + index);
		executeVisitor.visitVarInsn(DSTORE, index);
	}
	
	public static void storeProperty(int index)
	{
		debug("Storing at property: " + index);
		//Store result in temp variable in preparation for function call.
		executeVisitor.visitVarInsn(DSTORE, 3);
		//executeVisitor.visitVarInsn(ALOAD, 1);
		executeVisitor.visitLdcInsn(new Integer(index));
		
		//Restore value
		executeVisitor.visitVarInsn(DLOAD, 3);
		
		executeVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/hexcore/cas/model/Cell", "setValue", "(ID)V");
	}
	
	
	public static void performPostOp(int index, int delta)
	{
		debug("Post op");
		executeVisitor.visitVarInsn(DLOAD, index);
		executeVisitor.visitLdcInsn(new Double(delta));
		executeVisitor.visitInsn(DADD);
		executeVisitor.visitVarInsn(DSTORE, index);
	}
	
	
	public static void derefVariable(int index)
	{
		debug("Deref var: " + index);
		executeVisitor.visitVarInsn(DLOAD, index);
	}
	
	public static void derefRef(int index)
	{
		debug("Deref ref: " + index);
		executeVisitor.visitVarInsn(ALOAD, index);
	}
	
	public static void derefArrayRef()
	{
		debug("Dereferencing array");
		executeVisitor.visitInsn(D2I);
		executeVisitor.visitInsn(AALOAD);
	}
	
	
	public static void derefProperty(int index)
	{
		debug("Deref property: " + index);
		//executeVisitor.visitVarInsn(ALOAD, 1);
		executeVisitor.visitLdcInsn(new Integer(index));
		executeVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/hexcore/cas/model/Cell", "getValue", "(I)D");
	}
	
	public static void generatePropertyArray(int index)
	{
		debug("Generating property array.");
		executeVisitor.visitLdcInsn(new Integer(index));
		executeVisitor.visitMethodInsn(INVOKESTATIC, "com/hexcore/cas/rulesystems/StdLib", "generatePropertyArray", "([Lcom/hexcore/cas/model/Cell;I)[D");
	}
	
	public static void invokeStandardArrayMethod(String name, int type)
	{
		debug("Invoking StdLib array method: " + name + " as type " + type);
		
		if(type == TableEntry.doubleType)
			executeVisitor.visitMethodInsn(INVOKESTATIC, "com/hexcore/cas/rulesystems/StdLib", name, "([D)D");
		else if(type == TableEntry.intType)
		{
			executeVisitor.visitMethodInsn(INVOKESTATIC, "com/hexcore/cas/rulesystems/StdLib", name, "([D)I");
			executeVisitor.visitInsn(I2D);
		}
	}
	
	public static void invokeStandardScalarMethod(String name)
	{
		debug("Invoking StdLib scalar method: " + name);
		executeVisitor.visitMethodInsn(INVOKESTATIC, "com/hexcore/cas/rulesystems/StdLib", name, "(D)D");		
	}
	
	public static void performRelationalOp(RelOpE op)
	{
		debug("Relational op: " + op.toString());
		Label negative = new Label();
		Label end = new Label();
		executeVisitor.visitInsn(DCMPG);
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
	
	public static void toDouble()
	{
		debug("Convert to double");
		executeVisitor.visitInsn(I2D);
	}
	
	public static void performAddOp(AddOpE op)
	{
		debug("Add op: " + op.toString());
		switch(op)
		{
			case ADD: executeVisitor.visitInsn(DADD); break;
			case SUB: executeVisitor.visitInsn(DSUB); break;
			case OR: executeVisitor.visitInsn(IOR); break;
		}
	}
	
	public static void performMulOp(MulOpE op)
	{
		debug("Mul op: " + op.toString());
		switch(op)
		{
			case MUL: executeVisitor.visitInsn(DMUL); break;
			case DIV: executeVisitor.visitInsn(DDIV); break;
			case MOD: executeVisitor.visitInsn(DREM); break;
			case AND: executeVisitor.visitInsn(IAND); break;
		}
	}
	
	public static Label[]  initIf()
	{
		debug("Start if");
		Label negative = new Label();
		Label end = new Label();
		
		executeVisitor.visitJumpInsn(IFEQ, negative);
		Label[] pointers = {negative, end};
		return pointers;
	}
	
	public static void visitLabel(Label label)
	{
		debug("Label visit: " + label);
		executeVisitor.visitLabel(label);
	}
	
	public static void jump(Label label)
	{
		debug("Jump to: " + label);
		executeVisitor.visitJumpInsn(GOTO, label);
	}
	
	public static void negate()
	{
		debug("Negate");
		executeVisitor.visitInsn(DNEG);
	}
	
	public static void endType()
	{
		debug("End Type");
		executeVisitor.visitJumpInsn(GOTO, defaultLabel);
	}
	
	public static void endExecute()
	{
		debug("End Execute");
		executeVisitor.visitLabel(defaultLabel);
		executeVisitor.visitInsn(RETURN);
		executeEnd = new Label();
		executeVisitor.visitLabel(executeEnd);
		executeVisitor.visitLocalVariable("this", "Lcom/hexcore/cas/rulesystems/"+name+"Rule;", null, executeBegin, executeEnd, 0);
		executeVisitor.visitLocalVariable("self", "Lcom/hexcore/cas/model/Cell;", null, executeBegin, executeEnd, 1);
		executeVisitor.visitLocalVariable("neighbours", "[Lcom/hexcore/cas/model/Cell;", null, executeBegin, executeEnd, 2);
		executeVisitor.visitLocalVariable("temp", "D", null, executeBegin, executeEnd, 3);
		executeVisitor.visitMaxs(0, 0);
		executeVisitor.visitEnd();
	}
	
	public static void implementPropertyCountFunction()
	{
		debug("Implement property count");
		
		MethodVisitor mVisitor = cw.visitMethod(ACC_PUBLIC, "getNumProperties", "()I", null, null);
		mVisitor.visitCode();
		mVisitor.visitLdcInsn(new Integer(numProperties));
		mVisitor.visitInsn(IRETURN);
		mVisitor.visitMaxs(0, 0);
		mVisitor.visitEnd();
	}
	
	
	
	public static void endClass()
	{
		implementPropertyCountFunction();
		debug("End class");
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
