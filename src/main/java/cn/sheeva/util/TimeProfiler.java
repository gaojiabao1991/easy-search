package cn.sheeva.util;

import java.util.concurrent.TimeUnit;

/**
 * 用于统计一段操作的使用时间，在开始处调用begin(),结束处调用end(),可以跨方法，跨类，但是要求begin()和end()在同一个线程中
 * @author sheeva
 *
 */
public class TimeProfiler {
	private static final ThreadLocal<Long> times=new ThreadLocal<>();
	
	public static final void begin(){
		times.set(System.currentTimeMillis());
	}
	
	public static final long end(){
		return System.currentTimeMillis()-times.get();
	}

	
	public static void main(String[] args) throws InterruptedException {
		TimeProfiler.begin();
		TimeUnit.SECONDS.sleep(1);
		System.out.println(TimeProfiler.end());
	}
}
