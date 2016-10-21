package lambda;

import java.util.HashMap;
import java.util.Map;

public class Environment {
	
	Map<String, Expression> Buffer = new HashMap<String, Expression>();
	
	public Environment(){}
	public void Blind(String name, Expression exp){
		Buffer.put(name, exp);
	}
	
	public Expression Find(String name){
		return Buffer.get(name);
	}
}
