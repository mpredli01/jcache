package org.redlich.jcache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

import static org.junit.Assert.assertEquals;

public class CacheLoaderTest {

    private static final String CACHE_NAME = "SimpleCache";

    private Cache<Integer,String> cache;

    @Before
    public void setup() {
        System.out.println("+------------------------------------------+");
        System.out.println("| establishing test in CacheLoaderTest()   |");
        System.out.println("+------------------------------------------+");

        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();
        MutableConfiguration<Integer,String> config = new MutableConfiguration<Integer,String>().setReadThrough(true)
                .setCacheLoaderFactory(new FactoryBuilder.SingletonFactory<>(new SimpleCacheLoader()));
        this.cache = cacheManager.createCache("SimpleCache",config);
        }

    @After
    public void tearDown() {
        System.out.println("+------------------------------------------+");
        System.out.println("| tearing down test in CacheLoaderTest()   |");
        System.out.println("+------------------------------------------+");

        Caching.getCachingProvider()
                .getCacheManager().destroyCache(CACHE_NAME);
        }

    @Test
    public void whenReadingFromStorage_thenCorrect() {
        System.out.println("+-------------------------------------+");
        System.out.println("| executing test in CacheLoaderTest() |");
        System.out.println("+-------------------------------------+");

        for (int i = 1; i < 4; i++) {
            String value = cache.get(i);
            assertEquals("fromCache" + i,value);
            }
        }
    }
