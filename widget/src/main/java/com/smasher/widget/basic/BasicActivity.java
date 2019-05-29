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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * CoordinatorLayout等一系列的使用
 *
 * @author matao
 */
public class BasicActivity extends AppCompatActivity {

    private static final String TAG = "BasicActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;


    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;


    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;


    @BindView(R.id.magicIndicator)
    MagicIndicator magicIndicator;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    FragmentManager mFragmentManager;
    FargmentStateAdapter mFargmentStateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Log.d(TAG, "onCreate: ");

        mFragmentManager = getSupportFragmentManager();
        initViewPager();
        initMagicIndicator();
    }

    private void initViewPager() {

        try {
            mFargmentStateAdapter = new FargmentStateAdapter(mFragmentManager);
            viewPager.setAdapter(mFargmentStateAdapter);
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
                simplePagerTitleView.setNormalColor(Color.parseColor("#838A96"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#3b3F47"));
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
//                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
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


    @OnClick({R.id.toolbar, R.id.fab, R.id.appBarLayout, R.id.coordinatorLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                break;
            case R.id.fab:
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.appBarLayout:
                break;
            case R.id.coordinatorLayout:
                break;
            default:
                break;
        }
    }
}
