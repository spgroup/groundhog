package br.ufpe.cin.groundhog.metrics;

import com.google.gson.annotations.SerializedName;

public abstract class StatisticsTable {

	//Values for file level
	@SerializedName("metric_fout_avg")
	public double FOUT_avg;
	@SerializedName("metric_fout_max")
	public long FOUT_max;
	@SerializedName("metric_fout_sum")
	public long FOUT_sum;

	@SerializedName("metric_mloc_avg")
	public double MLOC_avg;
	@SerializedName("metric_mloc_max")
	public long MLOC_max;
	@SerializedName("metric_mloc_sum")
	public long MLOC_sum;

	@SerializedName("metric_nbd_avg")
	public double NBD_avg;
	@SerializedName("metric_nbd_max")
	public long NBD_max;
	@SerializedName("metric_nbd_sum")
	public long NBD_sum;

	@SerializedName("metric_nof_avg")
	public double NOF_avg;
	@SerializedName("metric_nof_max")
	public long NOF_max;
	@SerializedName("metric_nof_sum")
	public long NOF_sum;

	@SerializedName("metric_nom_avg")
	public double NOM_avg;
	@SerializedName("metric_nom_max")
	public long NOM_max;
	@SerializedName("metric_nom_sum")
	public long NOM_sum;

	@SerializedName("metric_nsf_avg")
	public double NSF_avg;
	@SerializedName("metric_nsf_max")
	public long NSF_max;
	@SerializedName("metric_nsf_sum")
	public long NSF_sum;

	@SerializedName("metric_nsm_avg")
	public double NSM_avg;
	@SerializedName("metric_nsm_max")
	public long NSM_max;
	@SerializedName("metric_nsm_sum")
	public long NSM_sum;

	@SerializedName("metric_par_avg")
	public double PAR_avg;
	@SerializedName("metric_par_max")
	public long PAR_max;
	@SerializedName("metric_par_sum")
	public long PAR_sum;

	@SerializedName("metric_vg_avg")
	public double VG_avg;
	@SerializedName("metric_vg_max")
	public long VG_max;
	@SerializedName("metric_vg_sum")
	public long VG_sum;
	
	@Override
	public String toString() {
		return 
				"metric_fout_avg: " + FOUT_avg + "\n"+
				"metric_fout_max: " + FOUT_max + "\n"+
				"metric_fout_sum: " + FOUT_sum + "\n"+
				"metric_mloc_avg: " + MLOC_avg + "\n"+
				"metric_mloc_max: " + MLOC_max + "\n"+
				"metric_mloc_sum: " + MLOC_sum + "\n"+
				"metric_nbd_avg: " + NBD_avg + "\n"+
				"metric_nbd_max: " + NBD_max + "\n"+
				"metric_nbd_sum: " + NBD_sum + "\n"+
				"metric_nof_avg: " + NOF_avg + "\n"+
				"metric_nof_max: " + NOF_max + "\n"+
				"metric_nof_sum: " + NOF_sum + "\n"+
				"metric_nom_avg: " + NOM_avg + "\n"+
				"metric_nom_max: " + NOM_max + "\n"+
				"metric_nom_sum: " + NOM_sum + "\n"+
				"metric_nsf_avg: " + NSF_avg + "\n"+
				"metric_nsf_max: " + NSF_max + "\n"+
				"metric_nsf_sum: " + NSF_sum + "\n"+
				"metric_nsm_avg: " + NSM_avg + "\n"+
				"metric_nsm_max: " + NSM_max + "\n"+
				"metric_nsm_sum: " + NSM_sum + "\n"+
				"metric_par_avg: " + PAR_avg + "\n"+
				"metric_par_max: " + PAR_max + "\n"+
				"metric_par_sum: " + PAR_sum + "\n"+
				"metric_vg_avg: " + VG_avg + "\n"+
				"metric_vg_max: " + VG_max + "\n"+
				"metric_vg_sum: " + VG_sum + "\n";
	}
	
}