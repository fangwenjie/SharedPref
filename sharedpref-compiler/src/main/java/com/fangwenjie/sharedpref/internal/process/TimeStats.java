package com.fangwenjie.sharedpref.internal.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by fangwenjie on 2020/4/8
 */
public class TimeStats {
    private final Map<String, Long> measures = new HashMap<>();
    private final List<Duration> durations = new ArrayList<>();

    private static class Duration implements Comparable<Duration> {
        public final String key;
        public final long durationInMs;

        public Duration(String key, long durationInMs) {
            this.key = key;
            this.durationInMs = durationInMs;
        }

        @Override
        public int compareTo(Duration duration) {
            return (int) (duration.durationInMs - durationInMs);
        }
    }

    public void start(String key) {
        long start = System.currentTimeMillis();
        measures.put(key, start);
    }

    public void stop(String key) {
        Long start = measures.remove(key);
        if (start != null) {
            long end = System.currentTimeMillis();
            long duration = end - start;
            durations.add(new Duration(key, duration));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Time measurements:");

        Collections.sort(durations);
        for (Duration duration : durations) {
            sb.append("[")
                    .append(duration.key)
                    .append(" = ")
                    .append(duration.durationInMs)
                    .append("ms],");
        }

        return sb.toString();
    }

    public void logStats() {
        System.out.println("SharedPref:" + toString());
    }

    public void clear() {
        measures.clear();
        durations.clear();
    }
}
