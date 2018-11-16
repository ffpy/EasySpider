package org.ffpy.easyspider.core.entity;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MemoryRequestQueue implements RequestQueue {
    private final Queue<Request> requests = new ConcurrentLinkedQueue<>();

    @Override
    public Request get() {
        return requests.poll();
    }

    @Override
    public void add(Request request) {
        requests.add(request);
    }
}
