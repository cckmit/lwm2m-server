package com.abupdate.iot.lwm2m;

import com.abupdate.iot.lwm2m.config.Config;
import com.abupdate.iot.lwm2m.util.SpringBootUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import static com.abupdate.iot.lwm2m.start.StartLwM2mServer.startLwM2mServer;
/**
 * Spring Boot 应用的标识
 */
@SpringBootApplication
/**
 * @company adups
 * @author wangxiaojing
 * @date 2018年1月5日
 **/
public class StartApplication extends SpringBootServletInitializer {
    private static final Logger logger = LoggerFactory.getLogger(StartApplication.class);

    public static StringRedisTemplate stringRedisTemplate;


    public static String DOWNLOADURL = "";

    public static final String IOT = "";

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(StartApplication.class);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(StartApplication.class, args);
        logger.info("fist");
        stringRedisTemplate = ac.getBean(StringRedisTemplate.class);
        Config config = SpringBootUtil.getBean(Config.class);
        String configFile = ac.getEnvironment().getProperty("configFile");
        logger.info("The following profiles are active --->>> : " + configFile);
        String redisUrl = config.getRedisUrl();
        String downloadUrl = ac.getEnvironment().getProperty("download.url");

        logger.info("config -> download domain = {}", downloadUrl);
        DOWNLOADURL = downloadUrl;
        /**
         * Start LwM2m Server
         */

        startLwM2mServer(redisUrl);
    }

}
