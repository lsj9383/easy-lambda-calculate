package lambda;

import java.util.HashMap;
import java.util.Map;

public class Environment {
	
	Map<String, Expression> Buffer = new HashMap<String, Expression>();
	private static Environment env=null;
	
	
	private Environment(){}
	
	public static Environment EnvInstance(){
		if(env==null){
			env = new Environment();
		}
		return env;
	}
	
	public void Blind(String name, Expression exp){
		Buffer.put(name, exp);
	}
	
	public Expression Find(String name){
		return Buffer.get(name);
	}
	
	@Override
	public String toString(){
		return Buffer.toString();
	}
}
