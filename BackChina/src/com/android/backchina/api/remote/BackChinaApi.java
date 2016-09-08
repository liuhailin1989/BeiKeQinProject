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
    
    public static void getComments(String url,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        ApiHttpClient.get(url, params, handler);
    }
    
    public static void sendNewsComment(int aid,String message,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("func", "reply");
        params.put("aid", aid);
        params.put("subject", "test");
        params.put("message", message);
        String url = "plugin.php?id=bkc_app_iphone:user";
        ApiHttpClient.post(url, params, handler);
    }
    
    public static void sendBlogComment(int aid,String message,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("func", "reply");
        params.put("blogid", aid);
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
    
    public static void Subscribe(String url,String title, AsyncHttpResponseHandler handler){
    	//http://www.backchina.com/plugin.php?id=bkc_app_iphone:user&func=sharelink&type=app
    	//&link=http://www.backchina.com/news/&general=A+good+website&title=%E5%80%8D%E5%8F%AF%E4%BA%B2
        RequestParams params = new RequestParams();
        params.put("func", "sharelink");
        params.put("type", "app");
        params.put("link", url);
        params.put("general", "A+good+website");
        params.put("title", title);
        ApiHttpClient.post("plugin.php?id=bkc_app_iphone:user", params, handler);
    }
    
    public static void getMySubscribeList(AsyncHttpResponseHandler handler){
    	//http://www.backchina.com/plugin.php?id=bkc_app_iphone:user&func=sharelink&type=app&action=list
    	 RequestParams params = new RequestParams();
         params.put("func", "sharelink");
         params.put("type", "app");
         params.put("action", "list");
         ApiHttpClient.get("plugin.php?id=bkc_app_iphone:user", params, handler);
    }
}
