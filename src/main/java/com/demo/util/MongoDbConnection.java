package com.demo.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Configuration
public class MongoDbConnection {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.mongo.mapPackage}")
	private String mapPackage;

	@Value("${spring.mongo.address}")
	private String mongoAddress;

	@Value("${spring.mongo.auth}")
	private String mongoAuth;
	
	@Value("${spring.mongo.dbName}")
	private String dbName;
	private static MongoClient mongoClient = null;

	private static Morphia morphia = new Morphia();
	private static Map<String,MongoDatabase> mongoDatabase =new HashMap<String,MongoDatabase>();

	public MongoDbConnection() {

	}

	private void mongoInit() {
		morphia.mapPackage(mapPackage);
		morphia.getMapper().getOptions().setStoreEmpties(true);
		List<ServerAddress> saList = new ArrayList<ServerAddress>();
		String[] mongoAddres = mongoAddress.split(",");
		logger.info("xxxxxxx mongoAddress = {}", mongoAddress);
		for (String mongoUrl : mongoAddres) {
			String[] mongoUrlSp = mongoUrl.split(":");
			ServerAddress sa = new ServerAddress(mongoUrlSp[0], Integer.valueOf(mongoUrlSp[1]));
			saList.add(sa);
		}
		String[] mongoAut = mongoAuth.split(",");
		MongoCredential credential = MongoCredential.createCredential(mongoAut[0], mongoAut[1],
				mongoAut[2].toCharArray());

		MongoClientOptions.Builder build = new MongoClientOptions.Builder();

		build.connectionsPerHost(50); // 与目标数据库能够建立的最大connection数量为50
		build.threadsAllowedToBlockForConnectionMultiplier(50); // 如果当前所有的connection都在使用中，则每个connection上可以有50个线程排队等待
		/*
		 * 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间为2分钟
		 * 这里比较危险，如果超过maxWaitTime都没有获取到这个连接的话，该线程就会抛出Exception
		 * 故这里设置的maxWaitTime应该足够大，以免由于排队线程过多造成的数据库访问失败
		 */
		build.maxWaitTime(1000 * 60 * 2);
		build.connectTimeout(1000 * 60 * 1); // 与数据库建立连接的timeout设置为1分钟
		mongoClient = new MongoClient(saList, Arrays.asList(credential), build.build());
	}


	private static Map<String, Datastore> datastoreMap = new HashMap<String, Datastore>();

	
	
	public MongoCollection<Document> getCollection(String collection) {
		if (mongoClient == null) {
			try {
				mongoInit();
				logger.warn("mongoClient尚未初始化，sleep 2s");
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		MongoDatabase md=null;
		if(mongoDatabase.get(dbName) ==null){
			 md= mongoClient.getDatabase(dbName);
			 mongoDatabase.put(dbName, md);
		}
		if (mongoDatabase.get(dbName) == null) {
			logger.error("db[" + dbName + "] not exists!");
			return null;
		}
		MongoCollection<Document> dc =mongoDatabase.get(dbName).getCollection(collection);
		if (dc == null) {
			logger.error("collection[" + collection + "] not exists!");
			return null;
		}
		return dc;
	}
	
	
	
	public Datastore getDatastore(String dbName) {
		if (mongoClient == null) {
			try {
				mongoInit();
				logger.warn("mongoClient尚未初始化，sleep 2s");
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		Datastore dt = datastoreMap.get(dbName);
		if (dt == null) {
			try {

				dt = morphia.createDatastore(mongoClient, dbName);
				datastoreMap.put(dbName, dt);
			} catch (Exception e) {
				logger.error("dbName={} mongoClient={}", dbName, mongoClient);
				logger.error("", e);
			}
		}
		return dt;
	}
}
