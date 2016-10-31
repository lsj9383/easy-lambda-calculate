package lambda;

import java.util.HashMap;
import java.util.Map;

public class Environment {
	
	Map<String, Expression> Buffer = new HashMap<String, Expression>();
	private static Environment Instance=null;
	
	
	private Environment(){}
	
	public static Environment EnvInstance(){
		if(Instance==null){
			Instance = new Environment();
		}
		return Instance;
	}
	
	public void Blind(String name, Expression exp){
		Buffer.put(name, exp);
	}
	
	public Expression Find(String name){
		if(!Buffer.containsKey(name)){
			return null;
		}
		return Buffer.get(name);
	}
	
	@Override
	public String toString(){
		return Buffer.toString();
	}
}
