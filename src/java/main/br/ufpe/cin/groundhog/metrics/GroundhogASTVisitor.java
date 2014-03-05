package br.ufpe.cin.groundhog.metrics;

import java.util.Stack;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.dom.*;

class GroundhogASTVisitor extends ASTVisitor{

	Statistics stat;
	MetricsCollector util = new MetricsCollector();

	/**
	 *	Auxiliar fields to extract metrics
	 */
	int methods = 0;
	int fields = 0;
	int methodCalls = 0;
	int staticMethod = 0;
	int staticField = 0;
	int cycloComplexity = 1;
	int returns = 0;
	
	//fields used to nested block depth metric calculation
	Stack<Integer> depth = new Stack<Integer>();
	Stack<Integer> maxDepth = new Stack<Integer>();
	
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
		
		this.methods++;
		this.methodCalls = 0;
		this.cycloComplexity = 1;
		this.returns = 0;

		//nested block depth calculation
		this.depth.push(0);
		this.maxDepth.push(0);

		String[] lines = md.toString().split("\n");
		Util.safeAddToHashTable(this.stat.lineCounter,lines.length);
		int f = md.getModifiers();
		if(Flags.isStatic(f)) this.staticMethod++;
		int param = md.parameters().size();
		Util.safeAddToHashTable(this.stat.parameters,param);
		return true;
	}
	
	public void endVisit(MethodDeclaration md){
		
		this.depth.pop();
		int max = this.maxDepth.pop();
		Util.safeAddStackTop(this.depth, max);
		this.cycloComplexity += 2*Math.max(0, this.returns-1);
		Util.safeAddToHashTable(this.stat.cycloCounter,this.cycloComplexity);
		Util.safeAddToHashTable(this.stat.depCounter,max);
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
		return true;
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

		//Using an stack strategy to count the max nested block depth
		//If I visit an block, so I have one more level of code
		if(!this.depth.empty()){
			/**
			 * We also want to calculate block nested depth for methods
			 * so, the stack will have at least one element if we was visited
			 * an method before
			 */
			Util.safeAddStackTop(this.depth, 1);
		}
		
		return true;

	}

	public void endVisit(Block node) {
		/**
		 * Using an stack strategy to count the max nested block depth
		 * If I end the visit of a block, so is time to check how deep the code are.
		 * If the actual nested level is more than actual maximum, so set maximum to
		 * this new maximum.
		 */
		if(!this.depth.empty()){
			/**
			 * We also want to calculate block nested depth for methods
			 * so, the stack will have at least one element if we was visited
			 * an method before
			 * 
			 * Once I finish to visit a block, I need to reduce the nested level because
			 * we complete one sub-level of nested block
			 */
			int temp = Util.safeAddStackTop(depth, -1);
			if(temp > this.maxDepth.peek()){
				this.maxDepth.pop();
				this.maxDepth.push(temp);
			}
		}
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