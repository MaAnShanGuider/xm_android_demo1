package com.zhangxueyou.android2.ui.homeMain;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhangxueyou.android2.R;

public class HomeMainFragment extends Fragment {

    private HomeMainViewModel mViewModel;
    private View view;
    public static HomeMainFragment newInstance() {
        return new HomeMainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_main_fragment, container, false);
        String imgUrl = "https://wx4.sinaimg.cn/mw2000/002Bv7tcgy1gv99o8cuqpj60sd0lbn2e02.jpg";

        ImageView bannerView = (ImageView) view.findViewById(R.id.iv_1);
        ImageView iconView = (ImageView) view.findViewById(R.id.noticeIcon);
        ImageView toDoIconView = (ImageView) view.findViewById(R.id.nav_toDo_icon);
        ImageView applyIconView = (ImageView) view.findViewById(R.id.nav_apply_icon);
        ImageView messageIconView = (ImageView) view.findViewById(R.id.nav_message_icon);

        LinearLayout navRecord = (LinearLayout) view.findViewById(R.id.nav_record);

        Glide.with(this).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(bannerView);

        // 他妈的，这里要使用android_asset 而不是assets!!! 这特么的，太莫名其妙了！！！
        Glide.with(this).load("file:///android_asset/img/index_02.png").diskCacheStrategy(DiskCacheStrategy.NONE).into(toDoIconView);
        Glide.with(this).load("file:///android_asset/img/index_03.png").diskCacheStrategy(DiskCacheStrategy.NONE).into(applyIconView);
        Glide.with(this).load("file:///android_asset/img/index_04.png").diskCacheStrategy(DiskCacheStrategy.NONE).into(messageIconView);

        iconView.setColorFilter(0xfff38b34); // 直接颜色覆盖图片
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeMainViewModel.class);
        // TODO: Use the ViewModel
    }

}