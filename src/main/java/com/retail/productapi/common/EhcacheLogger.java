package com.retail.productapi.common;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This class logs event details happening on the Ehcache
 */
@Component
public class EhcacheLogger implements CacheEventListener<Object, Object> {

    private static final Logger logger = LoggerFactory.getLogger(EhcacheLogger.class);

    /**
     * Invoked on {@link CacheEvent CacheEvent} firing.
     * @param cacheEvent the actual {@code CacheEvent}
     */
    @Override
    public void onEvent(CacheEvent<?, ?> cacheEvent) {
        logger.info("Key: {} | EventType: {} | Old value: {} | New value: {}",
                cacheEvent.getKey(), cacheEvent.getType(), cacheEvent.getOldValue(),
                cacheEvent.getNewValue());
    }
}
