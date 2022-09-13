package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final Map<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String status = "501", text = "";
        if ("POST".equals(req.httpRequestType())) {
            Map<String, ConcurrentLinkedQueue<String>> map = topics.get(req.sourceName());
            status = "204";
            if (map != null) {
                for (var clientQueue : map.values()) {
                    clientQueue.add(req.param());
                }
                status = "200";
            }
        } else if ("GET".equals(req.httpRequestType())) {
            topics.putIfAbsent(req.sourceName(), new ConcurrentHashMap<>());
            topics.get(req.sourceName()).putIfAbsent(req.param(), new ConcurrentLinkedQueue<>());
            String poll = topics.get(req.sourceName()).get(req.param()).poll();
            if (poll != null) {
                text = poll;
            }
            status = "202";
        }
        return new Resp(text, status);
    }
}