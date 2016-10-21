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
			ArrayList<String> parts = Split(origin);
			System.out.println(Analysis.Integer(Expression.AST(parts.get(1))));
			return new Expression();
		}else if(origin.indexOf("#REDUCE") != -1){
			ArrayList<String> parts = Split(origin);
			DisplayReduce(Expression.AST(parts.get(1)));
			return new Expression();
		}else if(origin.indexOf("=") == -1){
			return Expression.AST(origin);
		}else{
			ArrayList<String> parts = Split(origin);
			Expression.env.Blind(parts.get(0), Expression.AST(parts.get(2)));
			return new Expression();
		}
	}
	
	private static void DisplayReduce(Expression exp){
		while(exp != null){
			System.out.println(exp);
			exp = exp.Reduce();			
		}
	}
	
	static public ArrayList<String> Split(String s){
		Scanner stdin;
		ArrayList<String> subexps = new ArrayList<String>();
		
		if(s.charAt(0)=='(' && s.charAt(s.length()-1)==')'){
			stdin = new Scanner(s.substring(1, s.length()-1));
			subexps.add("(");
		}
		else{
			stdin = new Scanner(s);
		}
		
		while(stdin.hasNext())
		{
			String new_s = stdin.next();
			int bs = LeftBracketSize(new_s);
			while(bs!=0)
			{
				new_s += " "+stdin.next();
				bs = LeftBracketSize(new_s);
			}
			subexps.add(new_s);
		}
		
		if(s.charAt(0)=='(' && s.charAt(s.length()-1)==')'){
			subexps.add(")");
		}
		
		return subexps;
	}
	
	static private int LeftBracketSize(String s){
		int res = 0;
		
		for(int i=0; i<s.length(); i++){
			if( s.charAt(i) == '('){
				res++;
			}
			else if(s.charAt(i) == ')'){
				res--;
			}
		}
		
		return res;
	}
}
