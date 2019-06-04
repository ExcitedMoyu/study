package com.smasher.widget.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smasher.widget.R;

import java.util.Arrays;

/**
 * @author matao
 * @date 2019/6/4
 */
@CoordinatorLayout.DefaultBehavior(SlidingCardBehavior.class)
public class SlidingCardLayout extends FrameLayout {


    RecyclerView mList;
    TextView mHeader;


    private int mHeaderViewHeight;


    private final String[] ITEMS = {"赵丽颖", "林心如", "柳岩", "陈乔恩", "宋茜", "杨颖", "杨幂", "唐嫣",
            "林志玲", "赵丽颖", "林心如", "柳岩", "陈乔恩", "宋茜", "杨颖", "杨幂", "唐嫣",
            "林志玲"};


    public SlidingCardLayout(@NonNull Context context) {
        this(context, null, 0);
    }

    public SlidingCardLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingCardLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.card_layout, this);
        mList = findViewById(R.id.list);
        mHeader = findViewById(R.id.header);

        StringAdapter ad = new StringAdapter(context);
        ad.setData(Arrays.asList(ITEMS));
        mList.setLayoutManager(new LinearLayoutManager(context));
        mList.setAdapter(ad);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlidingCardLayout);
        mHeader.setBackgroundColor(a.getColor(R.styleable.SlidingCardLayout_android_colorBackground, 0));
        mHeader.setText(a.getString(R.styleable.SlidingCardLayout_android_text));
        a.recycle();

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            mHeaderViewHeight = findViewById(R.id.header).getMeasuredHeight();
        }
    }


    public int getHeaderViewHeight() {
        return mHeaderViewHeight;
    }
}
