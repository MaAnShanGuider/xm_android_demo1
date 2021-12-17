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

        ImageView imageView = (ImageView) view.findViewById(R.id.iv_1);
        Glide.with(this).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeMainViewModel.class);
        // TODO: Use the ViewModel
    }

}