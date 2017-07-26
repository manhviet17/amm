package com.demo.voice.web;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.demo.voice.process.cache.CacheManager;
import com.demo.voice.process.cache.CacheState;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = { "com.demo.voice.web.*" })
@PropertySources(@PropertySource("classpath:voice-search-ttx.properties"))
public class ResetCacheJob {
	Logger logger = Logger.getLogger(ResetCacheJob.class);
	@Value("${expireTime}")
	private long expireTime;
	@Autowired
	private CacheManager cache;

	@Scheduled(fixedDelay = 5000)
	public void scheduleFixedDelayTask() {
		Map<String, CacheState> cacheMap = cache.getAllCache();
		for (Map.Entry<String, CacheState> entry : cacheMap.entrySet()) {
			if (System.currentTimeMillis() - entry.getValue().getTime() >= expireTime) {
				cache.removeCache(entry.getKey());
				logger.info("Remove - " + entry.getKey());
			}
		}
	}
}
