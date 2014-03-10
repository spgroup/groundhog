package br.ufpe.cin.groundhog.metrics;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import com.google.gson.annotations.SerializedName;

@Entity("statisticstablefile")
public class StatisticsTableFile extends StatisticsTable{
	
	@Id
	@Indexed(unique=true, dropDups=true)
	ObjectId id;
	
	@SerializedName("metric_acd")
	public long ACD;

	@SerializedName("metric_noi")
	public long NOI;

	@SerializedName("metric_not")
	public long NOT;

	@SerializedName("metric_tloc")
	public long TLOC;
	
	@Override
	public String toString() {
		return super.toString() +
				"metric_acd: " + ACD + "\n" +
				"metric_noi: " + NOI + "\n" +
				"metric_not: " + NOT + "\n" +
				"metric_tloc: " + TLOC;
	}
}
