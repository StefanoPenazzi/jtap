package projects.CTAP.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProbDistEvenHomeActivityLocationTest {

	@Test
	void test() {
		double arr[] = {0.1,0.1,0.3,0.2,0.3};
		double[] destProbCDF = new double[arr.length];
		destProbCDF[0]=arr[0];
		for(int i =1;i<arr.length;i++) {
			destProbCDF[i]=destProbCDF[i-1]+arr[i];
		}
		int res = binarySearchForIndex(destProbCDF,0,destProbCDF.length-1,0.25);
		System.out.println(res);
	}
	
	private int binarySearchForIndex(double arr[], int l, int r, double x)
    {
        if (r >= l) {
            int mid = l + (r - l) / 2;
 
            if (arr[mid] == x)
                return mid;
            //left
            if (arr[mid] > x) {
            	if (arr[mid-1]<x) return mid;
                return binarySearchForIndex(arr, l, mid - 1, x);
            }
            //right
            else {
            	if (arr[mid+1]>x) return mid+1;
            	return binarySearchForIndex(arr, mid + 1, r, x);
            }
        }
        return -1;
    }

}
