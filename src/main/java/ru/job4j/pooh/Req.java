package ru.job4j.pooh;

public record Req(String httpRequestType, String poohMode, String sourceName, String param) {

    public static Req of(String content) {
        System.out.println(content);
        String[] lines = content.split(System.lineSeparator());
        String[] requestHead = lines[0].split(" ");
        String type = requestHead[0];
        if (!"POST".equals(type) && !"GET".equals(type)) {
            throw new IllegalArgumentException("Сервер принимает только POST и GET запросы");
        }
        String[] splitParams = requestHead[1].split("/");
        String poohMode = splitParams[1];
        String sourceName = splitParams[2];
        String param = "";
        if ("GET".equals(type)) {
            param = "topic".equals(poohMode) ? splitParams[3] : param;
        } else {
            param = lines[lines.length - 1];
        }
        return new Req(type, poohMode, sourceName, param);
    }
}