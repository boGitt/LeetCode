/**
 * 
 */
package bo.hard.arrayandstrings;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.Semaphore;

import org.junit.Test;

/**   
 * Copyright: Copyright (c) 2017 bobo
 * 
 * @ClassName: Module.java
 * @Description: 类功能描述
 *
 * @version: v1.0.0
 * @author: yangbo
 * @date: 2018年3月24日 上午8:52:44
 *
 * Modification History:
 * Date         Author          Version            Description
 *---------------------------------------------------------*
 * 2018年3月24日      yangbo          v1.0.0             版本创建
 */
public class Sum4 {
	
	@Test
	public void test(){
		int[] A = {1,2,-1,0,5,-3,-2,4,3},B = {-2,-1,2,1,-5,3,-4,0,4}, C = {-1,2,0,1,3,-2,4,-5,-3}, D = {0,2,-1,1,4,-3,5,-4,-2};
		System.out.println(new Date().getTime());
		System.out.println(fourSumCount(A,B,C,D));
		System.out.println(new Date().getTime());
	}

	public int fourSumCount(int[] A, int[] B, int[] C, int[] D) {
        
		BlockingQueue<List<Integer>>  numsFour = new LinkedBlockingQueue<List<Integer>>();
		Semaphore semaphore = new Semaphore(0);

		ForkJoinPool forkJoinPool = new ForkJoinPool();

		forkJoinPool.submit(new ChooseAction(A, B, C, D, numsFour,semaphore));
		Future future = forkJoinPool.submit(new Calculate(A, B, C, D, semaphore, numsFour));
		
		try {
			System.out.println(((List)future.get()).toString());
			forkJoinPool.shutdown();
			return ((List)future.get()).size();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
    }
	

	private class ChooseAction extends RecursiveAction {

		private static final int THRESHOLD = 2;
		
		private int[] A,  B,  C, D;
		
		private int offset;
		
		private BlockingQueue<List<Integer>>  numsFour;
		
		private Semaphore semaphore;
		
	    public ChooseAction(int[] A, int[] B, int[] C, int[] D,BlockingQueue numsFour,Semaphore semaphore){
	    	this.A = A;
			this.B = B;
			this.C = C;
			this.D = D;
			this.numsFour = numsFour;
			this.semaphore = semaphore;
		}
	    
	    public ChooseAction(int[] A, int[] B, int[] C, int[] D,BlockingQueue numsFour,Semaphore semaphore,int offset){
	    	this.A = A;
			this.B = B;
			this.C = C;
			this.D = D;
			this.numsFour = numsFour;
			this.semaphore = semaphore;
			this.offset = offset;
		}
	    
		/* (non-Javadoc)
		 * @see java.util.concurrent.RecursiveAction#compute()
		 */
		@Override
		protected void compute() {
			// TODO Auto-generated method stub

			semaphore.release();
			
			if(this.A.length > THRESHOLD ) {
				int[] ALeft = new int[A.length/2] ,ARight = new int[A.length-A.length/2];
				for (int i = 0; i < A.length; i++) {
					if(i < A.length/2)
						ALeft[i] = A[i];
					else
						ARight[i-A.length/2] = A[i];
				}

				ChooseAction leftAction = new ChooseAction(ALeft, B, C, D, numsFour,semaphore,offset);
				ChooseAction rightAction = new ChooseAction(ARight, B, C, D, numsFour,semaphore,offset+A.length/2);

				leftAction.fork();
				rightAction.fork();

				
			}else{

				List<Integer> numIndex ;
				for (int i = 0; i < this.A.length; i++) {
					for (int j = 0; j < this.B.length; j++) 
						for (int k = 0; k < this.C.length; k++) 
							for (int l = 0; l < this.D.length; l++) {
								numIndex = new LinkedList<Integer>();
								numIndex.add(offset+i);
								numIndex.add(j);
								numIndex.add(k);
								numIndex.add(l);
								try {
									numsFour.put(numIndex);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
				}
			}
			
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	private class Calculate implements Callable{

		private int length;

		private Semaphore semaphore;

		private int[] A,  B,  C, D;
		
		private BlockingQueue<List<Integer>>  numsFour;
		
		public Calculate(int[] A, int[] B, int[] C, int[] D,Semaphore semaphore,BlockingQueue numsFour){
			this.A = A;
			this.B = B;
			this.C = C;
			this.D = D;
			this.semaphore = semaphore;
			this.numsFour = numsFour;
		}
		
		/* 
		 * @see java.util.concurrent.Callable#call()
		 */
		public List call() {
			// TODO Auto-generated method stub
			List result = new LinkedList<>();
			try {

				while( semaphore.availablePermits() > 0 || numsFour.size() != 0 ){
					LinkedList<Integer> num = (LinkedList<Integer>) numsFour.take();
					if(A[num.get(0)] + B[num.get(1)] + C[num.get(2)] + D[num.get(3)] == 0){
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
