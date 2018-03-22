/**
 * 
 */
package easy.array;

import java.util.Arrays;

/**   
 * Copyright: Copyright (c) 2017 bobo
 * 
 * @ClassName: Module.java
 * @Description: 类功能描述
 *
 * @version: v1.0.0
 * @author: yangbo
 * @date: 2018年3月20日 下午10:59:13
 *
 * Modification History:
 * Date         Author          Version            Description
 *---------------------------------------------------------*
 * 2018年3月20日      yangbo          v1.0.0             版本创建
 */
public class Rotate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] nums = {1,2,3,4,5,6,7};
		
		System.out.println(Arrays.toString(rotate(nums,3)));
	}
	
	public static int[] rotate(int[] nums, int k) {
	
        int[] result = new int[nums.length];
        for (int i = 0; i < result.length; i++) {
			if(i < k)
				result[i] = nums[nums.length-k+i];
			else
				result[i] = nums[i-k];
		}
        
        return result;
    }
	
	public static int[] rotateMiddle(int[] nums, int k) {
		
        int temp;
        for (int i = 0; i < k; i++) {
			temp = nums[i];
			nums[i] = nums[nums.length-k+i];
			nums[nums.length-k+i] = temp;
		}
        
        return nums;
    }
	
}
