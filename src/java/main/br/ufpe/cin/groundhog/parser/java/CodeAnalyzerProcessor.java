package br.ufpe.cin.groundhog.parser.java;

import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("*")
public class CodeAnalyzerProcessor extends AbstractProcessor {	
    private Trees trees;
    private HashMap<String, HashMap<String, MutableInt>> counters;
    
    public CodeAnalyzerProcessor() {
    	counters = new HashMap<String, HashMap<String, MutableInt>>();
	}
    
    @Override
    public void init(ProcessingEnvironment pe) {
        super.init(pe);
        trees = Trees.instance(pe);
    }
    
    public void setCounterValue(String counterName, String value, int count) {
    	HashMap<String, MutableInt> counter = counters.get(counterName);
    	if (counter == null) {
    		counter =  new HashMap<String, MutableInt>();
    		counters.put(counterName, counter);
    	}
    	counter.put(value, new MutableInt(count));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        CodeAnalyzerTreeVisitor visitor = new CodeAnalyzerTreeVisitor(counters);

        for (Element e : roundEnvironment.getRootElements()) {
            TreePath tp = trees.getPath(e);
           	
            if (tp != null) {
                CompilationUnitTree compUnitTree = tp.getCompilationUnit();
               	int lines = countAllLines(compUnitTree.toString()); // count lines
               	setCounterValue("line count", compUnitTree.getSourceFile().getName(), lines);
               	for (ImportTree it : compUnitTree.getImports()) { // count imports
               		visitor.count("import", it.getQualifiedIdentifier().toString());
               	}
               	visitor.scan(tp, trees); // count everything else
    		}
        }
        return true;
    }
    
    public int countAllLines(String code) {
        int lines = 0; 
    	Pattern patternComment = Pattern.compile("(/\\*(?>(?:(?>[^*]+)|\\*(?!/))*)\\*/)|(//.*$)|(\"[\\s\\w]*\")", Pattern.MULTILINE);        
    	Pattern patternNewLine = Pattern.compile("\n", Pattern.MULTILINE);
    	Matcher commentMatches = patternComment.matcher(code);
        String comment;
        
        while (commentMatches.find()) {
        	comment = commentMatches.group(); 
        	Matcher mC = patternNewLine.matcher(comment);
        	while (mC.find()) {
        		--lines;
        	}
        }
    	
		Matcher newLineMatches = patternNewLine.matcher(code);
	    while (newLineMatches.find()) {
	    	++lines;
	    }
	    return lines;
    }
    
    public HashMap<String, HashMap<String, MutableInt>> getCounters() {
		return counters;
	}

}