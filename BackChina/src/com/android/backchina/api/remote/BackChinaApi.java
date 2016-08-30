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
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("pwd", password);
        params.put("keep_login", 1);
        String loginurl = "action/api/login_validate";
        ApiHttpClient.post(loginurl, params, handler);
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
//        if (!StringUtils.isEmpty(pageToken)) {
//            params.put("pageToken", pageToken);
//        }
//        ApiHttpClient.get("action/apiv2/news", params, handler);
        ApiHttpClient.get(url, params, handler);
    }
    
    public static void getChannelList(AsyncHttpResponseHandler handler){
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
    
    public static void sendNewsComment(String url,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        ApiHttpClient.post(url, params, handler);
    }
}
