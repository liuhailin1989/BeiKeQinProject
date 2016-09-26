package com.android.backchina.api.remote;

import com.android.backchina.api.ApiHttpClient;
import com.android.backchina.utils.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class BackChinaApi {

	public static void register(String username, String password,String email, AsyncHttpResponseHandler handler){
		//http://www.backchina.com/plugin.php?id=bkc_app_iphone:user&
		//func=register&username=admin&password=admin&email=xx&questionid=xxx&answer=fdxx
        RequestParams params = new RequestParams();
        params.put("func", "register");
        params.put("username", username);
        params.put("password", password);
        params.put("email", email);
        String loginurl = "plugin.php?id=bkc_app_iphone:user";
        ApiHttpClient.post(loginurl, params, handler);
	}
	
    /**
     * 登陆
     *
     * @param username
     * @param password
     * @param handler
     */
    public static void login(String username, String password,
                             AsyncHttpResponseHandler handler) {
        //http://www.backchina.com/plugin.php?id=bkc_app_iphone:user
    	//&func=login&username=xxx&password=xxx
        RequestParams params = new RequestParams();
        params.put("func", "login");
        params.put("username", username);
        params.put("password", password);
        String loginurl = "plugin.php?id=bkc_app_iphone:user";
        ApiHttpClient.post(loginurl, params, handler);
    }
    
    public static void syncLogin(String username, String password,AsyncHttpResponseHandler handler){
        //http://www.backchina.com/plugin.php?id=bkc_app_iphone:user&func=synlogin&username=admin&password=admin
        RequestParams params = new RequestParams();
        params.put("func", "synlogin");
        params.put("username", username);
        params.put("password", password);
        String loginurl = "plugin.php?id=bkc_app_iphone:user";
        ApiHttpClient.post(loginurl, params, handler);
    }

    public static void getUserInfo(AsyncHttpResponseHandler handler){
        //http://www.backchina.com/plugin.php?id=bkc_app_iphone:user&func=getuserinfo
        RequestParams params = new RequestParams();
        params.put("func", "getuserinfo");
        String url = "plugin.php?id=bkc_app_iphone:user";
        ApiHttpClient.post(url, params, handler);
    }
    
    public static void openIdLogin(String s) {

    }
    
    public static void getHttp(String url,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        ApiHttpClient.get(url, params, handler);
    }
    
    /**
     * 请求资讯列表
     *
     * @param pageToken 请求上下页数据令牌
     * @param handler   AsyncHttpResponseHandler
     */
    public static void getNewsList(AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        ApiHttpClient.get("http://www.backchina.com/news/?page=1&appxml=1&json=1", params, handler);
    }
    
    public static void getNewsList(String url, int page, AsyncHttpResponseHandler handler) {
    	url = url.replace("&page=1", "");
    	url = url.replace("page=1", "");
        RequestParams params = new RequestParams();
        params.put("page", page);
        ApiHttpClient.get(url, params, handler);
    }
    
    public static void getBlogList(String url, int page,AsyncHttpResponseHandler handler) {
    	url = url.replace("&page=1", "");
    	url = url.replace("page=1", "");
        RequestParams params = new RequestParams();
        params.put("page", page);
        ApiHttpClient.get(url, params, handler);
    }
    
    public static void getVideoList(String url, int page,AsyncHttpResponseHandler handler) {
    	url = url.replace("&page=1", "");
    	url = url.replace("page=1", "");
        RequestParams params = new RequestParams();
        params.put("page", page);
        ApiHttpClient.get(url, params, handler);
    }
    
    public static void getNewsChannelList(AsyncHttpResponseHandler handler){
    	RequestParams params = new RequestParams();
        ApiHttpClient.get("api/appxml/topchannel.php", params, handler);
    }
    
    public static void getBlogChannelList(AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("blognav", "1");
        ApiHttpClient.get("api/appxml/topchannel.php?", params, handler);
    }
    
    public static void getVideoChannelList(AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("videonav", "1");
        ApiHttpClient.get("api/appxml/topchannel.php?", params, handler);
    }
    
    public static void getNewsDetail(String url,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        ApiHttpClient.get(url, params, handler);
    }
    
    public static void getBlogDetail(String url,AsyncHttpResponseHandler handler){
    	RequestParams params = new RequestParams();
        ApiHttpClient.get(url, params, handler);
    }
    
    public static void getBlogerList(String uid, int page, AsyncHttpResponseHandler handler){
    	//http:\/\/www.backchina.com\/home.php?mod=space&uid=288584&do=blog&view=me&appxml=1&json=1
      	 RequestParams params = new RequestParams();
         params.put("mod", "space");
         params.put("uid", uid);
         params.put("do", "blog");
         params.put("view", "me");
         params.put("appxml", "1");
         params.put("json", "1");
         params.put("page", page);
         ApiHttpClient.get("home.php?", params, handler);
    }
    
    public static void getVideoDetail(String url,AsyncHttpResponseHandler handler){
    	RequestParams params = new RequestParams();
        ApiHttpClient.get(url, params, handler);
    }
    
    public static void getComments(String url, int page, AsyncHttpResponseHandler handler){
    	url = url.replace("&page=1", "");
    	url = url.replace("page=1", "");
        RequestParams params = new RequestParams();
        params.put("page", page);
        ApiHttpClient.get(url, params, handler);
    }
    
    public static void sendNewsComment(int aid, int cid,int floor,String title, String message,AsyncHttpResponseHandler handler){
    	//http://www.backchina.com/plugin.php?id=bkc_app_iphone:user&func=reply&tid=XXX&subject=test&message=verygood
        RequestParams params = new RequestParams();
        params.put("id", "bkc_app_iphone:user");
        params.put("func", "reply");
        params.put("aid", aid);
        params.put("cid", cid);
        params.put("position", floor);
        params.put("subject", title);
        params.put("message", message);
        String url = "plugin.php?";
        ApiHttpClient.post(url, params, handler);
    }
    
    public static void sendBlogComment(int bid,int cid,int floor,String message,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("func", "reply");
        params.put("blogid", bid);
        params.put("cid", cid);
        params.put("position", floor);
        params.put("subject", "test");
        params.put("message", message);
        String url = "plugin.php?id=bkc_app_iphone:user";
        ApiHttpClient.post(url, params, handler);
    }
    
    public static void getHotSubscribeList(AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("hot", "1");
        ApiHttpClient.get("api/appxml/subscription.php?json=1", params, handler);
    }
    
    public static void getNewSubscribeList(AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("new", "1");
        ApiHttpClient.get("api/appxml/subscription.php?json=1", params, handler);
    }
    
    public static void getFunnySubscribeList(AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("fan", "1");
        ApiHttpClient.get("api/appxml/subscription.php?json=1", params, handler);
    }
    
    public static void getSubscribeList(String url,int page, AsyncHttpResponseHandler handler){
    	url = url.replace("&page=1", "");
    	url = url.replace("page=1", "");
        RequestParams params = new RequestParams();
        params.put("page", page);
        ApiHttpClient.get(url, params, handler);
    }
    
    public static void getSubscribeCatList(AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("catlist", "1");
        params.put("json", "1");
        ApiHttpClient.get("api/appxml/subscription.php?", params, handler);
    }
    
    public static void getSubscribeDetail(String url,int page, AsyncHttpResponseHandler handler){
    	url = url.replace("&page=1", "");
    	url = url.replace("page=1", "");
        RequestParams params = new RequestParams();
        params.put("page", page);
        ApiHttpClient.get(url, params, handler);
    }
    
    public static void subscribe(int id, AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("func", "myfavorite");
        params.put("type", "infolist");
        params.put("action", "add");
        params.put("dataid", id);
        ApiHttpClient.post("plugin.php?id=bkc_app_iphone:user", params, handler);
    }
    
    public static void cancelSubscribe(String favid, AsyncHttpResponseHandler handler){
    	//http://www.backchina.com/plugin.php?id=bkc_app_iphone:user&func=myfavorite&action=del&favid=XXX
        RequestParams params = new RequestParams();
        params.put("func", "myfavorite");
        params.put("action", "del");
        params.put("favid", favid);
        ApiHttpClient.post("plugin.php?id=bkc_app_iphone:user", params, handler);
    }
    
    public static void subscribeBlog(String uid,AsyncHttpResponseHandler handler){
    	//http://www.backchina.com/plugin.php?id=bkc_app_iphone:user&func=followuser&op=add&fuid=xxx
    	RequestParams params = new RequestParams();
        params.put("func", "followuser");
        params.put("op", "add");
        params.put("fuid", uid);
        ApiHttpClient.post("plugin.php?id=bkc_app_iphone:user", params, handler);
    }
    
    public static void cancleSubscribeBlog(int uid,AsyncHttpResponseHandler handler){
    	//http://www.backchina.com/plugin.php?id=bkc_app_iphone:user&func=followuser&op=add&fuid=xxx
    	RequestParams params = new RequestParams();
        params.put("func", "followuser");
        params.put("op", "del");
        params.put("fuid", uid);
        ApiHttpClient.post("plugin.php?id=bkc_app_iphone:user", params, handler);
    }
    
    public static void getMySubscribeList(AsyncHttpResponseHandler handler){
    	 RequestParams params = new RequestParams();
         params.put("func", "myfavorite");
         params.put("type", "infolist");
         params.put("action", "list");
         ApiHttpClient.get("plugin.php?id=bkc_app_iphone:user", params, handler);
    }
    
    public static void getMySubscribeBlogList(AsyncHttpResponseHandler handler){
    	//http://www.backchina.com/plugin.php?id=bkc_app_iphone:user&func=myfavorite&type=space&action=list&page=1
   	 RequestParams params = new RequestParams();
     params.put("func", "myfavorite");
     params.put("type", "space");
     params.put("action", "list");
     ApiHttpClient.get("plugin.php?id=bkc_app_iphone:user", params, handler);
    }
}
