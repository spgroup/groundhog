package br.ufpe.cin.groundhog.metrics;

import java.util.Hashtable;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.dom.*;

class GroundhogASTVisitor extends ASTVisitor{

	Statistics stat;
	Util util = new Util();

	/**
	 *	Auxiliar fields to extract metrics
	 */
	Hashtable<Integer, Integer> depthBlock = new Hashtable<Integer,Integer>();
	int methods = 0;
	int fields = 0;
	int methodCalls = 0;
	int staticMethod = 0;
	int staticField = 0;
	int cycloComplexity = 1;
	int returns = 0;

	public boolean visit(AnonymousClassDeclaration node){
		this.stat.anonymousClasses++;
		return true;
	}

	public boolean visit(TypeDeclaration td){
		if(Flags.isInterface(td.getModifiers())){
			this.stat.interfaces++;
		}else{
			this.stat.classes++;
		}

		this.fields = 0;
		this.methods = 0;
		this.staticMethod = 0;
		this.staticField = 0;

		return true;
	}

	public void endVisit(TypeDeclaration td){

		safeAddToHashTable(this.stat.fieldCounter, this.fields);
		safeAddToHashTable(this.stat.methodCounter,this.methods);
		safeAddToHashTable(this.stat.sMethodCounter,this.staticMethod);
		safeAddToHashTable(this.stat.sFieldCounter,this.staticField);
	}

	private void safeAddToHashTable(Hashtable<Integer, Integer> table,int position){

		if(table.containsKey(position)){
			table.put(position, table.get(position)+1);
		}else{
			table.put(position, 1);
		}
	}

	public boolean visit(MethodDeclaration md){
		//TODO: Perguntar a valdemir se ele pensou na logica resetando ou nao
		this.depthBlock.clear();
		this.methods++;
		this.methodCalls = 0;
		this.cycloComplexity = 1;
		this.returns = 0;
		String[] lines = md.toString().split("\n");
		safeAddToHashTable(this.stat.lineCounter,lines.length);
		int f = md.getModifiers();
		if(Flags.isStatic(f)) this.staticMethod++;
		int param = md.parameters().size();
		safeAddToHashTable(this.stat.parameters,param);
		return true;
	}

	public void endVisit(MethodDeclaration md){
		this.util.processMax(this.depthBlock);
		int maxDepth = this.util.getMax();
		this.util.clear();
		this.cycloComplexity += 2*Math.max(0, this.returns-1);
		safeAddToHashTable(this.stat.cycloCounter,this.cycloComplexity);
		safeAddToHashTable(this.stat.depCounter,maxDepth);
		safeAddToHashTable(this.stat.methodCall,this.methodCalls);
	}

	public boolean visit(MethodInvocation mi){
		this.methodCalls++;
		return true;
	}

	public boolean visit(ForStatement fs){
		this.cycloComplexity++;
		return true;
	}

	public boolean visit(WhileStatement ws){
		this.cycloComplexity++;
		return true;
	}

	public boolean visit(IfStatement is){
		this.cycloComplexity++;
		return true;
	}

	public boolean visit(ReturnStatement rs){
		this.returns++;
		return true;
	}

	public boolean visit(FieldDeclaration fd){
		this.fields++;
		if(Flags.isStatic(fd.getModifiers())){
			this.staticField++;
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

		safeAddToHashTable(this.depthBlock, c);
		return true;

	}

	public void setStatistics(Statistics stat){
		this.stat = stat;
	}

	public Statistics getStatistics(){
		Statistics to_return = this.stat;
		this.stat = null;
		return to_return;
	}
}