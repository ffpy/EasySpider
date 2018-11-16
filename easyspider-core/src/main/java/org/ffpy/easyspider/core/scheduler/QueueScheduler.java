package org.ffpy.easyspider.core.scheduler;

import org.ffpy.easyspider.core.entity.*;

public class QueueScheduler implements Scheduler {
    private final RequestQueue requestQueue = new MemoryRequestQueue();
    private final UrlSet urlSet = new HashUrlSet();

    @Override
    public Request getRequest() {
        return requestQueue.get();
    }

    @Override
    public void addRequest(Request request) {
        if (request.method() == Request.Method.GET) {
            if (urlSet.contains(request.url())) return;
            urlSet.add(request.url());
        }
        requestQueue.add(request);
    }

    @Override
    public void removeRequest(Request request) {
        requestQueue.remove(request);
    }

    @Override
    public int getRequestCount() {
        return requestQueue.size();
    }
}
