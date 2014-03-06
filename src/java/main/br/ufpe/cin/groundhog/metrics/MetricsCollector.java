package br.ufpe.cin.groundhog.metrics;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public final class MetricsCollector {
	
	private long max;
	private double avg;
	private long total;

	
	private void process(Hashtable<Integer, Integer> table, long fileCount){
		long denominator = 0;
		long numerator = 0;

		for (Map.Entry<Integer, Integer> map : table.entrySet()){
			denominator += map.getValue();
			numerator += (map.getKey() * map.getValue());
			if(this.max < map.getKey()) this.max = map.getKey();
		}
		
		if(fileCount > 1) this.avg = Util.safeCalculateAvg(numerator, fileCount);
		else this.avg = Util.safeCalculateAvg(numerator, denominator);
		
		this.total = numerator;
	}

	private void clear(){
		this.max = 0;
		this.avg = 0.0;
		this.total = 0;
	}
	
	private void commonProcess(StatisticsTable table, Statistics stat){
		clear();
		process(stat.methodCall,stat.fileCount);
		table.FOUT_avg = this.avg;
		table.FOUT_max = this.max;
		table.FOUT_sum = this.total;
		
		clear();
		process(stat.lineCounter,stat.fileCount);
		table.MLOC_avg = this.avg;
		table.MLOC_max = this.max;
		table.MLOC_sum = this.total;
		
		clear();
		process(stat.depCounter,stat.fileCount);
		table.NBD_avg = this.avg;
		table.NBD_max = this.max;
		table.NBD_sum = this.total;
		
		clear();
		process(stat.fieldCounter,stat.fileCount);
		table.NOF_avg = this.avg;
		table.NOF_max = this.max;
		table.NOF_sum = this.total;
		
		clear();
		process(stat.sFieldCounter,stat.fileCount);
		table.NSF_avg = this.avg;
		table.NSF_max = this.max;
		table.NSF_sum = this.total;
		
		clear();
		process(stat.methodCounter,stat.fileCount);
		table.NOM_avg = this.avg;
		table.NOM_max = this.max;
		table.NOM_sum = this.total;
		
		clear();
		process(stat.sMethodCounter,stat.fileCount);
		table.NSM_avg = this.avg;
		table.NSM_max = this.max;
		table.NSM_sum = this.total;
		
		clear();
		process(stat.parameters,stat.fileCount);
		table.PAR_avg = this.avg;
		table.PAR_max = this.max;
		table.PAR_sum = this.total;
		
		clear();
		process(stat.cycloCounter,stat.fileCount);
		table.VG_avg = this.avg;
		table.VG_max = this.max;
		table.VG_sum = this.total;
	}
	
	public void processFileLevel(StatisticsTableFile table, Statistics stat){
		
		commonProcess(table, stat);		
		table.ACD = stat.anonymousClasses;
		table.NOI = stat.interfaces;
		table.NOT = stat.classes;
		table.TLOC = stat.totalCode;
	}
	
	public void processPackageLevel(StatisticsTablePackage table, List<Statistics> stats){
		
		//Join all statistics to obtain package level metrics
		Statistics temp = new Statistics(true);
		for (Statistics st : stats) {
			temp.merge(st);
		}
		
		commonProcess(table, temp);
		
		table.ACD_sum = temp.anonymousClasses;
		table.ACD_max = temp.anonymousClasses_max;
		table.ACD_avg = Util.safeCalculateAvg(temp.anonymousClasses, temp.fileCount);
		
		table.NOI_sum = temp.interfaces;
		table.NOI_max = temp.interfaces_max;
		table.NOI_avg = Util.safeCalculateAvg(temp.interfaces, temp.fileCount);
		
		table.NOT_sum = temp.classes;
		table.NOT_max = temp.classes_max;
		table.NOT_avg = Util.safeCalculateAvg(temp.classes, temp.fileCount);
		
		table.TLOC_sum = temp.totalCode;
		table.TLOC_max = temp.totalCode_max;
		table.TLOC_avg = Util.safeCalculateAvg(temp.totalCode, temp.fileCount);
		
		table.NOCU = temp.compilationUnits;
		
	}
	
}

