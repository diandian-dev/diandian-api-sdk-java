package com.diandian.api.sdk.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * sdk参数的map。
 * 
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-4-17 上午11:58:23
 * 
 */
public class DDParameters {

    public final Map<String, String> mParameters = new HashMap<String, String>();

    private final List<String> mKeys = new ArrayList<String>();

    public DDParameters() {

    }

    public void add(String key, String value) {
        if (this.mKeys.contains(key)) {
            this.mParameters.put(key, value);
        } else {
            this.mKeys.add(key);
            this.mParameters.put(key, value);
        }
    }

    public void remove(String key) {
        mKeys.remove(key);
        this.mParameters.remove(key);
    }

    public void remove(int i) {
        String key = this.mKeys.get(i);
        this.mParameters.remove(key);
        mKeys.remove(key);
    }

    public int getLocation(String key) {
        if (this.mKeys.contains(key)) {
            return this.mKeys.indexOf(key);
        }
        return -1;
    }

    public String getKey(int location) {
        if (location >= 0 && location < this.mKeys.size()) {
            return this.mKeys.get(location);
        }
        return "";
    }

    public String getValue(String key) {
        String rlt = this.mParameters.get(key);
        return rlt;
    }

    public String getValue(int location) {
        String key = this.mKeys.get(location);
        String rlt = this.mParameters.get(key);
        return rlt;
    }

    public int size() {
        return mKeys.size();
    }

    public void addAll(DDParameters parameters) {
        for (int i = 0; i < parameters.size(); i++) {
            this.add(parameters.getKey(i), parameters.getValue(i));
        }

    }

    public void clear() {
        this.mKeys.clear();
        this.mParameters.clear();
    }

}
