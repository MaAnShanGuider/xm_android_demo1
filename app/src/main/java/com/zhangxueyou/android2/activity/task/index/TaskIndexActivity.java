package com.zhangxueyou.android2.activity.task.index;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.zhangxueyou.android2.R;
import com.zhangxueyou.android2._commonActivity.CustomTitleBar;
import com.zhangxueyou.android2.databinding.ActivityTaskIndexBinding;

import java.util.zip.Inflater;

public class TaskIndexActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityTaskIndexBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTaskIndexBinding.inflate(getLayoutInflater());

        getSupportActionBar().hide(); // 隐藏标题栏
        Window window = getWindow();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏状态栏
//        window.setStatusBarColor(binding.getRoot().getResources().getColor(R.color.design_default_color_error)); // 设置状态栏颜色
        setContentView(binding.getRoot());


        Log.v("onCreate", "onCreate");

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_task_index);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {

        Log.v("onCreateView", "onCreateView");
        return super.onCreateView(name, context, attrs);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_task_index);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}