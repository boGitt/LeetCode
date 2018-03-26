/**
 * 
 */
package bo.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**   
 * Copyright: Copyright (c) 2017 bobo
 * 
 * @ClassName: Module.java
 * @Description: 类功能描述
 *
 * @version: v1.0.0
 * @author: yangbo
 * @date: 2018年3月17日 下午11:12:55
 *
 * Modification History:
 * Date         Author          Version            Description
 *---------------------------------------------------------*
 * 2018年3月17日      yangbo          v1.0.0             版本创建
 */
public class CountDownLatchMain {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		// 开始的倒数锁
		   final CountDownLatch begin = new CountDownLatch(1);
		   // 结束的倒数锁
		   final CountDownLatch end = new CountDownLatch(10);
		   // 十名选手
		   final ExecutorService exec = Executors.newFixedThreadPool(10);
		   
		   for (int index = 0; index < 10; index++) {
		    final int NO = index + 1;
		    Runnable run = new Runnable() {
		     public void run() {
		      try {
		       begin.await();//一直阻塞
		       Thread.sleep((long) (Math.random() * 10000));
		       System.out.println("No." + NO + " arrived");
		      } catch (InterruptedException e) {
		      } finally {
		       end.countDown();
		      }
		     }
		    };
		    exec.submit(run);
		   }
		   System.out.println("Game Start");
		   begin.countDown();
		   end.await();
		   System.out.println("Game Over");
		   exec.shutdown();
	}

}
