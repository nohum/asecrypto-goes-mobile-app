package at.fhj.gaar.asecrypto.mobile.crypto.millerrabin;

import java.util.Random;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.util.StopWatch;

public class FermatTest {
	
	// after N tests, the user gets an informational message
	private static long informUserAfterNTests = 10000;
	
	/**
	 * This function creates a random AseInteger between 0 and modulus
	 * You can use this number as a witness in a Fermat test to check the primality of modulus
	 * 
	 * @param modulus is an upper bound for your test number
	 * @return a random AseInteger guaranteed to be >0 and <modulus
	 */
	private AseInteger createRandomTestNumber(AseInteger modulus){
		
		// get a random number
		AseInteger testNumber = new AseInteger(modulus.bitLength(), new Random());
		
		// assure that it is positive and smaller modulus
		testNumber = testNumber.mod(modulus);
		
		// assure that it is non zero
		if(testNumber.compareTo(AseInteger.ZERO)==0){
			testNumber = modulus.subtract(AseInteger.ONE);
		}
		return testNumber;
	}
	
	/**
	 * @param possiblePrime is the number to check for primality
	 * @param numOfTests is the number of times you want the test to run
	 * @param outputInfo decides whether info should be output to stdout for the user
	 * @return gives true if possiblePrime passed all tests, else false
	 */
	public boolean run(AseInteger possiblePrime, final long numOfTests, final boolean outputInfo){
		if(numOfTests <=0){
			return false;
		}
							
		StopWatch watch = new StopWatch();
		watch.start();
		
		// if possiblePrime is really prime, its phi is its value-1
		AseInteger exponent = possiblePrime.subtract(AseInteger.ONE);
		
		////////////////////////////////////////////////////////////////////
		// start a series of numOfTests FermatTests
		// that is exponentiate a test number 
		long counter = 1;
		for(counter=1; counter<=numOfTests; counter++){
			
			// create a random AseInteger as witness
			AseInteger a = createRandomTestNumber(possiblePrime);
			
			// check if a^(p-1) mod p <> 1
			if(a.modPow(exponent, possiblePrime).compareTo(AseInteger.ONE) != 0){
			// if test failed
				watch.stop();
				if(outputInfo){
					System.out.println("Fermat test " + counter + " failed after " + watch.getElapsedTime() + " ms, " + possiblePrime + " is not a prime number!");
				}
				return false;
			}
			
			// inform the user of current state
			if(outputInfo && counter%informUserAfterNTests==0)
			{
				System.out.println(counter + " Fermat tests succeeded after " + watch.getElapsedTime() + " ms");
			}
		}
		
		// adjust counter to the number of tests actually done for correct output
		
		
		// all Fermat tests succeeded
		watch.stop();
		if(outputInfo){
			System.out.println("\nAfter " + watch.getElapsedTime() + " ms " + possiblePrime + " finally passed " + counter + " Fermat tests!" );
		}
		return true;
	}
}
