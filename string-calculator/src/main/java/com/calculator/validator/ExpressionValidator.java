package com.calculator.validator;

import java.util.Stack;
import java.util.regex.Pattern;

import com.calculator.constants.Constants;

public class ExpressionValidator {
	private static ExpressionValidator validator;
	
	private ExpressionValidator() {
	}
	
	public static ExpressionValidator getInstance() {
		
		if (validator == null) {
			synchronized (ExpressionValidator.class) {
				if (validator == null) {
					validator = new ExpressionValidator();
				}
			}
		}
		return validator;
	}
	
	public boolean hasValidCharacters(String expression) {
		expression = expression.replaceAll("\\s", "");
		return !expression.isEmpty()&&Pattern.matches("["+Constants.OPERATORS+"\\d]*", expression);
	}


	public boolean hasValidStart(String expression) {
		if(hasValidCharacters(expression)) {
			String start = expression.substring(0, 1);
			return !Constants.OPERATORS.contains(start)
					|| Constants.VALID_START_OPERATOR.contains(start);
		}
		return false;
	}

	public boolean hasValidEnd(String expression) {
		if(hasValidCharacters(expression)) {
			int l = expression.length();
			String end = expression.substring(l-1, l);
			return !Constants.OPERATORS.contains(end)
					|| Constants.VALID_END_OPERATOR.contains(end);
		}
		return false;
	}

	public boolean everyOpeningHasClosing(String expression) {
		if(hasValidStart(expression)&&hasValidEnd(expression)) {
			Stack<Character> stack = new Stack<>();
			for(Character c: expression.toCharArray()) {
				if(Constants.VALID_START_OPERATOR.equals(c.toString())) {
					stack.push(c);
				}else if(Constants.VALID_END_OPERATOR.equals(c.toString())) {
					if(stack.isEmpty())
						return false;
					stack.pop();
				}
				
			}
			return stack.isEmpty();
		}
		return false;
	}

	public boolean noAdjacentOperators(String expression) {
		if(everyOpeningHasClosing(expression)) {
			for(int i=0; i<expression.length(); i++) {
				
				if(i+1<expression.length()) {
					Character c1 = expression.charAt(i);
					Character c2 = expression.charAt(i+1);
					
					if(isOperand(c1) && isOperator(c2)
							&& isValidStartChar(c2)) {
							return false;
					}else if(isOperator(c1) && isOperand(c2)
								&& isValidEndChar(c1)) {
							return false;
					}
					if(isOperator(c1) && isOperator(c2)
						&& !isValidStartChar(c1) && !isValidEndChar(c1)
						&& !isValidStartChar(c2) && !isValidEndChar(c2)) {
						return false;
					}
					
				}
			}
			return true;
		}
		return false;
	}
	
	
	
	private boolean isValidStartChar(Character c) {
		return Constants.VALID_START_OPERATOR.equals(c.toString());
	}
	
	private boolean isValidEndChar(Character c) {
		return Constants.VALID_END_OPERATOR.equals(c.toString());
	}

	public boolean isOperator(Character str) {
//		return Pattern.matches("["+Constants.OPERATORS+"]", str.toString());
		for(Character c : Constants.OPERATORS.toCharArray()) {
			if(c.equals(str))
				return true;
		}
		return false;
	}
	
	private boolean isOperand(Character str) {
		return Pattern.matches("[\\d]", str.toString());
	}
	
	public boolean isValid(String expression) {
		return noAdjacentOperators(expression);
	}
}

