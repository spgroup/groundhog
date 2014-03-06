package br.ufpe.cin.groundhog.metrics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;

import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;

public class Util {
	
	/**
	 * Merge Hashtable ht2 onto ht1
	 * @param ht1
	 * @param ht2
	 */
	public static Hashtable<Integer, Integer> mergeHashTable(Hashtable<Integer, Integer> ht1, Hashtable<Integer, Integer> ht2){
		for (Map.Entry<Integer, Integer> map : ht2.entrySet()){
			if(ht1.containsKey(map.getKey())){
				Util.safeAddToHashTable(ht1, map.getKey(), map.getValue());
			}else{
				ht1.put(map.getKey(), map.getValue());
			}
		}
		
		return ht1;
	}
	
	public static void safeAddToHashTable(Hashtable<Integer, Integer> table,int position){

		if(table.containsKey(position)){
			table.put(position, table.get(position)+1);
		}else{
			table.put(position, 1);
		}
	}
		
	public static void safeAddToHashTable(Hashtable<Integer, Integer> table, int key, int value){

		if(table.containsKey(key)){
			table.put(key, table.get(key)+value);
		}else{
			table.put(key, value);
		}
	}
	
	/**
	 * Safe add the passed value to the top element of a stack
	 * @param stack
	 * @param value
	 * @return
	 */
	public static int safeAddStackTop(Stack<Integer> stack, int value){
		
		int to_return = 0;
		
		if(!stack.empty()){
			to_return = stack.pop();
			stack.push(to_return+value);
		}
		
		return to_return;
	}
	
	
	public static int countCodeLines(String source){
		String temp = source.trim();
		//Because we can get many tokens from the same line and JDT compiler removes all
		//space, comment and line break in scanner, we need to create a Set to unique store
		//the line number of tokens and get total of line codes
		HashSet<Integer> set = new HashSet<>();
		//We need to process the file without whiteSpace and Commenst, but with the rest
		IScanner scanner = ToolFactory.createScanner(false,false,true,true);
		scanner.setSource(temp.toCharArray());
		int token = 0x00;

		//While don't reach the end of file count the number of lines
		try {
			
			do{
				
				//Get the next token
				token = scanner.getNextToken();
				//And them process your line number
				set.add(new Integer(scanner.getLineNumber(scanner.getCurrentTokenStartPosition())));
				
			}while(token != ITerminalSymbols.TokenNameEOF);
		} catch (InvalidInputException e) {
			return 0;
		}
		return set.size();
	}
	
	public static double safeCalculateAvg(long numerator, long denominator){
		
		BigDecimal bNumerator = new BigDecimal(numerator);
		BigDecimal bDenominator = new BigDecimal(denominator);
		
		if (denominator !=0) return bNumerator.divide(bDenominator,10,RoundingMode.HALF_EVEN).doubleValue();
		else return 0;
	}
	
}
