//
//Copyright (C) 2006 United States Government as represented by the
//Administrator of the National Aeronautics and Space Administration
//(NASA).  All Rights Reserved.
//
//This software is distributed under the NASA Open Source Agreement
//(NOSA), version 1.3.  The NOSA has been approved by the Open Source
//Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
//directory tree for the complete NOSA document.
//
//THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
//KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
//LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
//SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
//A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
//THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
//DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//

package gov.nasa.jpf.symbc.numeric;

//TODO: problem: we do not distinguish between ints and reals?
// still needs a lot of work: do not use!


import java.util.HashMap;
import java.util.List;

import cvc3.Expr;
import cvc3.ExprMut;
import cvc3.FlagsMut;
import cvc3.QueryResult;
import cvc3.Rational;
import cvc3.SatResult;
import cvc3.TypeMut;
//import cvc3.SatResult;
import cvc3.Type;
import cvc3.ValidityChecker;

public class ProblemCVC3 extends ProblemGeneral {
	protected Expr pb;
	protected ValidityChecker vc = null;
    protected FlagsMut flags = null;
    protected final int base = 10; //used in creating real variables
    protected HashMap model;

	public ProblemCVC3() {
		pb = null;
		try{
	        flags = ValidityChecker.createFlags(null);
	        flags.setFlag("dagify-exprs",false);
	        vc = ValidityChecker.create(flags);
	       // System.out.println("validity checker is initialized");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
	    }
	}

	//To Do: need to call this at some point;
	private void cleanup(){
		try{
	        if (vc != null) vc.delete();
	        if (flags != null) flags.delete();
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	//if min or max are passed in as null objects to the vc
	//it will use minus and plus infinity
	Object makeIntVar(String name, int min, int max) {
		try{
			Type sType = vc.subrangeType(vc.ratExpr(min),
                    vc.ratExpr(max));
			return vc.varExpr(name, sType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}
	
	

	Object makeRealVar(String name, double min, double max) {

		//WARNING: need to downcast double to int - I don't see
		// a way in CVC3 to create a sub-range for real types
		//other choice is not to bound and use vc.realType() to
		//create the expression
		int minInt = (int)min;
		int maxInt = (int)max;
		try{
			//Expr x = vc.varExpr(name, vc.realType());
			Type sType = vc.subrangeType(vc.ratExpr(minInt),
                    vc.ratExpr(maxInt));
			return vc.varExpr(name, sType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object eq(int value, Object exp){
		try{
			return  vc.eqExpr(vc.ratExpr(value), (Expr)exp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object eq(Object exp, int value){
		try{
			return  vc.eqExpr((Expr)exp, vc.ratExpr(value));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object eq(Object exp1, Object exp2){
		try{
			return  vc.eqExpr((Expr)exp1, (Expr)exp2);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object eq(double value, Object exp){
		try{
			return  vc.eqExpr(vc.ratExpr(Double.toString(value), base), (Expr)exp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object eq(Object exp, double value){
		try{
			return  vc.eqExpr((Expr)exp, vc.ratExpr(Double.toString(value), base));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object neq(int value, Object exp){
		try{
			return  vc.notExpr(vc. eqExpr(vc.ratExpr(value), (Expr)exp));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object neq(Object exp, int value){
		try{
			return  vc.notExpr(vc.eqExpr((Expr)exp, vc.ratExpr(value)));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object neq(Object exp1, Object exp2){
		try{
			return  vc.notExpr(vc.eqExpr((Expr)exp1, (Expr)exp2));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	public Object not(Object exp1){
		try{
			return  vc.notExpr((Expr)exp1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object neq(double value, Object exp){
		try{
			return  vc.notExpr(vc.eqExpr(vc.ratExpr(Double.toString(value), base), (Expr)exp));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object neq(Object exp, double value){
		try{
			return  vc.notExpr(vc.eqExpr((Expr)exp, vc.ratExpr(Double.toString(value), base)));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object leq(int value, Object exp){
		try{
			return  vc.leExpr(vc.ratExpr(value), (Expr)exp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object leq(Object exp, int value){
		try{
			return  vc.leExpr((Expr)exp, vc.ratExpr(value));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object leq(Object exp1, Object exp2){
		try{
			return  vc.leExpr((Expr)exp1, (Expr)exp2);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object leq(double value, Object exp){
		try{
			return  vc.leExpr(vc.ratExpr(Double.toString(value), base), (Expr)exp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object leq(Object exp, double value){
		try{
			return  vc.leExpr((Expr)exp, vc.ratExpr(Double.toString(value), base));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object geq(int value, Object exp){
		try{
			return  vc.geExpr(vc.ratExpr(value), (Expr)exp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object geq(Object exp, int value){
		try{
			return  vc.geExpr((Expr)exp, vc.ratExpr(value));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object geq(Object exp1, Object exp2){
		try{
			return  vc.geExpr((Expr)exp1, (Expr)exp2);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object geq(double value, Object exp){
		try{
			return  vc.geExpr(vc.ratExpr(Double.toString(value), base), (Expr)exp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object geq(Object exp, double value){
		try{
			return  vc.geExpr((Expr)exp, vc.ratExpr(Double.toString(value), base));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object lt(int value, Object exp){
		try{
			return  vc.ltExpr(vc.ratExpr(value), (Expr)exp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object lt(Object exp, int value){
		try{
			return  vc.ltExpr((Expr)exp, vc.ratExpr(value));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object lt(Object exp1, Object exp2){
		try{
			return  vc.ltExpr((Expr)exp1, (Expr)exp2);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object lt(double value, Object exp){
		try{
			return  vc.ltExpr(vc.ratExpr(Double.toString(value), base), (Expr)exp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object lt(Object exp, double value){
		try{
			return  vc.ltExpr((Expr)exp, vc.ratExpr(Double.toString(value), base));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}


	Object gt(int value, Object exp){
		try{
			return  vc.gtExpr(vc.ratExpr(value), (Expr)exp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object gt(Object exp, int value){
		try{
			return  vc.gtExpr((Expr)exp, vc.ratExpr(value));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object gt(Object exp1, Object exp2){
		try{
			return  vc.gtExpr((Expr)exp1, (Expr)exp2);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object implies(Object exp1, Object exp2){
		try{
			return  vc.impliesExpr((Expr)exp1, (Expr)exp2);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object gt(double value, Object exp){
		try{
			return  vc.gtExpr(vc.ratExpr(Double.toString(value), base), (Expr)exp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}

	Object gt(Object exp, double value){
		try{
			return  vc.gtExpr((Expr)exp, vc.ratExpr(Double.toString(value), base));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);

	    }
	}




	Object plus(int value, Object exp) {
		try{
			return  vc.plusExpr(vc.ratExpr(value), (Expr)exp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	Object plus(Object exp, int value) {
		try{
			return  vc.plusExpr((Expr)exp, vc.ratExpr(value));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	Object plus(Object exp1, Object exp2) {
		try{
			return  vc.plusExpr((Expr)exp1, (Expr)exp1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	Object plus(double value, Object exp) {
		try{
			return  vc.plusExpr(vc.ratExpr(Double.toString(value), base), (Expr)exp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	Object plus(Object exp, double value) {
		try{
			return  vc.plusExpr((Expr)exp, vc.ratExpr(Double.toString(value), base));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	Object minus(int value, Object exp) {
		try{
			return  vc.minusExpr(vc.ratExpr(value), (Expr)exp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	Object minus(Object exp, int value) {
		try{
			return  vc.minusExpr((Expr)exp, vc.ratExpr(value));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	Object minus(Object exp1, Object exp2) {
		try{
			return  vc.minusExpr((Expr)exp1, (Expr)exp2);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	Object minus(double value, Object exp) {
		try{
			return  vc.minusExpr(vc.ratExpr(Double.toString(value), base), (Expr)exp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	Object minus(Object exp, double value) {
		try{
			return  vc.minusExpr((Expr)exp, vc.ratExpr(Double.toString(value), base));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	Object mult(int value, Object exp) {
		try{
			return  vc.multExpr(vc.ratExpr(value), (Expr)exp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	Object mult(Object exp, int value) {
		try{
			return  vc.multExpr((Expr)exp, vc.ratExpr(value));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	Object mult(Object exp1, Object exp2) {
		try{
			return  vc.multExpr((Expr)exp1, (Expr)exp2);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}
	Object mult(double value, Object exp) {
		try{
			return  vc.multExpr(vc.ratExpr(Double.toString(value), base), (Expr)exp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}
	Object mult(Object exp, double value) {
		try{
			return  vc.multExpr((Expr)exp, vc.ratExpr(Double.toString(value), base));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	//TODO

	Object div(int value, Object exp) {
		try{
			return  vc.divideExpr(vc.ratExpr(value), (Expr)exp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	Object div(Object exp, int value) {
		try{
			return  vc.divideExpr((Expr)exp, vc.ratExpr(value));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	Object div(Object exp1, Object exp2) {
		try{
			return  vc.divideExpr((Expr)exp1, (Expr)exp2);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}
	Object div(double value, Object exp) {
		try{
			return  vc.divideExpr(vc.ratExpr(Double.toString(value), base), (Expr)exp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}
	Object div(Object exp, double value) {
		try{
			return  vc.divideExpr((Expr)exp, vc.ratExpr(Double.toString(value), base));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}
	
	

	// not yet done for CVC3
//	Object sin(Object exp) {
//		throw new RuntimeException("## Error: Math.sin not supported");
//	}
//	Object cos(Object exp) {
//		throw new RuntimeException("## Error: Math.cos not supported");
//	}
//
//	Object round(Object exp) {
//		throw new RuntimeException("## Error: Math.round not supported");
//	}
//	Object exp(Object exp) {
//		throw new RuntimeException("## Error: Math.exp not supported");
//	}
//	Object asin(Object exp) {
//		throw new RuntimeException("## Error: Math.asin not supported");
//
//	}
//	Object acos(Object exp) {
//		throw new RuntimeException("## Error: Math.acos not supported");
//
//	}
//	Object atan(Object exp) {
//		throw new RuntimeException("## Error: Math.atan not supported");
//
//	}
//	Object log(Object exp) {
//		throw new RuntimeException("## Error: Math.log not supported");
//
//	}
//	Object tan(Object exp) {
//		throw new RuntimeException("## Error: Math.tan not supported");
//
//	}
//	Object sqrt(Object exp) {
//		throw new RuntimeException("## Error: Math.sqrt not supported");
//
//	}
//	Object power(Object exp1, Object exp2) {
//		throw new RuntimeException("## Error: Math.power not supported");
//	}
//	Object power(Object exp1, double exp2) {
//		throw new RuntimeException("## Error: Math.power not supported");
//	}
//	Object power(double exp1, Object exp2) {
//		throw new RuntimeException("## Error: Math.power not supported");
//	}
//
//	Object atan2(Object exp1, Object exp2) {
//		throw new RuntimeException("## Error: Math.atan2 not supported");
//	}
//	Object atan2(Object exp1, double exp2) {
//		throw new RuntimeException("## Error: Math.atan2 not supported");
//	}
//	Object atan2(double exp1, Object exp2) {
//		throw new RuntimeException("## Error: Math.atan2 not supported");
//	}

	Object mixed(Object exp1, Object exp2) {  //not done yet for cvc3
		throw new RuntimeException("## Error CVC3: mixed integer/real constraint not yet implemented");
	}

	//there must be a better way to do this
	//returns the upper bound on the range
	double getRealValueInf(Object dpVar) {
		try{
			Expr exp = (Expr)dpVar;
			Type t = exp.getType();
			String bounds = t.toString();
			String s = bounds.substring(bounds.lastIndexOf('.')+1,bounds.length()-1);
			return Double.parseDouble(s);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	double getRealValue(Object dpVar) { //not done yet for cvc3
		try{
			Expr exp = (Expr)model.get((Expr)dpVar);
			Rational val = exp.getRational();
			int num = val.getNumerator().getInteger();
			int den = val.getDenominator().getInteger();
			return num/den;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	int getIntValue(Object dpVar) { //not done yet for cvc3
		try{
		Expr exp = (Expr)model.get((Expr)dpVar);
		Rational val = exp.getRational();
		return val.getInteger();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	//there must be a better way to do this
	//returns the upper bound on the range
	double getRealValueSup(Object dpVar) {
		try{
			Expr exp = (Expr)dpVar;
			Type t = exp.getType();
			String bounds = t.toString();
			String s = bounds.substring(1,bounds.indexOf('.'));
			return Double.parseDouble(s);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("## Error CVC3: Exception caught in CVC3 JNI: \n" + e);
		}
	}

	private Expr test(){
		Expr e = (Expr)makeIntVar("Z",-10, 10);
		Expr f = (Expr)makeIntVar("f", -10,10);
		Expr plus = (Expr)plus(10,e);
		Expr plus2 = (Expr)plus(1,e);
		Expr eq = (Expr)eq(plus, plus2);
		return eq;
	}

	public Boolean solve() {
        try {
			if (pb==null)
				return true;
			//Expr ex = test();
			//System.out.println("Query: " + pb.toString());
			vc.push();
			SatResult result = vc.checkUnsat(pb);
			//QueryResult result = vc.query(eq); //does not seem to work properly
			if (result == SatResult.UNSATISFIABLE) {
	            //System.out.println("Unsatisfiable (Valid)\n");
				vc.pop();
	            return false;
	        }
	        else if (result == SatResult.SATISFIABLE) {
	        	model = vc.getConcreteModel();
	        	vc.pop();
	           // System.out.println("Satisfiable (Invalid)\n");
	            return true;
	        }else{
	        	vc.pop();
	        	return false;
	        }
        }catch(Exception e){
        	e.printStackTrace();
        	throw new RuntimeException("## Error CVC3: Exception caught initializing CVC3 validity checker: \n" + e);
        }
	}

	public void post(Object constraint) {
		try{
			if (pb != null)
				pb = vc.andExpr(pb, (Expr)constraint);
			else
				pb = (Expr)constraint;
		} catch (Exception e) {
			e.printStackTrace();
        	throw new RuntimeException("## Error CVC3: Exception caught making Int Var in CVC3 ???: \n" + e);
	    }
	}

	public void postOr(Object constraint) {
		try{
			if (pb != null)
				pb = vc.orExpr(pb, (Expr)constraint);
			else
				pb = (Expr)constraint;
		} catch (Exception e) {
			e.printStackTrace();
        	throw new RuntimeException("## Error CVC3: Exception caught making Int Var in CVC3 ???: \n" + e);
	    }
	}

	Object and(int value, Object exp) {
		throw new RuntimeException("## Switch to CVC3BitVec");
	}

	Object and(Object exp, int value) {
		throw new RuntimeException("## Switch to CVC3BitVec");
	}

	Object and(Object exp1, Object exp2) {
		throw new RuntimeException("## Switch to CVC3BitVec");
	}

	Object or(int value, Object exp) {
		throw new RuntimeException("## Switch to CVC3BitVec");
	}

	Object or(Object exp, int value) {
		throw new RuntimeException("## Switch to CVC3BitVec");
	}

	Object or(Object exp1, Object exp2) {
		throw new RuntimeException("## Switch to CVC3BitVec");
	}

	Object shiftL(int value, Object exp) {
		throw new RuntimeException("## Switch to CVC3BitVec");
	}

	Object shiftL(Object exp, int value) {
		throw new RuntimeException("## Switch to CVC3BitVec");
	}

	Object shiftR(int value, Object exp) {
		throw new RuntimeException("## Switch to CVC3BitVec");
	}

	Object shiftR(Object exp, int value) {
		throw new RuntimeException("## Switch to CVC3BitVec");
	}

	Object xor(int value, Object exp) {
		throw new RuntimeException("## Switch to CVC3BitVec");
	}

	Object xor(Object exp, int value) {
		throw new RuntimeException("## Switch to CVC3BitVec");
	}

	Object xor(Object exp1, Object exp2) {
		throw new RuntimeException("## Switch to CVC3BitVec");
	}

	Object shiftL(Object exp1, Object exp2) {
		throw new RuntimeException("## Switch to CVC3BitVec");
	}

	Object shiftR(Object exp1, Object exp2) {
		throw new RuntimeException("## Switch to CVC3BitVec");
	}

	Object shiftUR(int value, Object exp) {
		throw new RuntimeException("## Switch to CVC3BitVec");
	}

	Object shiftUR(Object exp, int value) {
		throw new RuntimeException("## Switch to CVC3BitVec");
	}

	Object shiftUR(Object exp1, Object exp2) {
		throw new RuntimeException("## Switch to CVC3BitVec");
	}

}