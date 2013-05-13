import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Task2 {
	
	private static final BigInteger TWO = BigInteger.valueOf(2);

	public static List<BigInteger> primeDecomp(BigInteger number) {
		if (number.compareTo(TWO) < 0) {
			return null;
		}

		List<BigInteger> result = new ArrayList<BigInteger>();
		while (number.and(BigInteger.ONE).equals(BigInteger.ZERO)) {
			number = number.shiftRight(1);
			result.add(TWO);
		}

		if (!number.equals(BigInteger.ONE)) {
			BigInteger b = BigInteger.valueOf(3);
			while (b.compareTo(number) < 0) {
				if (b.isProbablePrime(10)) {
					BigInteger[] divideResult = number.divideAndRemainder(b);
					if (divideResult[1].equals(BigInteger.ZERO)) {
						result.add(b);
						number = divideResult[0];
					}
				}
				b = b.add(TWO);
			}
			result.add(b);
		}
		return result;
	}


    public static void main(String[] args) {
    	List<BigInteger> res = primeDecomp(new BigInteger("600851475143"));
		BigInteger maxPrime = Collections.max(res);
        System.out.printf("Result is " + maxPrime);
    }
}