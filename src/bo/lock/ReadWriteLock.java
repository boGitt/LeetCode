/**
 * 
 */
package bo.lock;

import java.util.Hashtable;
import java.util.Map;

/**   
 * Copyright: Copyright (c) 2017 bobo
 * 
 * @ClassName: Module.java
 * @Description: 类功能描述
 *
 * @version: v1.0.0
 * @author: yangbo
 * @date: 2018年3月26日 下午10:12:32
 *
 * Modification History:
 * Date         Author          Version            Description
 *---------------------------------------------------------*
 * 2018年3月26日      yangbo          v1.0.0             版本创建
 */
public class ReadWriteLock {
	
	//private int readers = 0;
	private Map<Thread,Integer> readingThreads = new Hashtable<Thread,Integer>();
	
	private int writers = 0;
	
	private int writeRequests = 0;
	
	private Thread writingThread = null;
	
	public synchronized void lockRead() throws InterruptedException {
		Thread callingThread = Thread.currentThread();
		while(! canGrantReadAccess(callingThread))
			wait();
		readingThreads.put(callingThread,getAccessCount(callingThread)+1);
	}
	
	public synchronized void unlockRead() {
		Thread callingThread = Thread.currentThread();
		int accessCount = getAccessCount(callingThread);
		if(accessCount == 1)
			readingThreads.remove(callingThread);
		else
			readingThreads.put(callingThread, accessCount-1);
		notifyAll();
	}
	
	public synchronized void lockWrite() throws InterruptedException {
		writeRequests++;
		Thread callingThread = Thread.currentThread(); 
		while(! canGrantWriteAccess(callingThread))
			wait();
		writers++;
		writeRequests--;
		writingThread = callingThread;
	}
	
	public synchronized void unlockWrite() {
		writers--;
		if(writers == 0)
			writingThread = null;
		notifyAll();
	}

	private int getAccessCount(Thread callingThread) {
		Integer accessCount = readingThreads.get(callingThread);
		return accessCount == null ? 0 : accessCount.intValue();
	}
	
	private boolean canGrantReadAccess(Thread callingThread) {
		if(isWriter(callingThread)) return true;
		if(writers > 0) return false;
		if(isReader(callingThread)) return true;
		if(writeRequests > 0) return false;
		return true;
	}
	
	private boolean isReader(Thread callingThread) {
		return readingThreads.get(callingThread) != null;
	}
	
	private boolean canGrantWriteAccess(Thread callingThread) {
		if(isOnlyReader(callingThread)) return true;
		if(hasReaders()) return false;
		if(writingThread == null ) return true;
		if(! isWriter(callingThread)) return false;
		return true;
	}
	
	private boolean hasReaders(){
		return readingThreads.size() > 0;
	}
	
	private boolean isWriter(Thread callingThread){
		return writingThread == callingThread;
	}
	
	private boolean isOnlyReader(Thread callingThread) {
		return readingThreads.size() == 1 && readingThreads.get(callingThread) != null;
	}
}
