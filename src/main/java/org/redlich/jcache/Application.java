package org.redlich.jcache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonValue;
import javax.json.JsonReader;
import javax.json.JsonPointer;

import java.io.InputStream;
import java.io.FileInputStream;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Application {

    private static final String JSON_FILE = "publications.json";
    private static final String CACHE_NAME = "publications";

    public static void main(String[] args) {
        Application application = new Application();

        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();

        // create a simple, but typesafe, configuration for the cache
        CompleteConfiguration<String,String> config =
                new MutableConfiguration<String,String>().setTypes(String.class,String.class);

        // create and get the cache
        Cache<String,String> cache = cacheManager.createCache(CACHE_NAME,config);

        try {
            InputStream fis = new FileInputStream(JSON_FILE);
            JsonReader jsonReader = Json.createReader(fis);
            JsonArray jsonArray = jsonReader.readArray();
            int n = jsonArray.size();

            for(int i = 0;i < n;++i) {
                JsonPointer pointer = Json.createPointer("/" + i + "/publication");
                JsonValue publication = pointer.getValue(jsonArray);
                cache.put(Integer.toString(i),publication.toString());
                }
            fis.close();

            application.getAllRecords(cache,n);
            application.getOneRecord(cache,n,40);
            }
        catch(Exception exception) {
            exception.printStackTrace();
            }

        cachingProvider.close();
        }

    private void getAllRecords(Cache<String,String> cache, int n) {

        System.out.println("+--------------+");
        System.out.println("| " + cache.getName() + " |");
        System.out.println("+--------------+");

        Set<String> set = new HashSet<String>();
        for(int i = 0;i < n;++i) {
            set.add(Integer.toString(i));
            }

        Map<String,String> map = cache.getAll(set);
        for(String key: map.keySet()) {
            System.out.println(key + ": " + map.get(key));
            }
        System.out.println();
        }

    private void getOneRecord(Cache<String,String> cache,int n,int index) {
        if(index > n) {
            index = n - 1;
            }
        String value = cache.get(Integer.toString(index));
        System.out.println("The publication in position " + index + " is: " + value);
        System.out.println();
        }
    }
