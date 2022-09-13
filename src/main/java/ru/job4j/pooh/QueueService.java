package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final Map<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String status = "501", text = "";
        if ("POST".equals(req.httpRequestType())) {
            queue.putIfAbsent(req.sourceName(), new ConcurrentLinkedQueue<>());
            queue.get(req.sourceName()).add(req.param());
            status = "201";
        } else if ("GET".equals(req.httpRequestType())) {
            text = queue.getOrDefault(req.sourceName(), new ConcurrentLinkedQueue<>()).poll();
            if (text == null) {
                text = "";
                status = "204";
            } else {
                status = "200";
            }
        }
        return new Resp(text, status);
    }
}