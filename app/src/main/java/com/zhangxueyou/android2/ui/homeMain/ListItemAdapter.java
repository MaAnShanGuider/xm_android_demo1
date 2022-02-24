package com.zhangxueyou.android2.ui.homeMain;

import com.zhangxueyou.android2.ui.homeMain.ListItem;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.zhangxueyou.android2.R;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListItemAdapter extends ArrayAdapter<ListItem> {
    private int resourceId;

    public ListItemAdapter(Context context, int textViewResourceId, List<ListItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        ListItem listItem = getItem(position);

        View view;
        ViewHolder viewHolder;

        // 加个判断，以免ListView每次滚动时都要重新加载布局，以提高运行效率
        if(getContext() != null) { // convertView 换成getContext(),瞎猫碰见死耗子
            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent, false);
            // 避免每次调用getView()时都要重新获取控件实例
            viewHolder = new ViewHolder(view.findViewById(R.id.home_main_list_item_title), view.findViewById(R.id.home_main_list_item_time));
            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
            view.setTag(viewHolder);
        } else{
            view= convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title.setText(listItem.getTitle());
        viewHolder.time.setText(listItem.getTime());
        return view;
    }

    class ViewHolder {
        TextView title;
        TextView time;

        ViewHolder(TextView title,TextView time ){
            this.title = title;
            this.time = time;
        }

    }
}
