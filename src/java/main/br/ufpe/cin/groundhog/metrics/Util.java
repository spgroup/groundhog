package br.ufpe.cin.groundhog.metrics;

import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;

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
	
	
}
