package lambda;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * 
 * (lambda <exp> <exp>)
 * 
 * (<exp> <exp>)
 * 
 * */

public class Analysis {
	
	public static int Integer(Expression exp){
		Expression cal = new Call(new Call(exp, new INC()), new Number(0));
		Expression result =cal.Eval(); 
		return ((Number)result).number;
	}
	
	public static Expression Execute(String origin){
		
		if(origin.indexOf("=") == -1){
			return Expression.AST(origin);
		}else{
			ArrayList<String> parts = Expression.Split(origin);
			Expression.env.Blind(parts.get(0), Expression.AST(parts.get(2)));
			return new Expression();
		}
	}
}
