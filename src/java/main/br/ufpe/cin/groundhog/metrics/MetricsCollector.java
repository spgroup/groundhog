package br.ufpe.cin.groundhog.metrics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Hashtable;
import java.util.Map;

public final class MetricsCollector {
	
	private int max;
	private double avg;
	private long total;


	public void process(Hashtable<Integer, Integer> table){
		long denominator = 0;
		long numerator = 0;

		for (Map.Entry<Integer, Integer> map : table.entrySet()){
			denominator += map.getValue();
			numerator += (map.getKey() * map.getValue());
			if(this.max < map.getKey()) this.max = map.getKey();
		}
		
		if(denominator != 0){
			BigDecimal bDenominator = new BigDecimal(denominator);
			BigDecimal bNumerator = new BigDecimal(numerator);

			this.avg = bNumerator.divide(bDenominator,10,RoundingMode.HALF_EVEN).doubleValue();
		}

		this.total = numerator;
	}

	public void clear(){
		this.max = 0;
		this.avg = 0.0;
		this.total = 0;
	}
	
	public void processAll(Statistics statistics){
		clear();
		process(statistics.methodCall);
		System.out.println("FOUT_avg: " + this.avg);
		System.out.println("FOUT_max: " + this.max);
		System.out.println("FOUT_sum: " + this.total);
		clear();
		process(statistics.lineCounter);
		System.out.println("MLOC_avg: " + this.avg);
		System.out.println("MLOC_max: " + this.max);
		System.out.println("MLOC_sum: " + this.total);
		clear();
		process(statistics.depCounter);
		System.out.println("NBD_avg: " + this.avg);
		System.out.println("NBD_max: " + this.max);
		System.out.println("NBD_sum: " + this.total);
		clear();
		process(statistics.parameters);
		System.out.println("NOF_avg: " + this.avg);
		System.out.println("NOF_max: " + this.max);
		System.out.println("NOF_sum: " + this.total);
		clear();
		process(statistics.cycloCounter);
		System.out.println("NOM_avg: " + this.avg);
		System.out.println("NOM_max: " + this.max);
		System.out.println("NOM_sum: " + this.total);
		clear();
		process(statistics.fieldCounter);
		System.out.println("NSF_avg: " + this.avg);
		System.out.println("NSF_max: " + this.max);
		System.out.println("NSF_sum: " + this.total);
		clear();
		process(statistics.methodCounter);
		System.out.println("NSM_avg: " + this.avg);
		System.out.println("NSM_max: " + this.max);
		System.out.println("NSM_sum: " + this.total);
		clear();
		process(statistics.sFieldCounter);
		System.out.println("PAR_avg: " + this.avg);
		System.out.println("PAR_max: " + this.max);
		System.out.println("PAR_sum: " + this.total);
		clear();
		process(statistics.sMethodCounter);
		System.out.println("VG_avg: " + this.avg);
		System.out.println("VG_max: " + this.max);
		System.out.println("VG_sum: " + this.total);
		
		System.out.println("ACD: " + statistics.anonymousClasses);
		System.out.println("NOI: " + statistics.interfaces);
		System.out.println("NOT: " + statistics.classes);
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

