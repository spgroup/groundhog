package br.ufpe.cin.groundhog.metrics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Hashtable;
import java.util.Map;

public final class Util {
	
	private int max;
	private double avg;
	private long total;
	
	
	public void processAll(Hashtable<Integer, Integer> table){
		long denominator = 0;
		long numerator = 0;
		
		for (Map.Entry<Integer, Integer> map : table.entrySet()){
			denominator += map.getValue();
			numerator += (map.getKey() * map.getValue());
			if(this.max < map.getKey()) this.max = map.getKey();
		}
		
		BigDecimal bDenominator = new BigDecimal(denominator);
		BigDecimal bNumerator = new BigDecimal(numerator);
		
		this.avg = bNumerator.divide(bDenominator,10,RoundingMode.HALF_EVEN).doubleValue();
		this.total = numerator;
	}
	
	public void processMax(Hashtable<Integer, Integer> table){
		
		for (Map.Entry<Integer, Integer> map : table.entrySet()){
			if(this.max < map.getKey()) this.max = map.getKey();
		}
	}
	
	public void clear(){
		
		this.total = this.max = 0;
		this.avg = 0.0;
	}
	
	public int getMax(){
		return this.max;
	}
	
	public double getAvg(){
		return this.avg;
	}
	
	public long getTotal(){
		return this.total;
	}
}

