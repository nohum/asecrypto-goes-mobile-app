package at.fhj.gaar.asecrypto.mobile.crypto.carmichael;


import java.util.Scanner;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;

public class Carmichael {
	private static int minBits = 4;
	private static long minFermatTests = 1;
	private static boolean outputInfo = true;
	
	public static void main(String[] args) {
        final Scanner in = new Scanner(System.in);

        try {
		
			System.out.println("This programme initially creates a Carmichael Number which is made up of exactly three prime factors.");
			
			//////////////////////////////////////////////////////
			// create a Carmichael number
			//////////////////////////////////////////////////////
			
			Integer bits = null;
			do{
				System.out.println("\nPlease specify the number of bits the three prime factors shall be made up of (minimum of " + minBits + "):");
				bits = new Integer(in.nextLine());
			}while(bits < minBits);
				
			CarMichaelCreator cmCreator = new CarMichaelCreator();
			AseInteger carMichael = cmCreator.createCMNumber(bits, outputInfo);
			
			// do a series of Fermat Test to demonstrate the danger of Carmichael numbers
			long tests = 0;
			do{
				System.out.println("\nPlease specify the number of Fermat tests you want to execute (minimum of " + minFermatTests + "):");
				tests = new Long(in.nextLine());
			}while(tests < minFermatTests);
			
			FermatTest fermatTest = new FermatTest();
			
			System.out.println("\nDoing Fermat tests unitl one witnesses the compositness of your Carmichael number or " + tests + " tests succeed.");
			boolean isPrime = fermatTest.run(carMichael, tests, outputInfo);
			if(isPrime){
				System.out.println("Your Fermat tests did not detect that your Carmichael number is not prime!");
			} else {
				System.out.println("Your Fermat test detected the compositness of your Carmichael number!");
			}
		} finally {
			System.out.println("Program finished...");
		}
	}
	//5 bit, failed after 185 tests in 51ms
	//10 bit, failed after 4245 tests in 231ms
	//15bit, failed after 237 tests in 111ms
	//20bit, failed after 1323260 tests in 58047ms
	//25bit, failed after 33531787 tests in 1729431ms
	//30bit, aborted after ~50000000 tests in 2083793ms
	
}
