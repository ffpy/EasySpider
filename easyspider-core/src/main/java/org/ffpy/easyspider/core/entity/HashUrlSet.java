package org.ffpy.easyspider.core.entity;

import java.util.HashSet;
import java.util.Set;

public class HashUrlSet implements UrlSet {
    private final Set<String> urlSet = new HashSet<>();

    @Override
    public void add(String url) {
        urlSet.add(url);
    }

    @Override
    public boolean contains(String url) {
        return urlSet.contains(url);
    }

    @Override
    public void remove(String url) {
        urlSet.remove(url);
    }

    @Override
    public int size() {
        return urlSet.size();
    }
}
