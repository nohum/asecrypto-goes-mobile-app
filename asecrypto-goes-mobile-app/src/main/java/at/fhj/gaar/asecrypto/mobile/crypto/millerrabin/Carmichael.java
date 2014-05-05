package at.fhj.gaar.asecrypto.mobile.crypto.millerrabin;

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
			
			MillerRabinTest millerRabinTest = new MillerRabinTest();
			
			System.out.println("\nDoing Fermat tests unitl one witnesses the compositness of your Carmichael number or " + tests + " tests succeed.");
//			carMichael = new AseInteger("611949");
			boolean isPrime = millerRabinTest.run(carMichael, tests, outputInfo);
			if(isPrime){
				System.out.println("Your Fermat tests did not detect that your Carmichael number is not prime!");
			} else {
				System.out.println("Your Fermat test detected the compositness of your Carmichael number!");
			}
			
			// do a series of Miller Rabing test to demonstrate that Carmichael numbers are no match for Miller-Rabin
			
			
			
		} finally {
			System.out.println("Program finished...");
		}
	}
}
