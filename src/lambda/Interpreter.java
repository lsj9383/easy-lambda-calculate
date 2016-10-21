package lambda;

public class Interpreter {
	public static void Test(){
		
		Analysis.Execute("(lambda p (lambda x x))");
		
		Analysis.Execute("ZERO		= (lambda p (lambda x x))");
		Analysis.Execute("ONE		= (lambda p (lambda x (p x)))");
		Analysis.Execute("TWO		= (lambda p (lambda x (p (p x))))");
		Analysis.Execute("THREE	= (lambda p (lambda x (p (p (p x)))))");
		
		Analysis.Execute("INC		= (lambda n (lambda p (lambda x (p ((n p) x)))))");
		Analysis.Execute("FOUR	= (INC THREE)");
		
		System.out.println(new Variable("ZERO").Eval().toString());
		System.out.println(new Variable("ONE").Eval().toString());
		System.out.println(new Variable("TWO").Eval().toString());
		System.out.println(new Variable("THREE").Eval().toString());
		System.out.println(new Variable("FOUR").Eval().toString());
	}
}
