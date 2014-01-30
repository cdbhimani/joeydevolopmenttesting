package com.emptyPockets.utils;

import java.util.ArrayList;

public class ObjectPool<T> {
	ArrayList<T> pool;

	public ObjectPool() {
		pool = new ArrayList<T>();
	}

	public void release(T object) {
		synchronized (pool) {
			pool.add(object);
			pool.notify();
		}
	}

	public boolean hasNext() {
		synchronized (pool) {
			return (pool.size() > 0);
		}

	}

	public T getNext() {
		synchronized (pool) {
			while (true) {
				if (pool.size() > 0) {
					T rst = pool.get(0);
					pool.remove(rst);
					return rst;
				} else {
					try {
						pool.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
