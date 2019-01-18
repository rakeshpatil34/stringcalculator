package com.calculator;

import static org.junit.jupiter.api.Assertions.*;

import javax.script.ScriptException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.calculator.StringCalculator;
import com.calculator.constants.Constants;

class CalculatorTest {
	private StringCalculator calculator =new StringCalculator();
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void INVALID_01() throws ScriptException {
		String expression = "7+(67(56*2))";
		assertEquals(Constants.INVALID_EXPRESSION,calculator.evaluate(expression));
	}
	
	@Test
	void INVALID_02() throws ScriptException {
		String expression = "8*+7";
		assertEquals(Constants.INVALID_EXPRESSION,calculator.evaluate(expression));
	}
	
	@Test
	void VALID_03() throws ScriptException {
		String expression = "7+(6*5^2+3-4/2)";
		assertEquals("158", calculator.evaluate(expression));
	}
	
	@Test
	void VALID_04() throws ScriptException {
		String expression = "(8*5/8)-(3/1)-5";
		assertEquals("-3", calculator.evaluate(expression));
	}
	
	@Test
	void INVALID_04_1() throws ScriptException {
		String expression = "25^2";
		assertEquals("625", calculator.evaluate(expression));
	}
	
	@Test
	void VALID_05() throws ScriptException {
		String expression = "2.5^2";
		assertEquals("6.25", calculator.evaluate(expression));
	}
	
	
	@Test
	void VALID_06() throws ScriptException {
		String expression = "(3.5/5)*100";
		assertEquals("70", calculator.evaluate(expression));
	}
	
	
	@Test
	void VALID_07() throws ScriptException {
		String expression = "5^2.5";
		assertEquals("55.90", calculator.evaluate(expression));
	}
	
}
