package concolic;

import gov.nasa.jpf.symbc.Concrete;
import gov.nasa.jpf.symbc.Partition;

public class PartitionEx {
	
	public static void runSymbolic(double y) {
			if(y > runConcrete(y)) {
				System.out.println("greater than the input ");
			} else {
				System.out.println("less than the input");
			}
			System.out.println("x > 10");
	}
	
	@Concrete("true")
	@Partition({"z within 10 +- 4", "z > 0",
					"z within 10 +- 15"})
	public static double runConcrete(double z) {
		if(z == 10) {
			return z / 1.2;
		}
		return z * 1.2;
	}
	
	public static void main(String[] args) {
		PartitionEx.runSymbolic(10.0);
	}
}