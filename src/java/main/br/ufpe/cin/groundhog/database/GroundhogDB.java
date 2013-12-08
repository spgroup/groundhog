package br.ufpe.cin.groundhog.database;

import java.net.UnknownHostException;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import com.mongodb.MongoClient;

/**
 * This class represents the database interface for Groundhog data persistence via MongoDB
 * Read more at https://github.com/spgroup/groundhog#database-support
 * 
 * @author Rodrigo Alves
 * 
 */
public class GroundhogDB {
	private String dbName;
	private static MongoClient mongo;
    private static Datastore datastore;
    
	public GroundhogDB(String host, String dbName) throws UnknownHostException {
		this.dbName = dbName;
		this.mongo = new MongoClient(host);
		this.datastore = new Morphia().createDatastore(this.mongo, dbName);
	}
	
	/**
	 * Receives a GitHub entity object and persists it to the database
	 * @param entity, a {@link Object} representing one of the many GitHub entities covered by Groundhog
	 */
	public void save(Object entity) {
		this.datastore.save(entity);
	}
}