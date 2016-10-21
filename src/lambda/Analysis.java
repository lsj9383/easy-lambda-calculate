package lambda;

import java.util.ArrayList;
import java.util.Scanner;

public class Analysis {
	
	public static int Integer(Expression exp){
		Expression cal = new Call(new Call(exp, new INC()), new Number(0));
		Expression result =cal.Eval(); 
		return ((Number)result).number;
	}
	
	public static Expression Execute(String origin){
		
		if(origin.equals("")){	// ‰»Î»ÙŒ™ø’£¨∑≈∆˙
			return null;
		}else if(origin.indexOf("#IMPORT") != -1){
			String[] parts = origin.split(" ");
			Interpreter.LoadDefinition(parts[1].substring(1, parts[1].length()-1));
			return new Expression();
		}else if(origin.indexOf("#JAVA") != -1){
			ArrayList<String> parts = Expression.Split(origin);
			System.out.println(Analysis.Integer(Expression.AST(parts.get(1))));
			return new Expression();
		}else if(origin.indexOf("=") == -1){
			return Expression.AST(origin);
		}else{
			ArrayList<String> parts = Expression.Split(origin);
			Expression.env.Blind(parts.get(0), Expression.AST(parts.get(2)));
			return new Expression();
		}
	}
}
