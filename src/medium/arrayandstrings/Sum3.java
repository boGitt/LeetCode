/**
 * 
 */
package medium.arrayandstrings;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**   
 * Copyright: Copyright (c) 2018 bobo
 * 
 * @ClassName: Sum3.java
 * @Description: 类功能描述
 *
 * @version: v1.0.0
 * @author: yangbo
 * @date: 2018年3月21日 下午3:00:42 
 *
 * Modification History:
 * Date         Author          Version            Description
 *---------------------------------------------------------*
 * 2018年3月21日      yangbo          v1.0.0             版本创建
 */
public class Sum3 {

	private BlockingQueue<List<Integer>> queueOfThreeNums = new LinkedBlockingQueue<List<Integer>>();	

	private BlockingQueue<List<Integer>> queueOfSums = new LinkedBlockingQueue<List<Integer>>();	
	
	public List<List<Integer>> threeSum(int[] nums) throws InterruptedException, ExecutionException {
		
		ExecutorService executor = Executors.newFixedThreadPool(3);
		Semaphore semaphore = new Semaphore(0);
		
		executor.execute(new Choose(nums,semaphore));
		executor.execute(new Calculate(semaphore));
		Future future = executor.submit(new Unduplicate(semaphore));
        
		executor.shutdown();
		
        return (List<List<Integer>>) future.get();
    }

	
	public class Choose implements Runnable{

		private int[] nums;
		
		private Semaphore semaphore;
		
		public Choose(int[] nums,Semaphore semaphore){
			this.nums = nums;
			this.semaphore = semaphore;
		}
		/* 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			// TODO Auto-generated method stub
			List<Integer> num = new LinkedList<Integer>();
			for (int i = 0; i < nums.length-2; i++) {
				for (int j = i+1; j < nums.length-1; j++) {
					for (int k = j+1; k < nums.length; k++) {
						num.add(nums[i]);
						num.add(nums[j]);
						num.add(nums[k]);
						try {
							queueOfThreeNums.put(num);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						num = new LinkedList<Integer>();
					}
					
				}
			}
			semaphore.release();
		}
	}
	
	private class Calculate implements Runnable{

		private int length;

		private Semaphore semaphore;
		
		public Calculate(Semaphore semaphore){
			this.semaphore = semaphore;
		}
		
		/* 
		 * @see java.util.concurrent.Callable#call()
		 */
		public void run() {
			// TODO Auto-generated method stub
			try {				
				while( semaphore.availablePermits() == 0 || queueOfThreeNums.size() != 0){
					LinkedList<Integer> num = (LinkedList<Integer>) queueOfThreeNums.take();
					if(num.get(0) + num.get(1) + num.get(2) == 0){
						queueOfSums.put(num);
					}
				}
				semaphore.release();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private class Unduplicate implements Callable {

		private Semaphore semaphore;
		/**
		 * @param semaphore
		 */
		public Unduplicate(Semaphore semaphore) {
			// TODO Auto-generated constructor stub
			this.semaphore = semaphore;
		}

		/* 
		 * @see java.util.concurrent.Callable#call()
		 */
		public List<List<Integer>> call() throws Exception {
			// TODO Auto-generated method stub
			List result = new LinkedList();
			boolean duplicateFlag = false;
			try {				
				while( semaphore.availablePermits() < 2 || queueOfSums.size() != 0 ){
					LinkedList<Integer> num = (LinkedList<Integer>) queueOfSums.take();
					if(result.size() == 0 )
						result.add(num);
					else {
						for (Iterator iterator = result.iterator(); iterator.hasNext();) {
							List tempNum = (LinkedList<Integer>) iterator.next();
							if(tempNum.containsAll(num))
								duplicateFlag = true;
						}
						if(duplicateFlag)
							continue;
						result.add(num);
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}
		
	}

}


