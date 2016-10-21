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
	
	protected Expression Replace(Variable origin, Expression replacement){return null;}
	public Expression Eval(){return null;}
	public Expression Reduce(){return null;}
	
	public boolean canCall(){return false;}
	public boolean canReduce(){return false;}
	public boolean isNumber(){ return false; }
	public boolean isInc(){return false;}
	
	public static Expression AST(String origin){
		ArrayList<String> parts = Analysis.Split(origin);
		
		if(parts.size()==0){
			return null;
		}else if(parts.size() == 1){
			return new Variable(origin);
		}else if(parts.get(1).equals("lambda")){			//Function
			return new Function((Variable)AST(parts.get(2)), AST(parts.get(3)));
		}else if(parts.get(0).equals("(")){					//Call
			return new Call(AST(parts.get(1)), AST(parts.get(2)));
		}else{
			return null;
		}
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
	protected Expression Replace(Variable origin, Expression replacement) {
		return this;
	}
	
	@Override
	public boolean isInc(){
		return true;
	}
	
	@Override
	public String toString(){
		return "INC";
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
	public Expression Reduce() {
		return env.Find(name);
	}
	
	@Override
	public Expression Eval(){
		return env.Find(name).Eval();
	}
	
	@Override
	protected Expression Replace(Variable origin, Expression replacement){
		if(name.equals(origin.name)){
			return replacement;
		}else{
			return this;
		}
	}
	
	@Override
	public boolean canReduce() {
		return true;
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
	public Expression Eval(){
		return this;
	}
	
	@Override
	protected Expression Replace(Variable origin, Expression replacement) {	
		if(parameter.name.equals(origin.name)){	//约束变量不替换
			return this;
		}else{
			return new Function(parameter, body.Replace(origin, replacement));
		}
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
	public Expression Reduce() {
		if(left.canReduce()){
			return new Call(left.Reduce(), right);
		}else if(right.canReduce()){
			return new Call(left, right.Reduce());
		}else{
			Function Left = (Function)left.Eval();
			Expression Right = right.Eval();
			Expression Result = Left.body.Replace(Left.parameter, Right);
			return Result;
		}
	}
	
	@Override
	public Expression Eval(){
		if(left.canCall()){
			Expression Left = left.Eval();
			Expression Right = right.Eval();
			return new Call(Left, Right).Eval();
		}else{
			if(!left.isInc()){
				Function Left = (Function)left.Eval();
				Expression Right = right.Eval();
				Expression tmp = Left.body.Replace(Left.parameter, Right);
				Expression result = tmp.Eval();
				return result;
			}else{
				Expression Right = right.Eval();
				return new Number(((Number)Right).number+1);
			}
		}
	}
	
	@Override
	protected Expression Replace(Variable origin, Expression replacement) {
		Expression Left = left.Replace(origin, replacement);
		Expression Right = right.Replace(origin, replacement);
		return new Call(Left, Right);
	}
	
	@Override
	public boolean canCall() {
		return true;
	}
	
	@Override
	public String toString(){
		return left.toString()+" ["+right.toString()+"]";
	}
	
	@Override
	public boolean canReduce() {
		return true;
	}
}