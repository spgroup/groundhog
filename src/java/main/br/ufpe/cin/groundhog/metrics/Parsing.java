package br.ufpe.cin.groundhog.metrics;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;


public class Parsing {
	
	public static Statistics parsing(String file){
		/*It's necessary to use final variables in order to interact with
		* the code inside ASTVisitor class
		*/
		final int[] depCounter = new int[4000];
		final int[] lineCounter = new int[4000];
		final int[] methodCall = new int[4000];
		final int[] methodCounter = new int[4000];
		final int[] fieldCounter = new int[4000];
		final int[] classes = new int[1];
		final int[] parameters = new int[4000];
		final int[] sMethodCounter = new int[4000];
		final int[] sFieldCounter = new int[4000];
		final int[] interfaces = new int[1];
		final int[] cycloCounter = new int[4000];
		final int[] anonymousClasses = new int[1];
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource((file).toCharArray());
		//parser.setKind(ASTParser.K_COMPILATION_UNIT);
		//parser.setResolveBindings(true);
		
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		
		ASTVisitor gg = new ASTVisitor() {
		//creating local variables to collect the metrics
			public int maximum(int[] list){
				int max=0;
				for(int i = list.length-1; i>0 && max == 0 ;i--){
					if(list[i] > 0) max=i;
				}
				return max;
			}
 			
 			int [] depthBlock=new int[1000];
			int methods = 0;
			int fields = 0;
			int methodCalls = 0;
			int staticMethod = 0;
			int staticField = 0;
			int cycloComplexity = 1;
			int returns = 0;
			
			public boolean visit(AnonymousClassDeclaration node){
				anonymousClasses[0]++;
				return true;
			}
			
			public boolean visit(TypeDeclaration td){
				if(Flags.isInterface(td.getModifiers())){
					interfaces[0]++;
				}else{
					classes[0]++;
				}
				
				fields = 0;
				methods = 0;
				staticMethod = 0;
				staticField = 0;
				
				return true;
			}
			public void endVisit(TypeDeclaration td){
				fieldCounter[fields]++;
				methodCounter[methods]++;
				sMethodCounter[staticMethod]++;
				sFieldCounter[staticField]++;
			}
			
 			public boolean visit(MethodDeclaration md){
 				depthBlock = new int[1000];
				methods++;
				methodCalls = 0;
				cycloComplexity = 1;
				returns = 0;
				String[] lines = md.toString().split("\n");
				lineCounter[lines.length]++;
				int f = md.getModifiers();
				if(Flags.isStatic(f))staticMethod++;
				int param = md.parameters().size();
				parameters[param]++;
				return true;
 			}
 			
			public void endVisit(MethodDeclaration md){
				int maxDepth = maximum(depthBlock);
				cycloComplexity += 2*Math.max(0, returns-1);
				cycloCounter[cycloComplexity]++;
				depCounter[maxDepth]++;
				methodCall[methodCalls]++;
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
				returns++;
				return true;
			}
			public boolean visit(FieldDeclaration fd){
				fields++;
				if(Flags.isStatic(fd.getModifiers()))staticField++;
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
 			
 		};
		
 		
 		cu.accept(gg);
		Statistics st = new Statistics(depCounter,lineCounter,methodCall,methodCounter,
				fieldCounter,classes,parameters,sMethodCounter,sFieldCounter,
				interfaces,cycloCounter,anonymousClasses);
		return st;
	}
}
