package br.ufpe.cin.groundhog.metrics;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import com.google.gson.annotations.SerializedName;

@Entity("statisticstablepackage")
public class StatisticsTablePackage extends StatisticsTable{

	@Id
	@Indexed(unique=true, dropDups=true)
	ObjectId id;
	
	@SerializedName("metric_nocu")
	public long NOCU;

	@SerializedName("metric_acd_avg")
	public double ACD_avg;
	@SerializedName("metric_acd_max")
	public long ACD_max;
	@SerializedName("metric_acd_sum")
	public long ACD_sum;

	@SerializedName("metric_noi_avg")
	public double NOI_avg;
	@SerializedName("metric_noi_max")
	public long NOI_max;
	@SerializedName("metric_noi_sum")
	public long NOI_sum;

	@SerializedName("metric_not_avg")
	public double NOT_avg;
	@SerializedName("metric_not_max")
	public long NOT_max;
	@SerializedName("metric_not_sum")
	public long NOT_sum;

	@SerializedName("metric_tloc_avg")
	public double TLOC_avg;
	@SerializedName("metric_tloc_max")
	public long TLOC_max;
	@SerializedName("metric_tloc_sum")
	public long TLOC_sum;
}
