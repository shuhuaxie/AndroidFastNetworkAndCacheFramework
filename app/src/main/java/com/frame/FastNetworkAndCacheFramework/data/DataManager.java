package com.frame.FastNetworkAndCacheFramework.data;

import java.util.HashMap;
import java.util.Map;

//
public class DataManager {

  private static DataManager mInstance;

  public static DataManager getInstance() {
    if (mInstance == null) {
      mInstance = new DataManager();
    }
    return mInstance;
  }

  private final Map<String, Object> mCache;
  private final Map<String, Long> mExpireTimeMap;
  public static final int mShoreExpireTime = 1000 * 30;
  public static final int mNormalExpireTime = 1000 * 60 * 60 * 24;

  public DataManager() {
    mCache = new HashMap<>();
    mExpireTimeMap = new HashMap<>();
  }

  public synchronized <T> T get(String key) {

    return get(key, false);
  }

  public synchronized <T> T get(String key, boolean isIgnoreExpireTime) {
    if (mCache.get(key) == null) {
      return null;
    }

    if (mExpireTimeMap.containsKey(key) && !isIgnoreExpireTime) {
      long expireTimeMillis = mExpireTimeMap.get(key);
      if (System.currentTimeMillis() >= expireTimeMillis) {
        remove(key);
        return null;
      }
    }
    return (T) mCache.get(key);
  }

  public synchronized void put(String key, Object object) {
    put(key, object, mNormalExpireTime);
  }

  public synchronized void put(String key, Object object, long expireTimeMillis) {
    mCache.put(key, object);
    if (expireTimeMillis > 0) {
      mExpireTimeMap.put(key, System.currentTimeMillis() + expireTimeMillis);
    }
  }

  public synchronized void remove(String key) {
    mCache.remove(key);
    mExpireTimeMap.remove(key);
  }

  public synchronized void remove() {
    mCache.clear();
    mExpireTimeMap.clear();
  }

}
