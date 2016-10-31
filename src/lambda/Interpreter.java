package lambda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Interpreter {
	static PrintStream LogOut;
	static{try {LogOut = new PrintStream("./tmp/log");} catch (IOException e) {}}
	
	//主循环
	public static void Loop(){
		LoadModule("./src/Main.lc");
		System.out.println("===========================================================");
		
		while(true){
			Scanner stdin = new Scanner(System.in);
			System.out.print("Eval Input : ");
			ShowResult(Eval(stdin.nextLine()));
		}
	}
	
	//加载某个文件的内容
	public static void LoadModule(String fileName){		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
			String exp =  null;
			
			while((exp = br.readLine()) != null) {
				System.out.println(exp);
				LogOut.println(exp);
				ShowResult(Eval(exp));
			}
		} catch (Exception e) {System.out.println(fileName + "catch wrong!");}
		return ;
	}
	
	private static void ShowResult(StmtResult stmtResult){
		
		String output = null;
		
		switch (stmtResult.Type) {
		case RIGHT: output = "->OK"; break;
		case WRONG: output = "->ERROR"; break;
		case AST:	output = "->"+stmtResult.Exp.Eval().toString();break;
		case SKIP:	output = null; break;
		}
		
		if(output != null){
			System.out.println(output);	
			LogOut.println(output);
		}
	}
	
	private static StmtResult Eval(String origin){
		switch(Statement.Type(origin)){
		case IMPORT:	return EvalImport(origin);
		case JAVA:		return EvalJava(origin);
		case REDUCE:	return EvalReduce(origin);
		case TMP:		return EvalTmp(origin);
		case BLIND:		return EvalBlind(origin);
		case EXPRESSION:return EvalExpression(origin);
		default: 		return new StmtResult(StmtResult.TYPE.SKIP);
		}
	}
	
	private static StmtResult EvalImport(String origin){
		String[] parts = origin.split(" ");
		Interpreter.LoadModule(parts[1].substring(1, parts[1].length()-1));
		return new StmtResult(StmtResult.TYPE.RIGHT);
	}
	
	private static StmtResult EvalJava(String origin){
		ArrayList<String> parts = Analysis.Split(origin);
		Analysis.Integer(Expression.AST(parts.get(1)));
		return new StmtResult(StmtResult.TYPE.RIGHT);
	}
	
	private static StmtResult EvalReduce(String origin){
		ArrayList<String> parts = Analysis.Split(origin);
		Expression exp = Expression.AST(parts.get(1));
		while(exp != null){
			String output = "->"+exp;
			System.out.println(output);
			LogOut.println(output);
			exp = exp.Reduce();			
		}
		return new StmtResult(StmtResult.TYPE.RIGHT);
	}
	
	private static StmtResult EvalTmp(String origin){
		LogOut.flush();
		return new StmtResult(StmtResult.TYPE.RIGHT);
	}
	
	private static StmtResult EvalBlind(String origin){
		ArrayList<String> parts = Analysis.Split(origin);
		Expression.env.Blind(parts.get(0), Expression.AST(parts.get(2)));
		return new StmtResult(StmtResult.TYPE.RIGHT);
	}
	
	private static StmtResult EvalExpression(String origin){
		return new StmtResult(Expression.AST(origin));
	}
}
