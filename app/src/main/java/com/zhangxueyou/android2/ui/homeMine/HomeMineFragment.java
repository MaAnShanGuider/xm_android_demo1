package com.zhangxueyou.android2.ui.homeMine;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhangxueyou.android2.R;
import com.zhangxueyou.android2.activity.settings.index.SettingsIndexActivity;

public class HomeMineFragment extends Fragment {

    private HomeMineViewModel mViewModel;
    private View view;
    public static HomeMineFragment newInstance() {
        return new HomeMineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_mine_fragment, container, false);

        TextView bluetoothBlue = (TextView) view.findViewById(R.id.jumpToBlueTooth);
        bluetoothBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), SettingsIndexActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeMineViewModel.class);
        // TODO: Use the ViewModel
    }

}