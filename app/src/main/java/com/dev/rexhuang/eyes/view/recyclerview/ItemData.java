package com.dev.rexhuang.eyes.view.recyclerview;

/**
 * *  created by RexHuang
 * *  on 2019/4/12
 */
public class ItemData {
    private Object data;
    private String tag;

    public ItemData(Object data, String tag) {
        this.data = data;
        this.tag = tag;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
