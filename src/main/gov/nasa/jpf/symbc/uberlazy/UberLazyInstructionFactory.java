//
// Copyright (C) 2007 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
//
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
//
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//

package gov.nasa.jpf.symbc.uberlazy;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPFException;
import gov.nasa.jpf.jvm.ClassInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.symbc.SymbolicInstructionFactory;
import gov.nasa.jpf.util.InstructionFactoryFilter;

public class UberLazyInstructionFactory extends SymbolicInstructionFactory {

	static Class<? extends Instruction>[] insnClass;

	static {
		//the highest intercepted bytecode (when alphabetically listed) + 1
		// should be the index of the instruction class array
		insnClass = createInsnClassArray(INSTANCEOF+1);
		
		//whenever adding a new bytecode -- add in alphabetical order of the bytecode
		//this allows us to easily detect whether the correct size instruction array
		// is instatiated or not
		
		insnClass[GETFIELD] = gov.nasa.jpf.symbc.uberlazy.bytecode.GETFIELD.class;
		insnClass[IF_ACMPEQ] = gov.nasa.jpf.symbc.uberlazy.bytecode.IF_ACMPEQ.class;
		insnClass[IF_ACMPNE] = gov.nasa.jpf.symbc.uberlazy.bytecode.IF_ACMPNE.class;
		insnClass[INSTANCEOF] = gov.nasa.jpf.symbc.uberlazy.bytecode.INSTANCEOF.class;
		insnClass[INVOKEVIRTUAL] = gov.nasa.jpf.symbc.uberlazy.bytecode.INVOKEVIRTUAL.class;
	}

	InstructionFactoryFilter filter = new InstructionFactoryFilter(null, new String[] {"java.*", "javax.*" },
			null, null);
	
	public UberLazyInstructionFactory(Config conf) {
		super(conf); //call to parent intitalizes the constraint solver
	}

	public Instruction create(ClassInfo ciMth, int opCode) {

	    if (opCode < insnClass.length){
	      Class<?> cls = insnClass[opCode];
	      if (cls != null && filter.isInstrumentedClass(ciMth)) {
	        try {
	          Instruction insn = (Instruction) cls.newInstance();
	          return insn;

	        } catch (Throwable e) {
	          throw new JPFException("creation of symbc Instruction object for opCode "
	                  + opCode + " failed: " + e);
	        }
	      }
	    }

	    // use default instruction classes
	    return super.create(ciMth, opCode);
	  }
}