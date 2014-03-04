package br.ufpe.cin.groundhog.metrics;

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.dom.*;

class GroundhogASTVisitor extends ASTVisitor{
	
	Statistics stat;
	MetricsCollector util = new MetricsCollector();

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

		Util.safeAddToHashTable(this.stat.fieldCounter, this.fields);
		Util.safeAddToHashTable(this.stat.methodCounter,this.methods);
		Util.safeAddToHashTable(this.stat.sMethodCounter,this.staticMethod);
		Util.safeAddToHashTable(this.stat.sFieldCounter,this.staticField);
	}

	public boolean visit(MethodDeclaration md){
		System.out.println("Metodo encontrado!");
		this.depthBlock.clear();
		this.methods++;
		this.methodCalls = 0;
		this.cycloComplexity = 1;
		this.returns = 0;
		String[] lines = md.toString().split("\n");
		Util.safeAddToHashTable(this.stat.lineCounter,lines.length);
		int f = md.getModifiers();
		if(Flags.isStatic(f)) this.staticMethod++;
		int param = md.parameters().size();
		Util.safeAddToHashTable(this.stat.parameters,param);
		return true;
	}

	public int localMax(){
		int max = 0;
		
		for (Map.Entry<Integer, Integer> map : this.depthBlock.entrySet()){
			if(max < map.getKey()) max = map.getKey();
		}
		
		return max;
	}
	
	public void endVisit(MethodDeclaration md){
				
		int maxDepth = localMax();		
		//Each return that isn't the last statement of a method is being used to calculate the Cyclomatic Complexity.
		this.cycloComplexity += 2*Math.max(0, this.returns-1);
		Util.safeAddToHashTable(this.stat.cycloCounter,this.cycloComplexity);
		Util.safeAddToHashTable(this.stat.depCounter,maxDepth);
		Util.safeAddToHashTable(this.stat.methodCall,this.methodCalls);
	}

	public boolean visit(MethodInvocation mi){
		this.methodCalls++;
		return true;
	}
	
	
	public boolean visit(ForStatement fs){
		this.cycloComplexity++;
		this.inspecionarExpressao(fs.getExpression());
		return true;
	}

	public boolean visit(WhileStatement ws){
		this.cycloComplexity++;
		this.inspecionarExpressao(ws.getExpression());
		return true;
	}

	public boolean visit(IfStatement is){
		this.cycloComplexity++;
		this.inspecionarExpressao(is.getExpression());
		return true;
	}
	
	public boolean visit(BreakStatement bs){
		this.cycloComplexity++;
		return true;
	}
	
	public boolean visit(ContinueStatement cs){
		this.cycloComplexity++;
		return true;
	}
	
	public boolean visit(ExpressionStatement es){
		this.inspecionarExpressao(es.getExpression());
		return false;
	}
	
	public boolean visit(ConditionalExpression ce) {
		cycloComplexity++;
		inspecionarExpressao(ce.getExpression());
		return true;
	}
	
	public boolean visit(DoStatement ds){
		this.cycloComplexity++;
		this.inspecionarExpressao(ds.getExpression());
		return true;
	}
	
	
	public boolean visit(TryStatement ts){
		if(ts.getFinally()!=null){
			this.cycloComplexity++;
		}
		return true;
	}
	
	public boolean visit(CatchClause cc){
		this.cycloComplexity++;
		return true;
	}
	
	public boolean visit(ThrowStatement ts){
		this.cycloComplexity++;
		return true;
	}
		
	public boolean visit(SwitchCase sc){
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

		Util.safeAddToHashTable(this.depthBlock, c);
		return true;

	}
	
	public void inspecionarExpressao(Expression e) {
		if (e != null) {
			String expression = e.toString();
			char[] chars = expression.toCharArray();
			for (int i = 0; i < chars.length-1; i++) {
				char next = chars[i];
				if ((next == '&' || next == '|')&&(next == chars[i+1])) {
					this.cycloComplexity++;
				}
			}
		}
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