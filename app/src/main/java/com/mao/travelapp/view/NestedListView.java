package com.mao.travelapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by mao on 2017/3/27.
 */
public class NestedListView extends ListView{

    public NestedListView(Context context) {
        // TODO Auto-generated method stub
        super(context);
    }

    public NestedListView(Context context, AttributeSet attrs) {
        // TODO Auto-generated method stub
        super(context, attrs);
    }

    public NestedListView(Context context, AttributeSet attrs, int defStyle) {
        // TODO Auto-generated method stub
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
