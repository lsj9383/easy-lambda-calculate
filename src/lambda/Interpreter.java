package lambda;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Interpreter {
	//主循环
	public static void Loop(){
		//1).读取文件
		LoadModule("./src/Main.lc");
		System.out.println("===========================================================");
		
		//2).控制台循环
		while(true){
			Scanner stdin = new Scanner(System.in);
			String exp = null;
			//1).输入合法字串
			do{
				System.out.print("Eval Input : ");
				exp = stdin.nextLine();
			}while(exp.equals(""));
			
			//2.)执行
			ShowExpression(Analysis.Execute(exp));
		}
	}
	
	//加载某个文件的内容
	public static void LoadModule(String fileName){		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
			
			String exp =  br.readLine();
			while(exp != null) {
				if(exp.replace(" ", "").replace("\t", "").replaceAll("\n", "").length()==0){
					exp = br.readLine();
					continue;
				}
				System.out.println(exp);
				ShowExpression(Analysis.Execute(exp));
				exp = br.readLine();
			}
		} catch (Exception e) {System.out.println(fileName + "catch wrong!");}
		return ;
	}
	
	private static void ShowExpression(Expression EXP){
		if(EXP != null){
			if(EXP.Eval() != null){
				System.out.println("->"+EXP.Eval().toString());	
			}
		}
	}
}
