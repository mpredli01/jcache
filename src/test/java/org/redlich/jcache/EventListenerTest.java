package org.redlich.jcache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

import static org.junit.Assert.assertEquals;

public class EventListenerTest {

    private static final String CACHE_NAME = "MyCache";

    private Cache<String,String> cache;
    private SimpleCacheEntryListener listener;
    private MutableCacheEntryListenerConfiguration<String,String> listenerConfiguration;

    @Before
    public void setup() {
        System.out.println("+------------------------------------------+");
        System.out.println("| establishing test in EventListenerTest() |");
        System.out.println("+------------------------------------------+");

        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();
        MutableConfiguration<String,String> config = new MutableConfiguration<String,String>();
        this.cache = cacheManager.createCache("MyCache",config);
        this.listener = new SimpleCacheEntryListener();
        }

    @After
    public void tearDown() {
        System.out.println("+------------------------------------------+");
        System.out.println("| tearing down test in EventListenerTest() |");
        System.out.println("+------------------------------------------+");

        Caching.getCachingProvider().getCacheManager().destroyCache(CACHE_NAME);
        }

    @Test
    public void whenRunEvent_thenCorrect() throws InterruptedException {
        System.out.println("+---------------------------------------+");
        System.out.println("| executing test in EventListenerTest() |");
        System.out.println("+---------------------------------------+");

        this.listenerConfiguration = new MutableCacheEntryListenerConfiguration<>(FactoryBuilder
                .factoryOf(this.listener),null,false,true);
        this.cache.registerCacheEntryListener(this.listenerConfiguration);

        assertEquals(false,this.listener.getCreated());

        this.cache.put("key","value");
        assertEquals(true,this.listener.getCreated());
        assertEquals(false,this.listener.getUpdated());

        this.cache.put("key","newValue");
        assertEquals(true,this.listener.getUpdated());
        }
    }
