package org.ffpy.easyspider.core.entity;

public interface UrlSet {

    void add(String url);

    boolean contains(String url);

    void remove(String url);

    int size();
}
