package com.android.backchina.interf;

public interface IContractDetail {
    
	void setOperatorCallBack(OperatorCallBack callback);
	
   Object getData();
   
   void hideLoading();
   
   // 收藏
   void toFavorite();
   
   //
   void cancleFavorite();

   // 分享
   void toShare();

   // 提交评价
   void toSendComment(String comment,int cid, int position);
   
   //
   void toSeeMoreComments();
}
