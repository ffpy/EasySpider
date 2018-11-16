package org.ffpy.easyspider.core.entity;

import java.util.HashSet;
import java.util.Set;

public class HashUrlSet implements UrlSet {
    private final Set<String> set = new HashSet<>();

    @Override
    public void add(String url) {
        set.add(url);
    }

    @Override
    public boolean contains(String url) {
        return set.contains(url);
    }

    @Override
    public void remove(String url) {
        set.remove(url);
    }
}
