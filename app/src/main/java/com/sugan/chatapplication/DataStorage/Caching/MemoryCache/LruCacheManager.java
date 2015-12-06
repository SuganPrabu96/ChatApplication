package com.sugan.chatapplication.DataStorage.Caching.MemoryCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import java.util.ArrayList;

/**
 * Created by Suganprabu on 05-09-2015.
 */
public class LruCacheManager {

    public static LruCache<String, Bitmap> lruCache;
    private ArrayList<LruCache> imagesCached;

    // Set up the cache with % of memory from heap as passed as the argument

    public static void setUpCache(double percentageOfMemory){

        Runtime runtime = Runtime.getRuntime();

        final int maxMemory = (int) (runtime.maxMemory() / 1024);

        final int cacheSize = (int) (maxMemory * percentageOfMemory / 100);

        lruCache = new LruCache<String, Bitmap>(cacheSize){

            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }

        };
    }

    // Store the bitmap in the cache

    public static void storeBitmapInCache(String key, Bitmap bitmap){
        lruCache.put(key, bitmap);
    }

    // Store array of bitmaps in the cache

    public static void storeBitmapsInCache(String[] keys, Bitmap[] bitmaps){
        int length = keys.length;
        for(int i=0; i<length; i++){
            lruCache.put(keys[i],bitmaps[i]);
        }
    }

    // Retrieve the bitmap from the cache memory

    public static Bitmap getBitmapFromCache(String key){
        return lruCache.get(key);
    }

    // Clear cache memory

    public static boolean clearCache(){
        try{
            lruCache.evictAll();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
