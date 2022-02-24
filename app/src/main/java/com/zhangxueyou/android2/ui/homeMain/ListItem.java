package com.zhangxueyou.android2.ui.homeMain;

public class ListItem {
    private String title;
    private String time;

    public ListItem(String title, String time) {
        this.title = title;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }
}
