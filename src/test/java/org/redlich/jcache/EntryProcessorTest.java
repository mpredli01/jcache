package org.redlich.jcache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

import static org.junit.Assert.assertEquals;

public class EntryProcessorTest {

    private static final String CACHE_NAME = "MyCache";

    private Cache<String,String> cache;

    @Before
    public void instantiateCache() {
        System.out.println("+---------------------------------------------+");
        System.out.println("| establishing test in EntryProcessorTest()   |");
        System.out.println("+---------------------------------------------+");

        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();
        MutableConfiguration<String,String> config = new MutableConfiguration<>();
        this.cache = cacheManager.createCache(CACHE_NAME,config);
        this.cache.put("key","value");
        }

    @After
    public void tearDown() {
        System.out.println("+---------------------------------------------+");
        System.out.println("| tearing down test in EntryProcessorTest()   |");
        System.out.println("+---------------------------------------------+");

        Caching.getCachingProvider()
                .getCacheManager().destroyCache(CACHE_NAME);
        }

    @Test
    public void whenModifyValue_thenCorrect() {
        System.out.println("+----------------------------------------+");
        System.out.println("| executing test in EntryProcessorTest() |");
        System.out.println("+----------------------------------------+");

        this.cache.invoke("key",new SimpleEntryProcessor());
        assertEquals("value - modified",cache.get("key"));
        }
    }
