package lambda;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * 
 * <exp> := Indentify
 * 		 := λ.<exp> { <exp> }
 * 		 := (<exp> <exp>)
 * 
 * */

public class Expression {
	
	public static Environment env;
	static{
		env = new Environment();
	}
	
	public Expression Replace(Variable origin, Expression replacement){return null;}
	public Expression Eval(){return null;}
	
	public boolean canCall(){return false;}
	public boolean isNumber(){ return false; }
	public boolean isInc(){return false;}
	
	public static Expression AST(String origin){
		ArrayList<String> parts = Split(origin);
		
		if(parts.size() == 1){
			return new Variable(origin);
		}else if(parts.get(1).equals("lambda")){			//Function
			return new Function((Variable)AST(parts.get(2)), AST(parts.get(3)));
		}else if(parts.get(0).equals("(")){					//Call
			return new Call(AST(parts.get(1)), AST(parts.get(2)));
		}else{
			return null;
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

class Number extends Expression{
	
	int number = 0;
	
	public Number(int number){
		this.number = number;
	}
	
	@Override
	public boolean isNumber() {
		return true;
	}
	
	@Override
	public String toString() {
		return ""+number;
	}
	
	@Override
	public Expression Eval() {
		return this;
	}
}

class INC extends Expression{
	@Override
	public Expression Replace(Variable origin, Expression replacement) {
		return this;
	}
	
	@Override
	public boolean isInc(){
		return true;
	}
	
	@Override
	public String toString(){
		return "λ.n (n+1)";
	}
	
	@Override
	public Expression Eval() {
		return this;
	}
}


class Variable extends Expression{
	String name;
	
	public Variable(String name){
		this.name = name;
	}
	
	@Override
	public Expression Eval(){
		Expression expression = env.Find(name);
		Expression Result = expression.Eval();
		return env.Find(name).Eval();
	}
	
	@Override
	public Expression Replace(Variable origin, Expression replacement){
		if(name.equals(origin.name)){
			return replacement;
		}else{
			return this;
		}
	}
	
	@Override
	public String toString(){
		return name;
	}
}

class Function extends Expression{
	Variable parameter;
	Expression body;
	
	public Function(Variable parameter, Expression body){
		this.parameter = parameter;
		this.body = body;
	}
	
	@Override
	public Expression Replace(Variable origin, Expression replacement) {
		
		if(parameter.name.equals(origin.name)){	//约束变量不替换
			return this;
		}else{
			body = body.Replace(origin, replacement);
			return this;
		}
	}
	
	@Override
	public Expression Eval(){
		return this;
	}
	
	@Override
	public String toString(){
		return "λ."+parameter.toString()+" { "+body.toString()+" }";
	}
	
}

class Call extends Expression{
	Expression left;
	Expression right;
	
	public Call(Expression left, Expression right){
		this.left = left;
		this.right = right;
	}
	
	@Override
	public Expression Eval(){
		if(left.canCall()){
			left = left.Eval();
			right = right.Eval();
			return this.Eval();
		}else{
			if(!left.isInc()){
				Function fun = (Function)left.Eval();
				Expression result = fun.body.Replace(fun.parameter, right.Eval()).Eval();
				return result;
			}else{
				right = right.Eval();
				return new Number(((Number)right).number+1);
			}
		}
	}
	
	@Override
	public Expression Replace(Variable origin, Expression replacement) {
		left = left.Replace(origin, replacement);
		right = right.Replace(origin, replacement);
		return this;
	}
	
	@Override
	public boolean canCall() {
		return true;
	}
	
	@Override
	public String toString(){
		return left.toString()+" ["+right.toString()+"]";
	}
}