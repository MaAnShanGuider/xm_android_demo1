package com.zhangxueyou.android2.ui.homeMessage;

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
import com.zhangxueyou.android2.R;

public class HomeMessageFragment extends Fragment {

    private HomeMessageViewModel mViewModel;
    private View view;
    public static HomeMessageFragment newInstance() {
        return new HomeMessageFragment();
    }

    private ImageView mIv_1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_message_fragment, container, false);

//        ImageView imageView = (ImageView) view.findViewById(R.id.iv_1);
//        Glide.with(this).load("https://c-ssl.duitang.com/uploads/item/201412/23/20141223010548_Cvmnd.jpeg").into(imageView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeMessageViewModel.class);
        //  pageViewModel = ViewModelProviders.of(requireActivity()).get(key, PageViewModel.class); 这个可以避免重建

        // TODO: Use the ViewModel
    }

}