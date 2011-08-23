package com.mirantis.cachemod.filter;

import java.util.concurrent.ConcurrentHashMap;

import junit.framework.TestCase;

import com.mirantis.cachemod.filter.SimpleLFUCacheProvider.LFUEntry;

public class NewLFUCacheProviderTest extends TestCase {

  public void testOne() {
    NewLFUCacheProvider lfu = new NewLFUCacheProvider();
    lfu.init("cache");
    CacheEntry entry = lfu.instantiateEntry();
    lfu.putEntry("1", entry);
    CacheEntry entryInCache = lfu.getEntry("1");
    assertEquals(entry, entryInCache);
  }

  public void testEviction() {
    NewLFUCacheProvider lfu = new NewLFUCacheProvider();
    lfu.init("cache");
    CacheEntry entry = lfu.instantiateEntry();
    for (int i = 0; i != 1000; ++i) {
      entry = lfu.instantiateEntry();
      String key = Integer.toString(i);
      lfu.putEntry(key, entry);
      CacheEntry entryInCache = lfu.getEntry(key);
      assertEquals(entry, entryInCache);
    }
    ConcurrentHashMap<String, LFUEntry> cache = (ConcurrentHashMap<String, LFUEntry>) lfu.getCache();
    entry = lfu.instantiateEntry();
    lfu.putEntry("1001", entry);
    assertEquals(1000, cache.size());
  }
  
  public void testSecondPut() {
    NewLFUCacheProvider lfu = new NewLFUCacheProvider();
    lfu.init("cache");
    ConcurrentHashMap<String, LFUEntry> cache = (ConcurrentHashMap<String, LFUEntry>) lfu.getCache();

    
    CacheEntry entry = lfu.instantiateEntry();
    lfu.putEntry("1", entry);
    assertEquals(1, cache.size());

    entry = lfu.instantiateEntry();
    lfu.putEntry("11", entry);
    
    entry = lfu.instantiateEntry();
    lfu.putEntry("11", entry);
    assertEquals(2, cache.size());
    
    //lru.show();
  }

  
  
}