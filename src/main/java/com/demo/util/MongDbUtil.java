package com.demo.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MongDbUtil {
	/**
	 * 定义所有的表 OrderSeaTransportation ：海运表，枚举类
	 * 以及对应的类
	 */
	public final static String ORDERSEATRANSPORTATION = "OrderSeaTransportation";
	public final static String ORDERSEATRANSPORTATIONADMINJSON="OrderSeaTransportationAdminJson";
	public final static String SINGLECLIENTORDER = "SingleClientOrder";
	public final static String ORDERMAIN = "OrderMain";
	public final static String BUSINESSLOGIC = "BusinessLogic";
	public final static String INCOMINGPULLSTATUSLOG = "IncomingPullStatusLog";
	public final static String ORDERSEAUSERTEMPLATE = "OrderSeaUserTemplate";
	public final static String ORDERSEACOMPANYTEMPLATE = "OrderSeaCompanyTemplate";
	public final static String COBIZ = "CoBiz";
	public final static String CO = "Co";
	public final static String ORDERSEABACK = "OrderSeaBack";
	public final static String ORDERSEAUSERREMARKS = "OrderSeaUserRemarks";
	public final static String COUSER = "CoUser";
	public final static String DECLAREORDERDATAS = "DeclareOrderDatas";
	public final static String DECLAREORDERDETAIL = "DeclareOrderDetail";
	public final static String USERSTATISTIC = "UserStatistic";
	public final static String COROLE = "CoRole";
	public final static String COURL = "CoUrl";
	public final static String ENTRUSTORDER = "EntrustOrder";
	public final static String SINGLECONFIG = "SingleConfing";
	public final static String BILLRECORD = "BillRecord";
	public final static String COSTTYPE = "CostType";
	public final static String CUSTOMERRELATIONINFO = "CustomerRelationInfo";
	public final static String DOC_CLEAN_RESULT = "DocCleanResult";
	public final static String BOX_COMPARE_RESULT = "BoxCompareResult";
	public final static String SINGLE_SEND_ORDER = "SingleSendOrder";
	public final static String ORDER_MERGEBOX_RELATION = "OrderMergeBoxRelation";//订单拼箱关系表
	public final static String COCONFIG="CoConfig";
	public final static String SYSTEMCONFIG="SystemConfig";
	public final static String SYSTEMMAIL="SystemMail";
	private static Logger logger = LoggerFactory.getLogger(MongDbUtil.class);
	@Autowired
	private MongoDbConnection connection;

	/**
	 *
	 * @param collection
	 * @param map-只能是一个对象形式
	 * @return
	 */
	public int insert(String collection, Map<String, Object> map) {
		MongoCollection<Document> dc = connection.getCollection(collection);
		if (dc == null) {
			return -1;
		}
		Document bdb = new Document();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			bdb.put(entry.getKey(), entry.getValue());
		}
		try {
			dc.insertOne(bdb);
			return 1;
		} catch (Exception e) {
			logger.error("insert[" + map + "]", e);
			return -1;
		}
	}

	public int delete(String collection, Document obj) {
		MongoCollection<Document> dc = connection.getCollection(collection);
		if (dc == null) {
			return -1;
		}
		Document bdb = new Document();
		for (Map.Entry<String, Object> entry : obj.entrySet()) {
			bdb.put(entry.getKey(), entry.getValue());
		}
		try {
			dc.deleteOne(bdb);
			return 1;
		} catch (Exception e) {
			logger.error("delete[" + obj.toJson() + "]", e);
			return -1;
		}
	}

	public int insertDocument(String collection, Document bdb) {
		MongoCollection<Document> dc = connection.getCollection(collection);
		if (dc == null) {
			return -1;
		}
		try {
			dc.insertOne(bdb);
			return 1;
		} catch (Exception e) {
			logger.error("insert[" + bdb + "]", e);
			return -1;
		}
	}

	/**
	 *
	 * @param collection
	 * @param condition
	 * @return
	 */
	public Long queryLong(String collection, Document condition) {
		MongoCollection<Document> dc = connection.getCollection(collection);
		return dc.count(condition);
	}

	/**
	 *
	 * @param collection
	 * @param condition
	 * @return
	 */
	public List<Document> query(String collection, Document condition) {
		MongoCollection<Document> dc = connection.getCollection(collection);
		if (dc == null) {
			return new ArrayList<Document>();
		}

		List<Document> list = new ArrayList<Document>();
		for (MongoCursor<Document> mongoCursor = dc.find(condition, Document.class).iterator(); mongoCursor
				.hasNext();) {
			list.add(mongoCursor.next());
		}
		return list;
	}
	public List<Document> queryBySort(String collection, Document condition,Document sortDoc) {
		
		MongoCollection<Document> dc = connection.getCollection(collection);
		if (dc == null) {
			return new ArrayList<Document>();
		}
		List<Document> list = new ArrayList<Document>();
		FindIterable<Document> findDoc = dc.find(condition, Document.class);
		if (sortDoc != null) {
			findDoc.sort(sortDoc);
		}
		for (Document mc : findDoc) {
			list.add(mc);
		}
		return list;
	}

	

	public int update(String collection, Document oldObj, Document newObj) {
		findBigDecimal(newObj);
		MongoCollection<Document> dc = connection.getCollection(collection);
		if (dc == null) {
			return -1;
		}
		UpdateResult ur = dc.replaceOne(oldObj, newObj);
		return (int) ur.getModifiedCount();
	}
	public int updateJSONObject(String collection, Document oldObj, JSONObject newObj) {
		findBigDecimal(newObj);
		Document bdb = new Document(newObj);
		MongoCollection<Document> dc = connection.getCollection(collection);
		if (dc == null) {
			return -1;
		}
		UpdateResult ur = dc.replaceOne(oldObj, bdb);
		return (int) ur.getModifiedCount();
	}
	public int updateOne(String collection, Document filter, Document newObj) {
		MongoCollection<Document> dc = connection.getCollection(collection);
		if (dc == null) {
			return -1;
		}
		UpdateResult ur = dc.updateOne(filter, newObj);
		return (int) ur.getModifiedCount();
	}

	public int updateOneDoc(String collection, Document filter, Document update) {
		return (int) connection.getCollection(collection).updateOne(filter, update).getModifiedCount();
	}

	public int updateManyDoc(String collection, Document filter, Document update) {
		return (int) connection.getCollection(collection).updateMany(filter, update).getModifiedCount();
	}

	public Document queryOne(String collection, Document condition) {
		MongoCollection<Document> dc = connection.getCollection(collection);
		return dc.find(condition, Document.class).sort(new Document("createTime", -1)).first();
	}

	public Document queryOneBySort(String collection, Document condition, Document sortDoc) {
		MongoCollection<Document> dc = connection.getCollection(collection);
		FindIterable<Document> findDoc = dc.find(condition, Document.class).sort(sortDoc);
		return findDoc.first();
	}

	

	public List<Document> queryPage(String collection, Document condition, int startIndex, int pageSize) {
		return queryPage(collection, condition, startIndex, pageSize, null);
	}

	public List<Document> queryPage(String collection, Document condition, int startIndex, int pageSize,
			Document sortDoc) {
		MongoCollection<Document> dc = connection.getCollection(collection);
		if (dc == null) {
			return new ArrayList<Document>();
		}
		List<Document> list = new ArrayList<Document>();
		FindIterable<Document> findDoc = dc.find(condition, Document.class).skip(startIndex).limit(pageSize);
		if (sortDoc != null) {
			findDoc.sort(sortDoc);
		}
		for (Document mc : findDoc) {
			list.add(mc);
		}
		return list;
	}

	/**
	 *
	 * @param collection
	 * @param obj
	 * @return
	 */
	public int insert(String collection, JSONObject obj) {
		MongoCollection<Document> dc = connection.getCollection(collection);
		if (dc == null) {
			return -1;
		}
		Document bdb = new Document();
		for (Map.Entry<String, Object> entry : obj.entrySet()) {
			bdb.put(entry.getKey(), entry.getValue());
		}
		try {
			dc.insertOne(bdb);
			return 1;
		} catch (Exception e) {
			logger.error("insert[" + obj.toJSONString() + "]", e);
			return -1;
		}
	}

	public int batchInsert(String collection, JSONObject... objArray) {
		MongoCollection<Document> dc = connection.getCollection(collection);
		if (dc == null) {
			return -1;
		}
		List<Document> list = new ArrayList<Document>();
		for (JSONObject obj : objArray) {
			Document bdb = new Document();
			for (Map.Entry<String, Object> entry : obj.entrySet()) {
				bdb.put(entry.getKey(), entry.getValue());
			}
			list.add(bdb);
		}
		try {
			dc.insertMany(list);
			return objArray.length;
		} catch (Exception e) {
			logger.error("batch insert error", e);
			return -1;
		}
	}

	/**
	 * 分组查询
	 * 
	 * @param collection
	 *            集合名
	 * @param selectDoc
	 *            查询及分组条件集合
	 * @return
	 */
	public List<Document> group(String collection, List<Document> selectDoc) {
		return group(collection, selectDoc, Document.class);
	}

	public <T> List<T> group(String collection, List<Document> selectDoc, Class<T> c) {
		MongoCollection<Document> dc = connection.getCollection(collection);
		List<T> resultList = new ArrayList<>();
		for (MongoCursor<T> cursor = dc.aggregate(selectDoc, c).iterator(); cursor.hasNext();) {
			resultList.add(cursor.next());
		}
		return resultList;
	}
	
	/**
	 * 根据doc找到多条记录
	 */
	public List<JSONObject> selectMultiByDoc(String collection, Document doc) {

		List<JSONObject> result = new ArrayList<>();

		List<Document> queryResult = query(collection, doc);
		if (queryResult != null && queryResult.size() > 0) {
			for (Document idoc : queryResult) {
				result.add(JSONObject.parseObject(idoc.toJson()));
			}
		}
		return result;
	}
	
	/**
	 * 根据doc找到一条记录
	 */
	public JSONObject selectByDoc(String collection, Document doc) {
		JSONObject result = null;
		List<Document> queryResult = query(collection, doc);
		if (queryResult != null && queryResult.size() > 0) {
			Document oldData = queryResult.get(0);
			result = JSONObject.parseObject(oldData.toJson());
		}
		return result;
	}
	public static void findBigDecimal(Object object) {
		if (object == null)
			return;
		if (object instanceof JSONObject) {
			JSONObject json = (JSONObject) object;
			for (String key : json.keySet()) {
				Object obj = json.get(key);
				if (obj instanceof BigDecimal) {
					json.put(key, BigDecimal2Double((BigDecimal) obj));
				} else {
					findBigDecimal(obj);
				}

			}
		}  else if (object instanceof JSONArray) {
			JSONArray json = (JSONArray) object;
			for (int i = 0; i < json.size(); i++) {
				Object obj = json.get(i);
				if (obj instanceof BigDecimal) {
					json.set(i, BigDecimal2Double((BigDecimal) obj));
				} else {
					findBigDecimal(json.get(i));
				}
			}
		}
	}
	private static Object BigDecimal2Double(BigDecimal obj) {
		return obj.doubleValue();
	}

}
