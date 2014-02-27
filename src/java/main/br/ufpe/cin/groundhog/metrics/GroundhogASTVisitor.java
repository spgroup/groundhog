package br.ufpe.cin.groundhog.metrics;

import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.dom.*;

class GroundhogASTVisitor extends ASTVisitor{
	
	/*It's necessary to use final variables in order to interact with
	* the code inside ASTVisitor class
	*/
	
	/**
	 * Accumulators for method metrics
	 */
	
	private Hashtable<Integer, Integer> methodCall = new Hashtable<Integer,Integer>();
	private Hashtable<Integer, Integer> lineCounter = new Hashtable<Integer,Integer>();
	private Hashtable<Integer, Integer> depCounter = new Hashtable<Integer,Integer>();
	private Hashtable<Integer, Integer> parameters = new Hashtable<Integer,Integer>();
	private Hashtable<Integer, Integer> cycloCounter = new Hashtable<Integer,Integer>();
	
	/**
	 * Accumulators for classes metrics	
	 */
	private Hashtable<Integer, Integer> fieldCounter = new Hashtable<Integer,Integer>();
	private Hashtable<Integer, Integer> methodCounter = new Hashtable<Integer,Integer>();
	private Hashtable<Integer, Integer> sFieldCounter = new Hashtable<Integer,Integer>();
	private Hashtable<Integer, Integer> sMethodCounter = new Hashtable<Integer,Integer>();

	/**
	 * Accumulators for files metrics
	 */
	private long anonymousClasses = 0;
	private long interfaces = 0;
	private long classes = 0;
	
	/**
	 *	Auxiliar fields to extract metrics
	 */
	int [] depthBlock = new int[1000];
	int methods = 0;
	int fields = 0;
	int methodCalls = 0;
	int staticMethod = 0;
	int staticField = 0;
	int cycloComplexity = 1;
	int returns = 0;


	private int maximum(int[] list){
		
		int max=0;
		
		for(int i = list.length-1; (i > 0) && (max == 0); i--){
			if(list[i] > 0) max=i;
		}
		
		return max;
	}
	
	public boolean visit(AnonymousClassDeclaration node){
		this.anonymousClasses++;
		return true;
	}
	
	public boolean visit(TypeDeclaration td){
		if(Flags.isInterface(td.getModifiers())){
			this.interfaces++;
		}else{
			this.classes++;
		}
		
		fields = 0;
		methods = 0;
		staticMethod = 0;
		staticField = 0;
		
		return true;
	}

	public void endVisit(TypeDeclaration td){
		
		safeAddToHashTable(fieldCounter, fields);
		safeAddToHashTable(methodCounter,methods);
		safeAddToHashTable(sMethodCounter,staticMethod);
		safeAddToHashTable(sFieldCounter,staticField);
	}
	
	private void safeAddToHashTable(Hashtable<Integer, Integer> table,int position){
		
		if(table.containsKey(position)){
			table.put(position, table.get(position)+1);
		}
	}
	
	public boolean visit(MethodDeclaration md){
		depthBlock = new int[1000];
		methods++;
		methodCalls = 0;
		cycloComplexity = 1;
		returns = 0;
		String[] lines = md.toString().split("\n");
		safeAddToHashTable(lineCounter,lines.length);
		int f = md.getModifiers();
		if(Flags.isStatic(f))staticMethod++;
		int param = md.parameters().size();
		safeAddToHashTable(parameters,param);
		return true;
	}
		
	public void endVisit(MethodDeclaration md){
		int maxDepth = maximum(depthBlock);
		cycloComplexity += 2*Math.max(0, returns-1);
		safeAddToHashTable(cycloCounter,cycloComplexity);
		safeAddToHashTable(depCounter,maxDepth);
		safeAddToHashTable(methodCall,methodCalls);
	}
	
	public boolean visit(MethodInvocation mi){
		methodCalls++;
			return true;
	}

	public boolean visit(ForStatement fs){
		cycloComplexity++;
		return true;
	}

	public boolean visit(WhileStatement ws){
		cycloComplexity++;
		return true;
	}

	public boolean visit(IfStatement is){
		cycloComplexity++;
		return true;
	}

	public boolean visit(ReturnStatement rs){
		this.returns++;
		return true;
	}

	public boolean visit(FieldDeclaration fd){
		this.fields++;
		if(Flags.isStatic(fd.getModifiers())){
			staticField++;
		}
			return true;
	}
	
	public boolean visit(Block node){
		int c = 0;
		ASTNode nd = node;

		while(nd.getParent() != null){
			if(nd.getClass().getName().endsWith("Block"))c++;
			nd = nd.getParent();
		}
		
		depthBlock[c] = 1;
		return true;
		
	}
	
}