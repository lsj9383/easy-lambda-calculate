package lambda;

import java.util.ArrayList;

public class Statement {
	enum TYPE{
		IMPORT,
		INT,
		REDUCE,
		LOG,
		BLIND,
		EXPRESSION,
		NULL
	}
	
	public final static TYPE Type(String origin){
		if(origin.equals("")){
			return TYPE.NULL;
		}else if(origin.indexOf("@IMPORT") != -1){
			return TYPE.IMPORT;
		}else if(origin.indexOf("@INT") != -1){
			return TYPE.INT;
		}else if(origin.indexOf("@REDUCE") != -1){
			return TYPE.REDUCE;
		}else if(origin.indexOf("@LOG") != -1){
			return TYPE.LOG;
		}else if(origin.indexOf("=") != -1){
			return TYPE.BLIND;
		}else{
			return TYPE.EXPRESSION;
		}
	}
}

class StmtResult{
	enum TYPE{
		AST,
		RIGHT,
		SKIP,
		WRONG
	}
	
	public final TYPE Type;
	public final Expression Exp;
	
	public StmtResult(TYPE type){
		this.Exp = null;
		this.Type = type;
	}
	
	public StmtResult(Expression exp){
		this.Exp = exp;
		if(exp == null){
			this.Type = TYPE.WRONG;
		}else{
			this.Type = TYPE.AST;	
		}
	}
	
	
}