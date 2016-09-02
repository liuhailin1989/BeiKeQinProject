package com.android.backchina.api.remote;

import com.android.backchina.api.ApiHttpClient;
import com.android.backchina.utils.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class BackChinaApi {

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
    
    public static void getNewsList(String url, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        ApiHttpClient.get(url, params, handler);
    }
    
    public static void getChannelList(AsyncHttpResponseHandler handler){
    	RequestParams params = new RequestParams();
        ApiHttpClient.get("http://www.backchina.com/api/appxml/topchannel.php", params, handler);
    }
    
    public static void getBlogChannelList(AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        ApiHttpClient.get("http://www.backchina.com/api/appxml/topchannel.php", params, handler);
    }
    
    public static void getNewsDetail(String url,AsyncHttpResponseHandler handler){
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
}
