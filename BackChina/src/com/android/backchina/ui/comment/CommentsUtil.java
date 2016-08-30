package com.android.backchina.ui.comment;

import com.android.backchina.bean.Comment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
//
//    public static void formatHtml(Resources resources, TextView textView, String str) {
//        textView.setMovementMethod(MyLinkMovementMethod.a());
//        textView.setFocusable(false);
//        textView.setLongClickable(false);
//
//        if (textView instanceof TweetTextView) {
//            ((TweetTextView) textView).setDispatchToParent(true);
//        }
//
//        str = TweetTextView.modifyPath(str);
//        Spanned span = Html.fromHtml(str);
//        span = InputHelper.displayEmoji(resources, span.toString());
//        textView.setText(span);
//        MyURLSpan.parseLinkText(textView, span);
//    }
}
