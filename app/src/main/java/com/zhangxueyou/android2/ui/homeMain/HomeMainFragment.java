package com.zhangxueyou.android2.ui.homeMain;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhangxueyou.android2.R;
import com.zhangxueyou.android2.activity.task.index.TaskIndexActivity;
import com.zhangxueyou.android2.utils.PixelTool;


import java.util.ArrayList;
import java.util.List;

public class HomeMainFragment extends Fragment {

    private HomeMainViewModel mViewModel;
    private View view;
    public static HomeMainFragment newInstance() {
        return new HomeMainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,  @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_main_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(HomeMainViewModel.class);
        String imgUrl = "https://wx4.sinaimg.cn/mw2000/002Bv7tcgy1gv99o8cuqpj60sd0lbn2e02.jpg";

        ImageView bannerView = (ImageView) view.findViewById(R.id.iv_1);
        ImageView iconView = (ImageView) view.findViewById(R.id.noticeIcon);
        ImageView toDoIconView = (ImageView) view.findViewById(R.id.nav_toDo_icon);
        ImageView applyIconView = (ImageView) view.findViewById(R.id.nav_apply_icon);
        ImageView messageIconView = (ImageView) view.findViewById(R.id.nav_message_icon);
        LinearLayout todoCountWrap = (LinearLayout) view.findViewById(R.id.nav_toDo_count_wrap);
        TextView todoCount = (TextView) view.findViewById(R.id.nav_toDo_count);
        LinearLayout navRecord = (LinearLayout) view.findViewById(R.id.nav_record);
        ListView todoList = (ListView) view.findViewById(R.id.todo_list);
//        LinearLayout todoListWrapper = (LinearLayout) view.findViewById(R.id.todo_list_wrapper);

        Glide.with(this).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(bannerView);

        // 他妈的，这里要使用android_asset 而不是assets!!! 这特么的，太莫名其妙了！！！
        Glide.with(this).load("file:///android_asset/img/index_02.png").diskCacheStrategy(DiskCacheStrategy.NONE).into(toDoIconView);
        Glide.with(this).load("file:///android_asset/img/index_03.png").diskCacheStrategy(DiskCacheStrategy.NONE).into(applyIconView);
        Glide.with(this).load("file:///android_asset/img/index_04.png").diskCacheStrategy(DiskCacheStrategy.NONE).into(messageIconView);

        iconView.setColorFilter(0xfff38b34); // 直接颜色覆盖图片
        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                todoCount.setText(s);
            }
        });
        initEnableScrollListView();
//        initDisableScrollListView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(HomeMainViewModel.class);
        // TODO: Use the ViewModel
    }

    /**
     * 不滚动列表
     */
    private void initDisableScrollListView() {
//        val listAdapter = recentView.adapter;

    }

    /**
     * 自己滚动的列表
     */
    private void initEnableScrollListView() {

        List<ListItem> list1 = new ArrayList<>();
        list1.add(new ListItem("标题一标题一标题一标题一标题一标题一标题一标题一标题一标题一标题一标题一标题一标题一标题一","2022年3月12日"));
        list1.add(new ListItem("标题二","2022年4月12日"));
        list1.add(new ListItem("标题三","2022年5月12日"));
        list1.add(new ListItem("标题四","2022年6月12日"));
        list1.add(new ListItem("标题五","2022年7月12日"));

        Log.d("logD","????????????????????????????");
        ListItemAdapter adapter = new ListItemAdapter(getActivity(), R.layout.home_main_list_item_fragment, list1);
        ListView listView = view.findViewById(R.id.todo_list);
        listView.setAdapter(adapter);
        setListViewHeight(listView); //把上面的设置方法加到这里

        Log.d("logD","????????????????????????????");

        // 为ListView注册一个监听器，当用户点击了ListView中的任何一个子项时，就会回调onItemClick()方法
        // 在这个方法中可以通过position参数判断出用户点击的是那一个子项
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem item = list1.get(position);

                // fragment种获取上下文，getActivity().getApplicationContext()
                Toast.makeText(getActivity(),item.getTitle(),Toast.LENGTH_SHORT).show();
                // 给bnt1添加点击响应事件
                Intent intent =new Intent(getActivity(), TaskIndexActivity.class);
                startActivity(intent);
            }
        });

    }
    /**
     * 设置高度
     */
    private void setListViewHeight(ListView listView){
        ListAdapter  listAdapter = listView.getAdapter(); //得到ListView 添加的适配器
        Log.v("log.v","-------------------");

        if(listAdapter == null){
            return;
        }

        View itemView = listAdapter.getView(0, null, listView); //获取其中的一项
        //进行这一项的测量，为什么加这一步，具体分析可以参考 https://www.jianshu.com/p/dbd6afb2c890这篇文章
        itemView.measure(0,0);
        int itemHeight = itemView.getMeasuredHeight(); //一项的高度
        int itemCount = listAdapter.getCount();//得到总的项数
        int totalHeight = 0;

        for(int i = 0; i < itemCount;i++) {
            View _itemView = listAdapter.getView(i, null, listView);
            _itemView.measure(0,0);
            int _itemHeight = _itemView.getMeasuredHeight(); //一项的高度

            totalHeight += _itemHeight;
//
//            Log.v("logV", Integer.toString( listAdapter.getView(i, null, listView).getHeight()));
//            Log.v("logV", Integer.toString(i));
//            Log.v("logV", Integer.toString(itemCount));
//            Log.v("logV", Integer.toString(totalHeight));

        }
        LinearLayout.LayoutParams layoutParams = null; //进行布局参数的设置

        // itemHeight是拿的像素px的数值，而不是dp的数值。要记住：12dp != 12px
//        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT ,(itemHeight)*(itemCount ) + (itemCount +5 ) * 12 );
        layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT ,
//                totalHeight + PixelTool.dpToPx(getContext(),(itemCount + 1 ) * 12)
                totalHeight + (listView.getDividerHeight() * (itemCount - 1) + 4)
        );

        listView.setLayoutParams(layoutParams);
    }
}