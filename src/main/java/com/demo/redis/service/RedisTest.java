package com.demo.redis.service;

import com.demo.redis.bean.DBUserBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author ZhuLiang
 */
@Service
public class RedisTest {

    final static long START_USER_ID = 4611686027042922274L;


    @Resource(name = "executor")
    private ThreadPoolTaskExecutor executor;
        @Resource(name = "springLettuceTemplate")
    private RedisTemplate<String, String> springLettuceTemplate;
//    @Resource(name = "springRedissonTemplate")
//    private RedisTemplate<String, String> springRedissonTemplate;


    @PostConstruct
    public void work() throws Exception {
        String v = springLettuceTemplate.opsForValue().get("userKey-" + START_USER_ID + "-0");
        if (StringUtils.hasLength(v)) {
            getDbUserBean(v);
        }
        Thread.sleep(2 * 1000);
        final int threadCount = 100;
        final int workCount = 10000;
        for (int i = 0; i < threadCount; i++) {
            final long userId = START_USER_ID + i;
            final String key = "userKey-" + userId + "-0";
            executor.execute(() -> {
                int getCount = 0;
                SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                try {
                    do {
                        Date startTime = new Date();
                        String value = springLettuceTemplate.opsForValue().get(key);
                        Date endTime = new Date();
                        Date jsonStartTime = new Date();
                        if (StringUtils.hasLength(value)) {
                            getDbUserBean(value);
                        }
                        Date jsonEndTime = new Date();
                        long jsonUseTime = jsonEndTime.getTime() - jsonStartTime.getTime();
                        long useTime = endTime.getTime() - startTime.getTime();
                        if (useTime >= 100 || jsonUseTime >= 100) {
                            System.out.println(
                                    "Key:" + key +
                                            ",????????????:" + dateFormat.format(startTime) +
                                            ",????????????:" + dateFormat.format(endTime) +
                                            ",????????????:" + useTime +
                                            ",??????????????????:" + jsonUseTime
                            );
                        }

                        getCount++;
                    } while (getCount <= workCount);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        }
    }

    public DBUserBean getDbUserBean(String userStr) throws Exception {
        ObjectNode node = str2pojo(userStr, ObjectNode.class);
        JsonNode birthday = node.path("birthday");
        String birthdayStr = "";
        if (birthday.isObject()) {
            int year = birthday.path("year").asInt();
            int month = birthday.path("month").asInt();
            int day = birthday.path("day").asInt();
            if (year > 0 && month >= 0 && day > 0) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = new GregorianCalendar(year,
                        month, day);
                Date date = calendar.getTime();
                birthdayStr = df.format(date);
            }
        } else {
            birthdayStr = birthday.asText();
        }
        node.put("birthday", birthdayStr);

        return obj2pojo(node, DBUserBean.class);
    }

    public final static ObjectMapper mapper = initMapper();

    private static ObjectMapper initMapper() {
        ObjectMapper initMapper = new ObjectMapper();

        // ??????json?????????????????????????????????
        initMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // ?????????bean???json??????
        initMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // ??????????????????
        initMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        // ???????????????
        initMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        // ???????????????????????????
        initMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //???????????????
        initMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        return initMapper;
    }

    /**
     * ???json??????????????????????????????
     * ???????????????json????????????????????????
     *
     * @param jsonStr ?????????json???
     * @param clazz   ??????????????????
     * @return ??????????????????
     */
    public static <T> T str2pojo(String jsonStr, Class<T> clazz) throws Exception {

        return mapper.readValue(jsonStr, clazz);
    }

    public static <T> T obj2pojo(Object obj, Class<T> clazz) {
        return mapper.convertValue(obj, clazz);
    }
}
