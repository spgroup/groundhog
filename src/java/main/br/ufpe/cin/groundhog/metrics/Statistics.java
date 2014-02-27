package br.ufpe.cin.groundhog.metrics;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Statistics {
	
	/**
	 * Statistics applicable to classes methods
	 */
	
	//Number of method calls (fan out)
	final int[] mCallCount;
	//Method lines of code
	final int[] mLineCount;
	//Nested block depth
	final int[] mBlockDepth;
	//Number of parameters
	final int[] mParamCount;
	//McGabe cyclomatic complexity
	final int[] mCycloComp;

	/**
	 * Statistics applicable to classes 
	 */
	//Number of fields
	final int[] cFieldCount;
	//Number of methods
	final int[] cMethodCount;
	//Number of static fields
	final int[] csFieldCount;
	//Number of static methods
	final int[] csMethodCount;

	/**
	 * Statistics applicable to files
	 */
	//Number of anonymous type declarations
	final int[] fAnonClasses;
	//Number of classes
	final int[] fInterfacesCount;
	//Number of classes
	final int[] fClassesCount;
	
	
	
	public Statistics(final int[] mBlockDepth,
			final int[] mLineCount,
			final int[] mCallCount,
			final int[] cMethodCount,
			final int[] cFieldCount,
			final int[] fClassesCount,
			final int[]mParamCount,
			final int[] csMethodCount,
			final int[] csFieldCount,
			final int[]fInterfacesCount,
			final int[] mCycloComp,
			final int[] fAnonClasses){
		
		this.mBlockDepth = mBlockDepth;
		this.mLineCount = mLineCount;
		this.mCallCount = mCallCount;
		this.cMethodCount = cMethodCount;
		this.cFieldCount = cFieldCount;
		this.fClassesCount = fClassesCount;
		this.mParamCount = mParamCount;
		this.csMethodCount = csMethodCount;
		this.csFieldCount = csFieldCount;
		this.fInterfacesCount = fInterfacesCount;
		this.mCycloComp = mCycloComp;
		this.fAnonClasses = fAnonClasses;	
	}
	
	
	/**
	 * This function is an special case of max function.
	 * They do not return the max value in the list, although
	 * is returned the maximum index non zero of passed integer list.   
	 * 
	 * @param list a list of integer
	 * @return the position of the maximum index non zero 
	 */
	private int max(final int[] list){
		
		int max = 0;
		
		for(int i = list.length-1; (max == 0) && (i > -1) ; i--){
			if(list[i] > 0 ) max = i;
		}
		
		return max;
	}
	
	/**
	 * Return the average value of the elements in a array of integer.
	 * @param list a list of integer
	 * @return the average value of the list
	 */
	private double avg(final int[]list){
		
		BigDecimal counter = new BigDecimal(0);
		BigDecimal total = new BigDecimal(0);
		
		for(int i = 0 ; i < list.length ; i++){
			counter.add(new BigDecimal(list[i]));
			total.add(new BigDecimal(list[i]*i));
		}
		
		return  total.divide(counter,10,RoundingMode.HALF_EVEN).doubleValue();
	}
	
	/**
	 * Return  
	 * @param list
	 * @return
	 */
	private int total(final int[] list){
		
		int total = 0;
		
		for(int i = 0 ; i < list.length ; i++){
			total += list[i] * i;
		}
		
		return total;
	}
	
	/**
	 * 
	 * @return
	 */
	public int maxDepth(){
		return max(mBlockDepth);
	}
	
	/**
	 * 
	 * @return
	 */
	public double avgDepth(){
		return avg(mBlockDepth);
	}
	
	/**
	 * 
	 * @return
	 */
	public int totalDepth(){
		return total(mBlockDepth);
	}
	
	/**
	 * 
	 * @return
	 */
	public int maxLine(){
		return max(mLineCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public double avgLine(){
		return avg(mLineCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public int totalLine(){
		return total(mLineCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public int maxMethodCall(){
		return max(mCallCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public double avgMethodCall(){
		return avg(mCallCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public int totalMethodCall(){
		return total(mCallCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public int maxMethods(){
		return max(cMethodCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public double avgMethods(){
		return avg(cMethodCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public int totalMethods(){
		return total(cMethodCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public int maxFields(){
		return max(cFieldCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public double avgFields(){
		return avg(cFieldCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public int totalFields(){
		return total(cFieldCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public int Classes(){
		return fClassesCount[0];
	}
	
	/**
	 * 
	 * @return
	 */
	public int AClasses(){
		return fAnonClasses[0];
	}
	
	/**
	 * 
	 * @return
	 */
	public int Interfaces(){
		return fInterfacesCount[0];
	}
	
	/**
	 * 
	 * @return
	 */
	public int maxParameters(){
		return max(mParamCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public double avgParameters(){
		return avg(mParamCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public int totalParameters(){
		return total(mParamCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public int maxSMethods(){
		return max(csMethodCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public double avgSMethods(){
		return avg(csMethodCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public int totalSMethods(){
		return total(csMethodCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public int maxSFields(){
		return max(csFieldCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public double avgSFields(){
		return avg(csFieldCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public int totalSFields(){
		return total(csFieldCount);
	}
	
	/**
	 * 
	 * @return
	 */
	public int maxCycloComplexity(){
		return max(mCycloComp);
	}
	
	/**
	 * 
	 * @return
	 */
	public double avgCycloComplexity(){
		return avg(mCycloComp);
	}
	
	/**
	 * 
	 * @return
	 */
	public int totalCycloComplexity(){
		return total(mCycloComp);
	}

}
