/**
 * 
 */
package bo.easy.array;

import java.util.Arrays;

/**   
 * Copyright: Copyright (c) 2017 bobo
 * 
 * @ClassName: Module.java
 * @Description: 类功能描述
 *
 * @version: v1.0.0
 * @author: yangbo
 * @date: 2018年3月20日 下午9:07:19
 *
 * Modification History:
 * Date         Author          Version            Description
 *---------------------------------------------------------*
 * 2018年3月20日      yangbo          v1.0.0             版本创建
 */
public class RemoveDuplicates {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] nums = {1,1,2,3,2};
		System.out.println(removeDuplicates(nums));
	}

	public static int removeDuplicates(int[] nums) {
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
        	for (int k = i+1; k < nums.length; k++) {
    			if( nums[i] == nums[k] )
    				count++;
    		}
        	nums[i] = count;
        	count = 0;
		}
        System.out.println(Arrays.toString(nums));
        int length = 0;
        for (int i = 0; i < nums.length; i++) {
			if(nums[i] == 0)
				length++;
			
		}
		return length;
    }
	

}
