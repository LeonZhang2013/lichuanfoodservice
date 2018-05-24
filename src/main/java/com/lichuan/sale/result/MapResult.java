package com.lichuan.sale.result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResult extends Result {

    private Map<String,Object> data = new HashMap<>();

    private long total;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void put(String key,Object t){
        data.put(key,t);
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
