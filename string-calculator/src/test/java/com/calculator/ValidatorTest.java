package com.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.calculator.validator.ExpressionValidator;

class ValidatorTest {
	private ExpressionValidator validator ;
	
	@BeforeEach
	void setUp() throws Exception {
		validator = ExpressionValidator.getInstance();
	}

	@Test
	void test_validCharacters_01() {
		String expression="7+(67(56*2) )";
		assertEquals(true, validator.hasValidCharacters(expression));
	}
	
	@Test
	void test_validCharacters_02() {
		String expression="8*+7";
		assertEquals(true, validator.hasValidCharacters(expression));
	}
	
	@Test
	void test_validCharacters_03() {
		String expression="3+a";
		assertEquals(false, validator.hasValidCharacters(expression));
	}
	
	@Test
	void test_validCharacters_04() {
		String expression="";
		assertEquals(false, validator.hasValidCharacters(expression));
	}
	
	@Test
	void test_operator_01() {
		Character expression='.';
		assertEquals(false, validator.isOperator(expression));
	}
	
	
	
	
	
	@Test
	void test_canNotStartWithOperator_05() {
		String expression="7+(67(56*2) )";
		assertEquals(true, validator.hasValidStart(expression));
	}
	@Test
	void test_canNotStartWithOperator_06() {
		String expression="+7+(67(56*2) )";
		assertEquals(false, validator.hasValidStart(expression));
	}
	@Test
	void test_canNotStartWithOperatorException_07() {
		String expression="(3+7)";
		assertEquals(true, validator.hasValidStart(expression));
	}
	@Test
	void test_canNotStartWithOperator_08() {
		String expression=")3+7)";
		assertEquals(false, validator.hasValidStart(expression));
	}
	
	
	@Test
	void test_canNotEndWithOperator_09() {
		String expression="7+3";
		assertEquals(true, validator.hasValidEnd(expression));
	}
	@Test
	void test_canNotEndWithOperator_10() {
		String expression="7+3*";
		assertEquals(false, validator.hasValidEnd(expression));
	}
	@Test
	void test_canNotEndWithOperatorException_11() {
		String expression="(7+3)";
		assertEquals(true, validator.hasValidEnd(expression));
	}
	@Test
	void test_canNotEndWithOperator_12() {
		String expression="7+3(";
		assertEquals(false, validator.hasValidEnd(expression));
	}
	
	
	@Test
	void test_everyOpeningHasClosing_13() {
		String expression="(7+3)";
		assertEquals(true, validator.everyOpeningHasClosing(expression));
	}
	@Test
	void test_everyOpeningHasClosing_14() {
		String expression="(7+3*(2*5))";
		assertEquals(true, validator.everyOpeningHasClosing(expression));
	}
	@Test
	void test_everyOpeningHasClosing_15() {
		String expression="(7+3*5))";
		assertEquals(false, validator.everyOpeningHasClosing(expression));
	}
	@Test
	void test_everyOpeningHasClosing_16() {
		String expression="(7+3*5(";
		assertEquals(false, validator.everyOpeningHasClosing(expression));
	}
	@Test
	void test_everyOpeningHasClosing_17() {
		String expression="7+3*5)";
		assertEquals(false, validator.everyOpeningHasClosing(expression));
	}
	
	
	@Test
	void test_adjacentOperator_18() {
		String expression="(7+3)5";
		assertEquals(false, validator.noAdjacentOperators(expression));
	}
	@Test
	void test_adjacentOperator_19() {
		String expression="((7+3)*5)";
		assertEquals(true, validator.noAdjacentOperators(expression));
	}
	@Test
	void test_adjacentOperator_20() {
		String expression="(2(7+3))";
		assertEquals(false, validator.noAdjacentOperators(expression));
	}
	
	
	@Test
	void test_invalid_21() {
		String expression="7+(67(56*2))";
		assertEquals(false, validator.noAdjacentOperators(expression));
	}
	@Test
	void test_invalid_22() {
		String expression="8*+7";
		assertEquals(false, validator.noAdjacentOperators(expression));
	}
	
}
