package br.ufpe.cin.groundhog.metrics;

import java.util.Hashtable;

public class Statistics {
	/*It's necessary to use final variables in order to interact with
	* the code inside ASTVisitor class
	*/
	
	/**
	 * Accumulators for method metrics
	 */
	
	public Hashtable<Integer, Integer> methodCall = new Hashtable<Integer,Integer>();
	public Hashtable<Integer, Integer> lineCounter = new Hashtable<Integer,Integer>();
	public Hashtable<Integer, Integer> depCounter = new Hashtable<Integer,Integer>();
	public Hashtable<Integer, Integer> parameters = new Hashtable<Integer,Integer>();
	public Hashtable<Integer, Integer> cycloCounter = new Hashtable<Integer,Integer>();
	
	/**
	 * Accumulators for classes metrics	
	 */
	public Hashtable<Integer, Integer> fieldCounter = new Hashtable<Integer,Integer>();
	public Hashtable<Integer, Integer> methodCounter = new Hashtable<Integer,Integer>();
	public Hashtable<Integer, Integer> sFieldCounter = new Hashtable<Integer,Integer>();
	public Hashtable<Integer, Integer> sMethodCounter = new Hashtable<Integer,Integer>();

	/**
	 * Accumulators for files metrics
	 */
	public long anonymousClasses = 0;
	public long interfaces = 0;
	public long classes = 0;
	
	public void merge(Statistics statistics){
		this.methodCall = Util.mergeHashTable(this.methodCall, statistics.methodCall);
		this.lineCounter = Util.mergeHashTable(this.lineCounter, statistics.lineCounter);
		this.depCounter = Util.mergeHashTable(this.depCounter, statistics.depCounter);
		this.parameters = Util.mergeHashTable(this.parameters, statistics.parameters);
		this.cycloCounter = Util.mergeHashTable(this.cycloCounter, statistics.cycloCounter);
		this.fieldCounter = Util.mergeHashTable(this.fieldCounter, statistics.fieldCounter);
		this.methodCounter = Util.mergeHashTable(this.methodCounter, statistics.methodCounter);
		this.sFieldCounter = Util.mergeHashTable(this.sFieldCounter, statistics.sFieldCounter);
		this.sMethodCounter = Util.mergeHashTable(this.sMethodCounter, statistics.sMethodCounter);
		this.anonymousClasses += statistics.anonymousClasses;
		this.interfaces += statistics.interfaces;
		this.classes += statistics.classes;
	}
	
	@Override
	public String toString() {
		return "methodCall" + methodCall + "\n" +
				"lineCounter" + lineCounter + "\n" +
				"depCounter" + depCounter + "\n" +
				"parameters" + parameters + "\n" +
				"cycloCounter" + cycloCounter + "\n" +
				"fieldCounter" + fieldCounter + "\n" +
				"methodCounter" + methodCounter + "\n" +
				"sFieldCounter" + sFieldCounter + "\n" +
				"sMethodCounter" + sMethodCounter;
	}
}
