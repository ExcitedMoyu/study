package com.smasher.widget.basic;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.smasher.oa.core.utils.StatusBarUtil;
import com.smasher.widget.R;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;


/**
 * CoordinatorLayout等一系列的使用
 *
 * @author matao
 */
public class BasicActivity extends AppCompatActivity {

    private static final String TAG = "BasicActivity";
    Toolbar toolbar;
    FloatingActionButton fab;
    AppBarLayout appBarLayout;
    CoordinatorLayout coordinatorLayout;
    MagicIndicator magicIndicator;
    ViewPager viewPager;

    FragmentManager mFragmentManager;
    FragmentStateAdapter mFragmentStateAdapter;

    ArrayList<MFragment> mArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        StatusBarUtil.setTranslucent(this, 200);

        Log.d(TAG, "onCreate: ");
        initView();
        initListener();
        initViewPager();
        initMagicIndicator();
    }

    private void initListener() {
        fab.setOnClickListener(mOnClickListener);

    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        appBarLayout = findViewById(R.id.appBarLayout);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        magicIndicator = findViewById(R.id.magicIndicator);
        viewPager = findViewById(R.id.viewPager);
        setSupportActionBar(toolbar);
        mFragmentManager = getSupportFragmentManager();
    }


    private void initViewPager() {

        try {
            mFragmentStateAdapter = new FragmentStateAdapter(getSupportFragmentManager(), mArrayList);
            viewPager.setAdapter(mFragmentStateAdapter);
            viewPager.setOffscreenPageLimit(4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initMagicIndicator() {
        magicIndicator.setBackgroundColor(Color.parseColor("#00000000"));

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText("Title" + index);
                simplePagerTitleView.setNormalColor(Color.parseColor("#99fdda00"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#fdda00"));
                simplePagerTitleView.setOnClickListener(v -> viewPager.setCurrentItem(index));
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setYOffset(10);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 6));
                indicator.setLineWidth(UIUtil.dip2px(context, 30));
                //indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.parseColor("#00c853"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        LinearLayout linearLayout = commonNavigator.getTitleContainer();
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE | LinearLayout.SHOW_DIVIDER_BEGINNING | LinearLayout.SHOW_DIVIDER_END);
        linearLayout.setDividerPadding(UIUtil.dip2px(this, 35));
        linearLayout.setDividerDrawable(new ColorDrawable(Color.parseColor("#00000000")) {

            @Override
            public int getIntrinsicWidth() {
                return UIUtil.dip2px(BasicActivity.this, 25);
            }
        });


        ViewPagerHelper.bind(magicIndicator, viewPager);
    }


    View.OnClickListener mOnClickListener = view -> {
        int i = view.getId();
        if (i == R.id.fab) {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };
}
