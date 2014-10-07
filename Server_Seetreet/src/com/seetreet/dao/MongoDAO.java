package com.seetreet.dao;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.seetreet.bean.ApiContentIdListBean;
import com.seetreet.bean.UserBean;
import com.seetreet.bean.UserLoginBean;
import com.seetreet.bean.content.ContentProviderBean;

public class MongoDAO {
	public static boolean insertUser() {
		DB db = MongoDB.getDB();
		DBCollection col = db.getCollection("user");
		
		return false;
	}
	
	/*param : UserBean (email, name, age, phone, pw)
	 * description : 회원 가입하는 쿼리
	 * 
	 * */
	public static boolean signinUser(UserBean bean) {
		DB db = MongoDB.getDB();
		DBCollection col = db.getCollection(MongoDB.COLLECTION_USER);		
		
		if(col.findOne(new BasicDBObject(UserBean.KEY_EMAIL, bean.getEmail())) != null)
			return false;
		
		BasicDBObject newUser 
			= new BasicDBObject()
					.append(UserBean.KEY_EMAIL, bean.getEmail())
					.append(UserBean.KEY_AGE, bean.getAge())
					.append(UserBean.KEY_NAME, bean.getName())
					.append(UserBean.KEY_PHONE , bean.getPhone())
					.append(UserBean.KEY_PW, bean.getPw());
		System.out.println(newUser.toString());
		col.insert(newUser);
		
		return true;
	}
	
	/*param : apiContentIdListBean (email, name, age, phone, pw)
	 * description : 새로운 public Api 삽입
	 * 
	 * */
	
	public static boolean insertPublicApiContent(ApiContentIdListBean bean) {
	
		DB db = MongoDB.getDB();
		DBCollection col = db.getCollection(MongoDB.COLLECTION_CONTENTS);		
	
		
		//if(col.findOne(new BasicDBObject(UserBean.KEY_EMAIL, bean.getEmail())) != null)
			//return false;
		
		BasicDBObject newContent 
			= new BasicDBObject()
					.append(ApiContentIdListBean.KEY_CONTENTTITLE, bean.getContentName())
					.append(ApiContentIdListBean.KEY_CONTENTID, bean.getContentId())
					.append(ApiContentIdListBean.KEY_GENRE, bean.getContentGenre())
					.append(ApiContentIdListBean.KEY_TYPE, bean.getContentType())
					.append(ApiContentIdListBean.KEY_ARTIST, bean.getArtist())
					.append(ApiContentIdListBean.KEY_PROVIDER, bean.getProvider())
					.append(ApiContentIdListBean.KEY_MODIFIEDTIME, bean.getModifiedTime())
					.append(ApiContentIdListBean.KEY_OVERVIEW, bean.getOverview())
					.append(ApiContentIdListBean.KEY_ISCONFIRMED_ARTISTID, bean.getConfirmed_artistId())
					.append(ApiContentIdListBean.KEY_CONFIRMEDTIME, bean.getConfirmedTime())
					.append(ApiContentIdListBean.KEY_FINISHEDTIME, bean.getIsFinishedTime())
					.append(ApiContentIdListBean.KEY_EVENTSTARTDATE, bean.getEventStartDate())
					.append(ApiContentIdListBean.KEY_EVENTENDDATE , bean.getEventEndDate());
					
		System.out.println(newContent.toString());
		col.insert(newContent);
		
		return true;
	}
	
	public static boolean checkPublicApiContentId(int contentId) {
		
		DB db = MongoDB.getDB();
		DBCollection col = db.getCollection(MongoDB.COLLECTION_CONTENTS);		
	
		//같은게 있으면 false
		if(col.findOne(new BasicDBObject(ApiContentIdListBean.KEY_CONTENTID, contentId)) != null)
			return false;	

		//없으면 true
		return true;
	}
	
	/*param : email , pw
	 * description : 로그인 쿼리
	 * notice : MongoDB의 _id 값을 토큰 값으로 사용함. 다만 JAVA에서 받은 ObjectId 객체를 toString으로 하면 변환 됨
	 * */
	public static UserLoginBean loginUser(String email, String pw) {
		DB db = MongoDB.getDB();
		DBCollection col = db.getCollection(MongoDB.COLLECTION_USER);
		
		BasicDBObject user
			= new BasicDBObject()
					.append(UserBean.KEY_EMAIL, email)
					.append(UserBean.KEY_PW, pw);
		
		DBObject res;
		if((res = col.findOne(user)) != null) {
			UserBean bean 
				= new UserBean(
						(String)res.get(UserBean.KEY_EMAIL),
						(String)res.get(UserBean.KEY_NAME),
						(int)res.get(UserBean.KEY_AGE),
						(String)res.get(UserBean.KEY_PHONE),
						(String)res.get(UserBean.KEY_MODTIME),
						res.get(UserBean.KEY_TOKEN).toString());
			
			return bean;
		}
				
		return null;
	}
	
	/*param : email , token
	 * description :
	 * 
	 * */
	public static boolean isUser(String email, String token) {
		DB db = MongoDB.getDB();
		DBCollection col = db.getCollection(MongoDB.COLLECTION_USER);
		
		BasicDBObject user
			= new BasicDBObject()
					.append(UserBean.KEY_EMAIL, email)
					.append(UserBean.KEY_TOKEN, new ObjectId(token));
		
		
		if(col.findOne(user) != null) 
			return true;
				
		return false;
	}
	
	/*param : email , pw
	 * description : 
	 * notice : MongoDB의 _id 값을 토큰 값으로 사용함. 다만 JAVA에서 받은 ObjectId 객체를 toString으로 하면 변환 됨 */
	public static UserLoginBean hasUser(String email, String pw) {
		DB db = MongoDB.getDB();
		DBCollection col = db.getCollection(MongoDB.COLLECTION_USER);
		
		BasicDBObject user
			= new BasicDBObject()
					.append(UserBean.KEY_EMAIL, email)
					.append(UserBean.KEY_PW, pw);
		
		DBObject res;
		if((res = col.findOne(user)) != null) {
			UserBean bean 
				= new UserBean(
						(String)res.get(UserBean.KEY_EMAIL),
						(String)res.get(UserBean.KEY_NAME),
						(int)res.get(UserBean.KEY_AGE),
						(String)res.get(UserBean.KEY_PHONE),
						(String)res.get(UserBean.KEY_MODTIME),
						res.get(UserBean.KEY_TOKEN).toString());
			
			return bean;
		}
				
		return null;
	}
	
	public static ContentProviderBean enrollContent(ContentProviderBean bean) {		
		ContentProviderBean result = null;
		DB db = MongoDB.getDB();
		DBCollection col = db.getCollection(MongoDB.COLLECTION_CONTENT);
		
		BasicDBObject content
			= new BasicDBObject()
					.append(ContentProviderBean.KEY_TITLE, bean.getTitle())
					.append(ContentProviderBean.KEY_TYPE, bean.getType())
					.append(ContentProviderBean.KEY_PROVIDER, bean.getProviderId())
					.append(ContentProviderBean.KEY_GENRE, bean.getGenre())
					.append(ContentProviderBean.KEY_STARTTIME, bean.getStartTime())
					.append(ContentProviderBean.KEY_ENDTIME, bean.getEndTime());
		
		ObjectId id = (ObjectId) col.insert(content).getUpsertedId();
		
		DBObject res = col.findOne(new BasicDBObject(ContentProviderBean.KEY_ID, id));
		try {
			result = new ContentProviderBean(
					(String)res.get(ContentProviderBean.KEY_TITLE), 
					(String)res.get(ContentProviderBean.KEY_GENRE),
					(int)res.get(ContentProviderBean.KEY_TYPE), 
					(int)res.get(ContentProviderBean.KEY_STARTTIME), 
					(int)res.get(ContentProviderBean.KEY_ENDTIME), 
					res.get(ContentProviderBean.KEY_ID).toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
