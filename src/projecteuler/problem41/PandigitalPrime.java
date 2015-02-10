package projecteuler.problem41;

import java.util.Arrays;
import java.util.BitSet;

public class PandigitalPrime {

	/**
	 * Author: James Norcross	
	 * Date: 2/2/15
	 * Purpose: Project Euler Problem 41
	 * Description: Finds the largest n pandigital prime where a number is said to be n pandigital if it contains all
	 * digits 1 to n once and only once
	 * 
	 * NOTE: Of interest is that with my RAM I can't grow heap enough to create a 1 billion element boolean array sieve.  
	 * A BitSet can be used instead that significantly reduces memory requirements.  However, while can create a BitSet
	 * with 1 billion elements it takes 40+ seconds to create the sieve.  In the end I opt for creating boolean array sieves
	 * 100 million at a time, but I have left Bit Set code in file just for future reference.  Do some final timing.  It 
	 * takes 40+ seconds to make the 1 billion element BitSet sieve, while it takes about 2 seconds to make each 100 million
	 * element boolean array sieve.  In conclusion, using the BitSet sieve would make the code shorter but take about 2 times
	 * as long to run
	 * 
	 * After solution see that problem could be solved much more simply and rapidly by recognizing that both 8 and 9 digit
	 * pandigitals would be divisible by 3 so that the greatest one needing to be checked would be a 7 digit pandigital prime
	 */
	
	private static boolean[] baseSieve;
	private static final int MAX = 1000000000;
	
	public static void main(String[] args) {
		
		// try a bit set to help with size, note can actually get a 1 billion element sieve
		// but it takes 40+ seconds to create, opt to go with boolean array and 
		// calculate 100 million elements at a time
//		long startTime = System.currentTimeMillis();
//		BitSet sieve = bitSetSieve();
//		System.out.println("Making bitset sieve takes " + (System.currentTimeMillis() - startTime));
		
		// create base sieve
		baseSieve = makeBaseSieve((int)Math.sqrt((double)MAX));
		
		boolean[] sieve;
		int min, max, greatestPanPrime = 1;
		
		boolean done = false;
		
		for(int numDigits = 9; (numDigits > 1) && !done; numDigits--) {
			
			for(int i = numDigits; (i > 0) && !done; i--) {
				
				min = (int)(i * Math.pow(10.0, (double)(numDigits - 1)));
				max = (int)((i + 1) * Math.pow(10.0, (double)(numDigits - 1))) - 1;
								
				sieve = makeRangeSieve(min, max);
								
				for(int j = max-min; j >= 0; j--) {
					if(sieve[j]) {
						
						if(isNPandigital(j+min, numDigits)) {
							done = true;
							greatestPanPrime = j + min;
							break;
						}
					}
				}
			}
		}
		
		System.out.println("The greatest pandigital prime is " + greatestPanPrime);
		
	}
	
	// creates a prime sieve using a BitSet
	private static BitSet bitSetSieve() {
		
		BitSet bs = new BitSet(MAX + 1);
		bs.set(0, MAX);
		bs.clear(0);
		bs.clear(1);
		
		for(int i = 2; i*i <= MAX; i++)
		{
			if(bs.get(i))
			{
				for(int j = 2*i; j <= MAX; j += i)
				{
					bs.clear(j);
				}
			}
		}

		return bs;
	}
	
	// creates a prime sieve up to max using boolean array
	private static boolean[] makeBaseSieve(int max)
	{
		boolean[] sieve = new boolean[max+1];
		Arrays.fill(sieve, true);
		sieve[0] = false;
		sieve[1] = false;
		for(int i = 2; i*i <= max; i++)
		{
			if(sieve[i])
			{
				for(int j = 2*i; j <= max; j += i)
				{
					sieve[j] = false;
				}
			}
		}
		
		return sieve;
	}
	
	// calculates a prime sieve between min and max given a base sieve
	// works for cases where min is greater than the number of elements in the base sieve
	// and max is less than the square of the number of elements in the base sieve
	private static boolean[] makeRangeSieve(int min, int max)
	{
		int range = max - min;
		boolean[] sieve = new boolean[range+1];
		Arrays.fill(sieve, true);
		
		for(int i = 2; i*i <= max; i++)
		{
			if (baseSieve[i])
			{
				for(int j = i*((int)Math.ceil((double)min/i)); j <= max; j += i)
				{
					sieve[j-min] = false;
				}
			}
		}
		return sieve;
	}

	// returns true if number is n digit pandigital (contains each digit 1 through n once and only once)
	public static boolean isNPandigital(int number, int n) {

		String sNum = String.valueOf(number);
		
		if(sNum.length() != n)
			return false;
		
		for(int i = 1; i <= n; i++) {
			if(!sNum.contains(Integer.toString(i)))
				return false;
		}
		
		return true;
	}
}
