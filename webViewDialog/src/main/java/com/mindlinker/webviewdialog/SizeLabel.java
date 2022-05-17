package com.mindlinker.webviewdialog;

import android.text.Editable;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;

import androidx.annotation.ColorInt;

import org.xml.sax.Attributes;

import java.util.Stack;

public abstract class SizeLabel
//        implements Html.TagHandler,
        implements HtmlParser.TagHandler {
//    private int size;
//    private int startIndex = 0;
//    private int stopIndex = 0;
//    public SizeLabel(int size) {
//        this.size = size;
//    }

//    @Override
//    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
//        Log.e("handleTag", output.toString());
//        try {
//           if(tag.toLowerCase().equals("size")) {
////               int fontSize = 0;
////               ContentHandler contentHandler = xmlReader.getContentHandler();
////               String size = xmlReader.getProperty("font-size").toString();
////               String regEx="[^0-9]";
////               Pattern p = Pattern.compile(regEx);
////               Matcher m = p.matcher(size);
////               String trim = m.replaceAll("").trim();
////               if(trim.length() > 0) {
////                   fontSize = Integer.parseInt(trim);
////               }
//               if (opening) {
//                   startIndex = output.length();
//               } else {
//                   stopIndex = output.length();
////               output.setSpan(new AbsoluteSizeSpan(dip2px(size)), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                   output.setSpan(new AbsoluteSizeSpan(size), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//               }
//           }
//       }catch (Exception e){
//
//        }
//    }

    private String[] filterTags;//要过滤的自定义标签
    private Stack<Integer> tagIns;//识别对应tag的开始坐标
    private Stack<Attributes> attributesStack;//识别出对应tag的参数
    private boolean underlineText;//tag 默认去掉下划线 可设置为true
    private @ColorInt
    int tagColor = -1;

    public SizeLabel() {
        this.filterTags = getFilterTags();
        this.tagIns = new Stack<>();
        this.attributesStack = new Stack<>();
    }

    protected abstract String[] getFilterTags();

    @Override
    public boolean handleTag(boolean opening, String tag, Editable output, Attributes attributes) {
        boolean hasTag = hasTag(tag);
        if (hasTag) {//如果配置了自定义标签
            if (opening) handleStartTag(tag, output, attributes);
            else handleEndTag(tag, output, attributes);
        }
        return hasTag;
    }

    //标签开始
    protected void handleStartTag(String tag, Editable output, Attributes attributes) {
        tagIns.push(output.length());
        attributesStack.push(attributes);
    }

    //标签结束
    protected void handleEndTag(String tag, Editable output, Attributes attributes) {
        Attributes stackAttr = attributesStack.pop();
        int start = tagIns.pop();
        int end = output.length();
        String size = attributes.getValue("font-size");
//       output.setSpan(new AbsoluteSizeSpan(dip2px(size)), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        try {
            output.setSpan(new AbsoluteSizeSpan(Integer.parseInt(size)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }catch (Exception e){
            output.setSpan(new AbsoluteSizeSpan(20), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    //从a哦
    private boolean hasTag(String tag) {
        if (filterTags == null || filterTags.length == 0) return false;//不需要识别自定义标签 交给系统处理
        for (String filterTag : filterTags) {
            if (filterTag.toLowerCase().equalsIgnoreCase(tag.toLowerCase())) {//如果存在过滤 则交给自己处理
                return true;
            }
        }
        return false;
    }

//   public static int dip2px(float dpValue) {
//       final float scale = getContext().getResources().getDisplayMetrics().density;
//      return (int) (dpValue * scale + 0.5f);
//   }
}