package br.ufpe.cin.groundhog.database;

import java.net.UnknownHostException;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.Key;

import br.ufpe.cin.groundhog.GitHubEntity;

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
	private MongoClient mongo;
    private Datastore datastore;
    private Morphia mapper;
    
	public GroundhogDB(String host, String dbName) throws UnknownHostException {
		this.dbName = dbName;
		this.mongo = new MongoClient(host);
		this.mapper = new Morphia();
		this.datastore = this.mapper.createDatastore(this.mongo, dbName);
	}
	
	public GroundhogDB(MongoClient mongo, String dbName) throws UnknownHostException {
		this.dbName = dbName;
		this.mongo = null;
		this.datastore = new Morphia().createDatastore(mongo, dbName);
	}
	
	public static void query(GitHubEntity entity, String params) {
		String entityName = entity.getClass().getSimpleName().toLowerCase();
	}
	
	/**
	 * Receives a GitHub entity object and persists it to the database
	 * @param entity, a {@link Object} representing one of the many GitHub entities covered by Groundhog
	 */
	public <T> Key<T> save(T entity) {
		return this.datastore.save(entity);
	}

	public String getDbName() {
		return dbName;
	}

	public MongoClient getMongo() {
		return this.mongo;
	}

	public Datastore getDatastore() {
		return this.datastore;
	}

	public void setDatastore(Datastore datastore) {
		this.datastore = datastore;
	}

	public Morphia getMapper() {
		return mapper;
	}

	public void setMapper(Morphia mapper) {
		this.mapper = mapper;
	}

}