/**
 * 
 */
package com.news.soft.backchina.utils;

import com.news.soft.backchina.AppContext;
import com.news.soft.backchina.base.BaseApplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
    
    // 手机网络类型
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    
	public static boolean isConnected(Context context) {
    	boolean result = false;
    	
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = connMgr.getActiveNetworkInfo();
		if (ni != null && ni.isConnected()){
			result = true;
		}
		
		return result;
	}

	public static boolean isWifi(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeInfo = connectivity.getActiveNetworkInfo();
		if(activeInfo == null)
			return false;
		int networkType = activeInfo.getType();
		if (networkType == ConnectivityManager.TYPE_WIFI) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isWifiOpen() {
	  boolean isWifiConnect = false;
      ConnectivityManager cm = (ConnectivityManager) BaseApplication
              .context().getSystemService(Context.CONNECTIVITY_SERVICE);
      // check the networkInfos numbers
      NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
      for (int i = 0; i < networkInfos.length; i++) {
          if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
              if (networkInfos[i].getType() == ConnectivityManager.TYPE_MOBILE) {
                  isWifiConnect = false;
              }
              if (networkInfos[i].getType() == ConnectivityManager.TYPE_WIFI) {
                  isWifiConnect = true;
              }
          }
      }
      return isWifiConnect;
	}
	
    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public static int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) AppContext.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!StringUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

}
