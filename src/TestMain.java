import java.util.List;
import java.util.concurrent.ExecutionException;

import medium.arrayandstrings.Sum3;

/**
 * 
 */

/**   
 * Copyright: Copyright (c) 2018 bobo
 * 
 * @ClassName: TestMain.java
 * @Description: 类功能描述
 *
 * @version: v1.0.0
 * @author: yangbo
 * @date: 2018年3月21日 下午11:30:14 
 *
 * Modification History:
 * Date         Author          Version            Description
 *---------------------------------------------------------*
 * 2018年3月21日      yangbo          v1.0.0             版本创建
 */
public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] nums = {-1, 0, 1, 2, -1, -4};
		try {
			List result = new Sum3().threeSum(nums);
			System.out.println(result.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
