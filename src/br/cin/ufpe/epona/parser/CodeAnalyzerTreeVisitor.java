package br.cin.ufpe.epona.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.SynchronizedTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

// see: http://docs.oracle.com/javase/6/docs/jdk/api/javac/tree/com/sun/source/tree/package-summary.html
public class CodeAnalyzerTreeVisitor extends TreePathScanner<Object, Trees> {

	private HashMap<String, HashMap<String, MutableInt>> counters;
	
    public CodeAnalyzerTreeVisitor(HashMap<String, HashMap<String, MutableInt>> counters) {
    	this.counters = counters;
	}
    
    private void increment(String value, HashMap<String, MutableInt> counter) {
    	MutableInt count = counter.get(value);
    	if (count == null) {
    	    counter.put(value, new MutableInt());
    	}
    	else {
    	    count.increment();
    	}
    }
    
    protected void count(String counterName, String value) {
    	HashMap<String, MutableInt> counter = counters.get(counterName);
    	if (counter == null) {
    		counter =  new HashMap<String, MutableInt>();
    		counters.put(counterName, counter);
    	}
    	increment(value, counter);
    }
        
    @Override
    public Object visitNewClass(NewClassTree c, Trees ts) {
    	ExpressionTree type = c.getIdentifier();
    	
    	if (type != null) {
    		count("new", type.toString());
    	} 
    	return super.visitNewClass(c, ts);
    }

    @Override
    public Object visitClass(ClassTree c, Trees ts) {    	
    	String className = c.getSimpleName().toString();
    	if (className.equals("")) {
    		className = "<anonymous>";
    	}
    	count("class declaration", className);
    	
    	Tree extendsClause = c.getExtendsClause();
    	if (extendsClause != null) {  	
    		count("extends", extendsClause.toString());
    	}
    	
    	List<? extends Tree> implementsInterfaces = c.getImplementsClause();    	
    	for (Tree t : implementsInterfaces) {
    		count("implements", t.toString());
    	}
    	return super.visitClass(c, ts);
    }

	private String afterDot(String s) {
		int index = s.lastIndexOf(".");		
		if (index != -1) {
			s = s.substring(index + 1);
		}
		return s;
	}
    
    @Override
    public Object visitMethodInvocation(MethodInvocationTree m, Trees ts) {
    	String mExpression = m.getMethodSelect().toString();
    	count("call", afterDot(mExpression));
    	return super.visitMethodInvocation(m, ts);
    }
	
    @Override
    public Object visitMethod(MethodTree m, Trees trees) {
    	Set<Modifier> mods =  m.getModifiers().getFlags();
    	List<? extends ExpressionTree> throwss = m.getThrows();
    	
    	count("method declaration", m.getName().toString());
    	
    	for (Modifier mod : mods) {
    		count("method modifier", mod.toString());
    	}  
    	
    	for (ExpressionTree t : throwss) {
    		count("method throws", t.toString());
    	}
        return super.visitMethod(m, trees);
    }
    
    @Override
    public Object visitSynchronized(SynchronizedTree s, Trees ts) {
    	count("synchronized", s.getExpression().toString());
    	return super.visitSynchronized(s, ts);
    }
    
    @Override
    public Object visitAnnotation(AnnotationTree a, Trees ts) {
    	count("annotation", a.getAnnotationType().toString());
    	return super.visitAnnotation(a, ts);
    }
    
}

