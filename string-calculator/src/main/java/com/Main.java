package com;
import java.util.Scanner;

import javax.script.ScriptException;

import com.calculator.StringCalculator;

public class Main {

	public static void main(String[] args) {
		System.out.print("Enter an number of test caes (integer) followed by <enter>: ");
		Scanner reader = new Scanner(System.in);
		int t = reader.nextInt();
		
		if(t<1 || t>100) {
			System.out.println("System supports 1-100 test cases for now");
			reader.close();
			return ;
		}
			
		String[] inputs = new String[t];
		
		for(int j=0; j<t; j++) {
			inputs[j]=reader.next();
		}
		reader.close();
		
		StringCalculator calculator = new StringCalculator();
		for(int j=0; j<t; j++) {
			try {
				System.out.println("Case #"+(j+1)+": "+calculator.evaluate(inputs[j]));
			} catch (ScriptException e) {
				System.out.println("Case #"+(j+1)+": ScriptException occured while evaluation. "+e.getMessage());
			}
		}
	}

}
