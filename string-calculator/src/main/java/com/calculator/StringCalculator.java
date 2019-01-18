package com.calculator;

import java.text.DecimalFormat;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.calculator.constants.Constants;
import com.calculator.validator.ExpressionValidator;

public class StringCalculator {
	private ExpressionValidator validator;
	private ScriptEngine engine;
	private static StringCalculator stringCalculator;
	
	private StringCalculator() {
		validator = new ExpressionValidator();
		ScriptEngineManager mgr = new ScriptEngineManager();
	    engine = mgr.getEngineByName("JavaScript");
	}
	
	public static StringCalculator getInstance() {
		synchronized (StringCalculator.class) {
			if(stringCalculator==null) {
				stringCalculator = new StringCalculator();
			}
		}
		return stringCalculator;
	}
	public String evaluate(String expression) throws ScriptException {
		if(!validator.isValid(expression)) {
			return Constants.INVALID_EXPRESSION;
		}
		
		expression = doPriorityCalculations(expression);
		expression = evaluateInnerExpressions(expression);
		
		return expression;
	}

	/*
	 * Identifies the innermost expression if expressions are nested, otherwise 1st expression in the sequence
	 * and evaluates it using JavaScript engine. Replaces result into original expression
	 */
	private String evaluateInnerExpressions(String expression) throws ScriptException {

		StringBuilder builder = new StringBuilder(expression);
		int i = getInnerExpressionIndex(expression);
		while(i>0) {
			String innerExpression = getInnerMostExpression(expression);
			builder.delete(i, i + innerExpression.length());
			builder.insert(i, evaluateJS(innerExpression));
			expression = builder.toString();
			i = getInnerExpressionIndex(expression);
		}
		
		String strResult = evaluateJS(expression).toString();
		double result = Double.valueOf(strResult);
		if(!isInteger(strResult)) {
			DecimalFormat df = new DecimalFormat("#.00");
			strResult = df.format(result);
		}else {
			int intResult = (int)result;
			strResult = ""+intResult;
		}
		return strResult;
	}

	/*
	 * This evaluates all a^b type of expressions, replaces expression with result. 
	 * Also considers type of operands to avoid unnecessary .(decimals) in the result
	 *  (3*5^2) => (3*25)
	 *  (2.5^2) => 6.25
	 *  10^2 => 100
	 */
	private String doPriorityCalculations(String expression) {
		StringBuilder builder = new StringBuilder(expression);
		int index = expression.indexOf(Constants.POW), o1,o2;
		double d1,d2;
		String s1,s2;
		while(index>-1) {
			o1 = getO1StarIndex(builder, index);
			o2 = getO2EndIndex(builder, index);
			s1 = expression.substring(o1, index);
			s2 = expression.substring(index+1,o2);
			d1 = Double.valueOf(s1);
			d2 = Double.valueOf(s2);
			boolean isInt = isInteger(s1)&&isInteger(s2);
			double val = Math.pow(d1,d2);
			int intVal = (int)val;
			
			builder.delete(o1, o2);
			if(isInt)
				builder.insert(o1, intVal);
			else
				builder.insert(o1, val);
				
			expression = builder.toString();
			index = expression.indexOf(Constants.POW);
		}
		
		return builder.toString();
	}
	
	private boolean isInteger(String val) {
		double d = Double.valueOf(val);
		
		int i = (int)d, factor= 10000;
		int j = (int)(d*factor);
		// 2.5 -> 2*10000 == 25000
		return i*factor==j;
	}
	private int getO1StarIndex(StringBuilder builder, int p) {
		while(--p>0) {
			if(validator.isOperator(builder.charAt(p)))
				return p+1;
		}
		
		return 0;
	}
	private int getO2EndIndex(StringBuilder builder, int p) {
		while(++p<builder.length()) {
			if(validator.isOperator(builder.charAt(p)))
				return p;
		}
		
		return builder.length();
	}

	private String getInnerMostExpression(String expression) {
		String innerExpression = "";
		for(Character c: expression.toCharArray()) {
			if(Constants.VALID_START_OPERATOR.equals(c.toString())) {
				innerExpression = c.toString();
			}else if(Constants.VALID_END_OPERATOR.equals(c.toString())) {
				innerExpression += c.toString();
				return innerExpression;
			}else {
				innerExpression += c.toString();
			}
		}
		return "";
	}
	
	private int getInnerExpressionIndex(String expression) {
		int i=0, index=-1;
		for(Character c: expression.toCharArray()) {
			if(Constants.VALID_START_OPERATOR.equals(c.toString())) {
				index = i;
			}else if(Constants.VALID_END_OPERATOR.equals(c.toString())) {
				return index;
			}
			i++;
		}
		return index;
	}

	private Object evaluateJS(String expression) throws ScriptException {
	    return engine.eval(expression);
	}
}
