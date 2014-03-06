package br.ufpe.cin.groundhog.metrics;

import java.net.UnknownHostException;
import java.util.List;

import org.mongodb.morphia.Key;

import br.ufpe.cin.groundhog.database.GroundhogDB;
import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaFileException;

import com.mongodb.DB;
import com.mongodb.DBObject;

public class TesteMongoDB {

	public static void main(String[] args) throws UnknownHostException, InvalidJavaFileException {
//		String dbName = "java_metrics";
//		MongoClient client = new MongoClient("127.0.0.1");
//		Morphia morphia = new Morphia();
//		Datastore datastore = morphia.createDatastore(client, dbName);     
		//DB db = ghdb.getMongo().getDB("java_metrics");

		GroundhogDB ghdb = new GroundhogDB("127.0.0.1", "java_metrics");
		ghdb.getMapper().mapPackage("br.ufpe.cin.groundhog.metrics");
				
		JavaFile file = new JavaFile("/home/bruno/scm/github.com/groundhog2/src/java/main/br/ufpe/cin/groundhog/metrics/JavaFile.java");
		JavaFile file2 = new JavaFile("/home/bruno/scm/github.com/groundhog2/src/java/main/br/ufpe/cin/groundhog/metrics/JavaPackage.java");
		StatisticsTableFile table = new StatisticsTableFile();
//		DBObject object = ghdb.getMapper().toDBObject(file);
		//DBObject object2 = ghdb.getMapper().toDBObject(file2);
//		System.out.println(db.getCollection("files").save(object));

		Key<StatisticsTableFile> key_f = ghdb.save(table);
		
		file2.setTable(table);
				
		Key<JavaFile> key_jf = ghdb.save(file2);
		System.out.println(key_jf);
		
		
	}
}
