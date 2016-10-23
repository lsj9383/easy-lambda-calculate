package lambda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class Interpreter {
	static PrintStream tmpOut;
	static{
		try {
			tmpOut = new PrintStream("./tmp/data");
		} catch (IOException e) {}
	}
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
				tmpOut.println(exp);
				ShowExpression(Analysis.Execute(exp));
				exp = br.readLine();
			}
		} catch (Exception e) {System.out.println(fileName + "catch wrong!");}
		return ;
	}
	
	public static void SaveTmp(){
		try {
			tmpOut.close();
			tmpOut = new PrintStream(new FileOutputStream("./tmp/data", true));
		} catch (FileNotFoundException e) {}
	}
	
	public static void DisplayReduce(Expression exp){
		while(exp != null){
			String output = "->"+exp;
			System.out.println(output);
			tmpOut.println(output);
			exp = exp.Reduce();			
		}
	}
	
	private static void ShowExpression(Expression EXP){
		if(EXP != null){
			if(EXP.Eval() != null){
				String output = "->"+EXP.Eval().toString();
				System.out.println(output);	
				tmpOut.println(output);
			}
		}
	}
}
