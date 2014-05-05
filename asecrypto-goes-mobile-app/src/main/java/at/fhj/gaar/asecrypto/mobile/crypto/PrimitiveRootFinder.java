package at.fhj.gaar.asecrypto.mobile.crypto;
import java.util.Random;

import at.fhj.gaar.asecrypto.mobile.util.StopWatch;


public class PrimitiveRootFinder {
    private AseInteger myModulus = AseInteger.ONE; // a prime modulus number "p" with the property: p-1 = 2q (where q is another prime)
    private AseInteger myPrimeQ = AseInteger.ONE; // the prime number q of the formula: p-1 = 2q
    private int myBits = 1;

    private static final int numOfTests = 50;
    private static final AseInteger TWO = new AseInteger("2");

    /**
     * @param bits specifies the number of bits the primitive root and modulus number shall be internally made of
     * @param outputInfo decides whether information about the process of findining a primitive root shall be sent to system.out
     */
    /**
     * @param bits
     * @param outputInfo
     */
    public PrimitiveRootFinder(int bits, boolean outputInfo){
        // check the input argument
        if(bits >= 1){
            myBits = bits;
        } else {
            throw new IllegalArgumentException("" + bits + " is an illegal value for parameter bits.");
        }

        StopWatch watch = new StopWatch();
        watch.start();
        // do until you found a matching prime
        for(int i=1; true; i++){
            // generate a random prime (using a suitable AseInteger constructor)
            AseInteger mod = new AseInteger(bits, numOfTests, new Random());

            // check if this (prime-1)/2 is another prime (AseInteger provides the isProbablePrime() method for this)
            AseInteger q = mod.subtract(AseInteger.ONE).divide(TWO);
            if (q.isProbablePrime(numOfTests)) {

                watch.stop();

                // inform the user how many trials it took to find a prime following the formula: p-1=2*q
                if(outputInfo){
                    System.out.println("Found a special prime modulus number after " + i + " tests in " + watch.getElapsedTime() + " ms");
                }

                // store the modulus number "p" and "(p-1)/2" in member variables
                setPrimes(mod, q);
                break;
            }
        }
    }

    /**
     * @param outputInfo decides whether information about the process of findining a primitive root shall be sent to system.out
     * @return a new primitive root
     */
    public AseInteger createPrimitiveRoot(boolean outputInfo){
        StopWatch watch = new StopWatch();
        watch.start();
        int trails = 0;
        while(true) {
            // create a random AseInteger as a candidate
            AseInteger r = new AseInteger(getBits(), new Random());
            // make sure the candidate is smaller than the modulus number
            if (r.compareTo(getModulus()) >= 0) continue;
            // only count trails with an suitable random candidate
            trails++;
            // check the order of the candidate
            if (r.modPow(TWO, getModulus()).compareTo(AseInteger.ONE) == 0) continue;
            if (r.modPow(getPrimeQ(), getModulus()).compareTo(AseInteger.ONE) == 0) continue;

            watch.stop();
            // output how many trials it took to find a primitive root
            if(outputInfo){
                System.out.println("Found primitive root after " + trails + " trials in " + watch.getElapsedTime() + " ms");
            }
            return r;
        }
    }

    private int getBits() {
        return myBits;
    }

    /**
     * @param modulus is the prime modulus number following the formula: modulus - 1 = 2*primeQ (where primeQ is prime)
     * @param primeQ is the prime number following the formula: modulus - 1 = 2*primeQ
     */
    private void setPrimes(AseInteger modulus, AseInteger primeQ){
        // verify the contract that modulus - 1 = 2*primeQ
        if( modulus.subtract(AseInteger.ONE).divide(TWO).compareTo(primeQ) != 0){
            throw new IllegalArgumentException("Your primes do not stick to the formula: \"modulus - 1 = 2*primeQ\"");
        }

        myModulus = modulus;
        myPrimeQ = modulus.subtract(AseInteger.ONE).divide(TWO);
    }

    public AseInteger getModulus(){
        return myModulus;
    }

    private AseInteger getPrimeQ(){
        return myPrimeQ;
    }
}