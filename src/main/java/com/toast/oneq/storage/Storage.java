package com.toast.oneq.storage;

public interface Storage {
    
    public Object uploadObject(Object key, Object object);
    
    public Object downloadObject(Object key);
}