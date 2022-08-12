package com.teamide.metric;

import jodd.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author ZhuLiang
 */
public class Metric {

    private final String name;

    private final Object lock = new Object();

    public Metric(String name) {
        this.name = name;
    }

    private long startTime;
    private long endTime;

    private int failed;
    private int success;
    private long totalUseTime;
    private long minUseTime;
    private long maxUseTime;
    private final List<Long> useTimeList = new ArrayList<>();
    private final Map<String, Integer> failError = new HashMap<>();

    private final List<MetricData> metricDataList = new ArrayList<>();

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        endTime = System.nanoTime();
    }

    public MetricData startTask() {
        MetricData data = new MetricData();
        data.startTime = System.nanoTime();
        return data;
    }

    public void endTask(MetricData data) {
        end(data, null);
    }

    public void end(MetricData data, Exception exception) {
        data.endTime = System.nanoTime();
        data.exception = exception;
        synchronized (lock) {
            metricDataList.add(data);
        }
    }

    private void metricData(MetricData data) {
        synchronized (lock) {
            long useTime = data.endTime - data.startTime;
            totalUseTime += useTime;

            if (minUseTime == 0 || minUseTime > useTime) {
                minUseTime = useTime;
            }
            if (maxUseTime == 0 || maxUseTime < useTime) {
                maxUseTime = useTime;
            }
            useTimeList.add(useTime);
            if (data.exception == null) {
                success++;
            } else {
                failed++;
                String msg = data.exception.getMessage();
                if (StringUtil.isEmpty(msg)) {
                    msg = data.exception.getClass().getName();
                }
                Integer failErrorCount = failError.get(msg);
                failErrorCount = failErrorCount == null ? 0 : failErrorCount;
                failErrorCount++;
                failError.put(msg, failErrorCount);
            }
        }
    }

    public static final String[] TITLE_LIST = "名称,任务总数,使用时间,TPS,平均耗时,最小耗时,最大耗时,总耗时".split(",");

    // 成功,失败,TP30,TP50,TP80,TP90,TP99
    
    public String out() throws Exception {
        for (MetricData data : metricDataList) {
            metricData(data);
        }
        metricDataList.clear();

        int taskCount = failed + success;
        long useTime = endTime - startTime;
        float useSec = (float) useTime / (float) 1000000000;

        Map<String, String> info = new HashMap<>(1);
        info.put("名称", name);
        info.put("任务总数", String.valueOf(taskCount));
        info.put("成功", String.valueOf(success));
        info.put("失败", String.valueOf(failed));
        info.put("使用时间", toMillis(useTime));
        info.put("TPS", format(((float) taskCount / useSec)));
        info.put("总耗时", toMillis(totalUseTime));
        info.put("最小耗时", toMillis(minUseTime));
        info.put("最大耗时", toMillis(maxUseTime));
        info.put("平均耗时", toMillis(((float) totalUseTime / (float) taskCount)));

        Long[] useTimes = useTimeList.toArray(new Long[]{});
        Arrays.sort(useTimes);

        int size = useTimes.length;

        int t30 = (int) (size * 0.3);
        int t50 = (int) (size * 0.5);
        int t80 = (int) (size * 0.8);
        int t90 = (int) (size * 0.9);
        int t99 = (int) (size * 0.99);
        info.put("TP30", toMillis(useTimes[t30]));
        info.put("TP50", toMillis(useTimes[t50]));
        info.put("TP80", toMillis(useTimes[t80]));
        info.put("TP90", toMillis(useTimes[t90]));
        info.put("TP99", toMillis(useTimes[t99]));

        StringBuffer out = new StringBuffer();
        out.append("|");
        for (int i = 0; i < TITLE_LIST.length; i++) {
            String s = info.get(TITLE_LIST[i]);
            if (StringUtil.isEmpty(s)) {
                s = "";
            }
            out.append(s).append("|");
        }
        System.out.println(out);

        return out.toString();
    }

    public static String outHeader() {
        StringBuffer out = new StringBuffer();

        out.append("|").append(String.join("|", TITLE_LIST)).append("|");

        out.append("\n");

        out.append("|");
        for (int i = 0; i < TITLE_LIST.length; i++) {
            out.append(":------:|");
        }

        System.out.println(out);

        return out.toString();
    }

    public String toMillis(long time) {
        return format(((float) time / (float) 1000000));
    }

    public String toMillis(float time) {
        return format((time / (float) 1000000));
    }

    public String format(float num) {
        return String.format("%.2f", num);
    }
}
