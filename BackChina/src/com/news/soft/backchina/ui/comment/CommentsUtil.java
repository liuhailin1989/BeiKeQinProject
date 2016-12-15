package com.news.soft.backchina.ui.comment;

import java.io.InputStream;
import java.net.URL;

import org.w3c.dom.Text;

import com.news.soft.backchina.AppOperator;
import com.news.soft.backchina.base.BaseApplication;
import com.news.soft.backchina.emoji.InputHelper;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;


public final class CommentsUtil {
//    @SuppressWarnings("deprecation")
//    public static View getReferLayout(LayoutInflater inflater, Comment.Refer refer, int count) {
//        Context context = inflater.getContext();
//        @SuppressLint("InflateParams")
//        ViewGroup lay = (ViewGroup) inflater.inflate(R.layout.lay_comment_item_refer, null, false);
//        ShapeDrawable drawable = new ShapeDrawable(new BorderShape(new RectF(Ui.dipToPx(context.getResources(), 1), 0, 0, 0)));
//        drawable.getPaint().setColor(0xffd7d6da);
//        lay.findViewById(R.id.lay_blog_detail_comment_refer).setBackgroundDrawable(drawable);
//
//        TextView textView = ((TextView) lay.findViewById(R.id.tv_blog_detail_comment_refer));
//        drawable = new ShapeDrawable(new BorderShape(new RectF(0, 0, 0, 1)));
//        drawable.getPaint().setColor(0xffd7d6da);
//        textView.setBackgroundDrawable(drawable);
//
//        formatHtml(context.getResources(), textView, refer.author + ":<br>" + refer.content);
//
//        if (refer.refer != null && (--count) > 0) {
//            View view = getReferLayout(inflater, refer.refer, count);
//            lay.addView(view, lay.indexOfChild(textView));
//        }
//
//        return lay;
//    }
	
	public static String getReferComments(String str){
		String[] commentsArray = str.split("</div>");
		if(commentsArray.length > 1){
			String refer = commentsArray[0]+"</div>";
			Spanned span = Html.fromHtml(refer);
			return span.toString().replaceFirst(":","\n\n");
		}
		return null;
	}
	
	public static void getReferCommentsSpan(final String str,final TextView textView){
		AppOperator.runOnThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String[] commentsArray = str.split("</div>");
				if(commentsArray.length > 1){
					String refer = commentsArray[0]+"</div>";
					final Spanned span = Html.fromHtml(refer,new Html.ImageGetter() {

						@Override
						public Drawable getDrawable(String source) {
							// TODO Auto-generated method stub
							InputStream is = null;
							try {
								is = (InputStream) new URL("http://www.backchina.com/"+source).getContent();  
								Drawable d = Drawable.createFromStream(is, "src");
								d.setBounds(0, 0, d.getIntrinsicWidth(),
										d.getIntrinsicHeight());
								is.close();
								return d;
							} catch (Exception e) {
								return null;
							}
						}  
						
					},null);
					AppOperator.runOnMainThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							textView.setVisibility(View.VISIBLE);
							textView.setText(span);
						}
					});
				} else {
					AppOperator.runOnMainThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							textView.setVisibility(View.GONE);
						}
					});
				}
			}
		});
	}
	
	public static String getCommentsMessages(String str){
		String[] commentsArray = str.split("</div>");
		if(commentsArray.length > 1){
			String message = commentsArray[1];
			Spanned span = Html.fromHtml(message);
			return span.toString();
		}else{
			String message = commentsArray[0];
			Spanned span = Html.fromHtml(message);
			return span.toString();
		}
	}

    public static void formatHtml(Resources resources, TextView textView, String str) {
//        textView.setMovementMethod(MyLinkMovementMethod.a());
        textView.setFocusable(false);
        textView.setLongClickable(false);

//        if (textView instanceof TweetTextView) {
//            ((TweetTextView) textView).setDispatchToParent(true);
//        }

//        str = TweetTextView.modifyPath(str);
        Spanned span = Html.fromHtml(str);
//        span = InputHelper.displayEmoji(resources, span.toString());
        textView.setText(span.toString());
//        MyURLSpan.parseLinkText(textView, span);
    }
}
