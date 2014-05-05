package at.fhj.gaar.asecrypto.mobile.crypto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Random;

/**
 * 
 * Class AseInteger implements the most important methods of BigInteger and adds the algorithms of our semester as methods
 * BigInteger is used as a delegate for the implementation of the BigInteger methods, so that the return values and parameters are all AseIntegers
 *
 */
public class AseInteger implements Comparable<AseInteger> {
	
	public static final AseInteger ZERO = new AseInteger(BigInteger.ZERO);
	public static final AseInteger ONE = new AseInteger(BigInteger.ONE);
	public static final AseInteger TEN = new AseInteger(BigInteger.TEN);
	
	private BigInteger myValue;

	private BigInteger getMyValue() {
		return myValue;
	}

	private void setMyValue(BigInteger myValue) {
		this.myValue = myValue;
	}

	public AseInteger(BigInteger val){
		setMyValue(val);
	}
	
	public AseInteger(byte[] val) {
		setMyValue( new BigInteger(val) );
	}
		

	public AseInteger(int signum, byte[] magnitude) {
		setMyValue( new BigInteger(signum, magnitude) );
	}


	public AseInteger(int bitLength, int certainity, Random rnd) {
		setMyValue( new BigInteger(bitLength, certainity, rnd) );
	}

	
	// create a random AseInteger
	public AseInteger(int numBits, Random rnd) {
		setMyValue( new BigInteger(numBits, rnd) );
	}

	
	// create a random prime AseInteger
	public AseInteger(int numBits) {
		setMyValue( BigInteger.probablePrime(numBits, new Random()) );
	}

	
	public AseInteger(String val, int radix) {
		setMyValue( new BigInteger(val, radix) );
	}


	public AseInteger(String val) {
		setMyValue( new BigInteger(val) );
	}
	

	public AseInteger add(AseInteger arg0) {
		return new AseInteger( this.getMyValue().add(arg0.getMyValue() ) );
	}
	
	public static AseInteger valueOf(long i){
		return  new AseInteger( BigInteger.valueOf(i));
	}
	
	public int compareTo(AseInteger arg0) {
		return this.getMyValue().compareTo(arg0.getMyValue());
	}

	// standard integer division yielding quotient only
	public AseInteger divide(AseInteger arg0) {
		return new AseInteger( this.getMyValue().divide(arg0.getMyValue()) );
	}

	// standard integer division yielding quotient and remainder
	public AseInteger[] divideAndRemainder(AseInteger arg0) {
		BigInteger[] res = this.getMyValue().divideAndRemainder(arg0.getMyValue());
		AseInteger[] ret = new AseInteger[res.length];
		for(int i=0; i<res.length; i++){
			ret[i]=new AseInteger( res[i] );
		}
		return ret;
	}

	// standard multiplication operation
	public AseInteger multiply(AseInteger arg0) {
		return new AseInteger( this.getMyValue().multiply(arg0.getMyValue()) );
	}

	// standard substraction operation
	public AseInteger subtract(AseInteger arg0) {
		return new AseInteger( this.getMyValue().subtract(arg0.getMyValue()) );
	}
	
	// standard exponentiation operation
	public AseInteger pow(int exponent){
		return new AseInteger( this.getMyValue().pow(exponent));
	}
	
	// standard modulus operation
	public AseInteger mod(AseInteger modNum){
		return new AseInteger( this.getMyValue().mod(modNum.getMyValue()) );
	}
	
	// test wether the bit specified in the paramters is set to 1 (least significant bit has position 0)
	public boolean testBit(int bit){
		return this.getMyValue().testBit(bit);
	}
	
	public AseInteger setBit(int position){
		return new AseInteger( this.getMyValue().setBit(position));
		
	}
	
	public int bitCount(){
		return this.getMyValue().bitCount();
	}
	
	public int bitLength(){
		return this.getMyValue().bitLength();
	}
	
	// bit-shift the AseInteger right by the number of bits specified in the parameter
	public AseInteger shiftRight(int bits){
		return new AseInteger(this.getMyValue().shiftRight(bits));
	}
	
	// bit-shift the AseInteger left by the number of bits specified in the parameter
	public AseInteger shiftLeft(int bits){
		return new AseInteger(this.getMyValue().shiftLeft(bits));
	}
	
	// test AseInteger for primality
	public boolean isProbablePrime(int numOfTests){
		return this.getMyValue().isProbablePrime(numOfTests);
	}
	
	// get the next prime number following the given AseInteger 
	// implicitely calls isProbablePrime()
	public AseInteger nextProbablePrime() {
		return new AseInteger( this.getMyValue().nextProbablePrime() );
	}
	
	/**
	 * modPow() exponentiation of this. 
	 * 		 We replace the exponentiation function's implementation found in BigInteger,
	 *       by our own smart implementation
	 * 
	 * @param exponent - the exponent is now an AseInteger instead of an int such that we can do huge exponentiations
	 * @return An AseInteger holding the gcd.
	 */
	public AseInteger modPow(AseInteger exponent, AseInteger mod){
		// For each bit of the exponent, we square the basis to get a "squared result", 
		// If the bit is 1, we multiply our intermediate result with the "squared result"
		// This will provide us with the final result after working through all bits
				
		AseInteger squaredResult = this;
		AseInteger intermediateResult = AseInteger.ONE;
				
		do{
			if(exponent.testBit(0)){
				intermediateResult = intermediateResult.multiply(squaredResult).mod(mod);
			}
					
			// prepare for the next run
			squaredResult = squaredResult.multiply(squaredResult).mod(mod);
			exponent = exponent.shiftRight(1);
		}while(exponent.compareTo(AseInteger.ZERO) >0);
		return intermediateResult;
	}
	
	public AseInteger modPowChinese(AseInteger exponent, AseInteger p, AseInteger q){
		
		// two exponentiations mod p and mod q instead of one big exponentiation mod pq
		AseInteger result1 = this.modPow(exponent, p);
		AseInteger result2 = this.modPow(exponent, q);
		
		// now we need the correct name of the solution
		// which integer x satisfies
		// x congruent result1 mod p
		// x congruent result2 mod q
		//
		// Use the algo of the Chinese Remainder Theorem to find x
		// x = x1 + x2
		// x1 = result1*q*inv_q (mod p)
		// x2 = result2*p*inv_p (mod q)
		// 
		// Beware, the resulting x need not be the smallest positive representative of its residue class
		// -> Transform x into the standardized name of residue class [x] mod pq
		
		AseInteger inv_q = q.getBezoutRec(p)[0];
		inv_q = inv_q.mod(p);
		
		AseInteger inv_p = p.getBezoutRec(q)[0];
		inv_p = inv_p.mod(q);
		
		AseInteger x1 = result1.multiply(q).multiply(inv_q);
		AseInteger x2 = result2.multiply(p).multiply(inv_p);
		return x1.add(x2).mod(p.multiply(q));
	}

	public String toString(){
		return this.getMyValue().toString();
	}

	
	/**
	 * getGcd() returns the gcd of the current and another numbers using the Euclidean Algorithm
	 * 
	 * @param b The other number, the gcd of this and b is calculated 
	 * @return An AseInteger holding the gcd.
	 */
	public AseInteger getGcd(AseInteger b) {
		AseInteger a = this;
		AseInteger q,r;
		
		// make sure that a is the dividend and b the divisor
		if( this.compareTo(b) < 0 ){ // if a < b, then just switch a and b
			a = b ;
			b = this;
		}
		
		// iterate over all steps of the algorithm
		do{
			// a=bq+r
			AseInteger[] res = a.divideAndRemainder(b); 
			q=res[0];
			r=res[1];
			
			// output the numbers in a=bq+r format to the screen, so that the user can view the algorithm running step by step
			System.out.println(a + "=" + b + "*" + q + "+" + r);
			
			// b becomes your new a, r becomes your new b
			a = b; // prepare the next row of the algorithm
			b = r;
			
			// iterate until no remainder is left
		}while(r.compareTo(AseInteger.ZERO) != 0);
		
		// the gcd is the b of the last division - which was already moved to a
		return a;
	}
	
	/**
	 * getBezout() returns the factors x and y of bezouts equation as well as the gcd for the two integers this and b
	 * 	
	 * a*x + b*y = gcd
	 * 
	 * @param b The other integer, the bezout factors x and y are calculated for this (x) and b (y) 
	 * @return An array of three AseInteger holding x (element 0) and y (element 1) and the gcd (element 2)
	 *
	 */
	public AseInteger[] getBezout(AseInteger b) {
		AseInteger a = this;
		AseInteger q,r;
		
		List<AseInteger> negQuotients = new ArrayList<AseInteger>();
		List<AseInteger> outputNumbers = new ArrayList<AseInteger>();
		
		// iterate over all steps of the algorithm, creating the Bezout equations on the fly
		do{
			// a = bq  + r 
			// r = 1*a -(q)*b -> for substitution step in extension we have to store a and -q
			AseInteger[] res = a.divideAndRemainder(b);
			q=res[0];
			r=res[1];
			AseInteger negQuotient = AseInteger.ZERO.subtract(q);
			negQuotients.add(0, negQuotient); // add intermediate results at top of list
			outputNumbers.add(0, a);
			
			// output the numbers in a=bq+r format to the screen, so that the user can view the algorithm running step by step
			System.out.println(a + "=" + b + "*" + q + "+" + r);
			
			// b becomes your new a, r becomes your new b
			a = b; // prepare the next row of the algorithm
			b = r;
			
			// iterate until no remainder is left
		}while(r.compareTo(AseInteger.ZERO) != 0);
		
		// the gcd is the b of the last division - which was already moved to a
		AseInteger gcd = a;
		System.out.println("\ngcd="+gcd+"\n");
		
		// now iterate over the substitutions of the remainders to get Bezouts equation
		Iterator<AseInteger> iter = negQuotients.iterator();
		
		// Last step of Euclid: b=rq + 0 -> r is the gcd, this quotient q is not needed for Bezout!
		iter.next();
		
		// for b = rq + 0 we get the initial Bezout equation
		// -> gcd = b*0 + r*1
		AseInteger x = AseInteger.ZERO;
		AseInteger y = AseInteger.ONE;
				
		// prepare nice output
		Iterator<AseInteger> outputIter = outputNumbers.iterator();
		AseInteger bOutput = gcd;
		AseInteger aOutput = outputIter.next();
		
		while(iter.hasNext()){
			// a = bq+r
			// get the -q from that equation
			AseInteger minusQ = iter.next();
			bOutput = aOutput;
			aOutput = outputIter.next();
			
			// gcd = b*x + r*y -> substitute r with r = a - bq 
			// -> gcd = b*x + (a-bq)*y
			// -> gcd = a*y + b*(x-qy) -> X = y, Y = x-qy
			AseInteger Y = x.add(minusQ.multiply(y));
			AseInteger X = y;
			System.out.println(gcd + "=" + aOutput + "*" + X + "+" + bOutput + "*" + Y);
			x=X;
			y=Y;
		}
		
		AseInteger[] retArray = new AseInteger[3];
		retArray[0]=x;
		retArray[1]=y;
		retArray[2]=gcd;
		return retArray;
	}
	
	/**
	 * getGcdRec() returns the gcd of the current and another numbers using the Euclidean Algorithm in recursive fashion
	 * 
	 * @param b The other number, the gcd of this and b is calculated 
	 * @return An AseInteger holding the gcd.
	 */
	public AseInteger getGcdRec(AseInteger b) {
		AseInteger a = this;
		
		// make sure that a is the dividend and b the divisor
		if( this.compareTo(b) < 0 ){ // if a < b, then just switch a and b
			a = b ;
			b = this;
		}
		
		// a=bq+r
		AseInteger[] res = a.divideAndRemainder(b);
		
		// are we finished? (remainder == zero)
		if(res[1].compareTo(AseInteger.ZERO) == 0){
			
			// then return the last divisor
			return b;
		} else {
			
			// otherwise return gcd(b,r) which we know to be equal to gcd(a,b)
			return b.getGcdRec(res[1]);
		}
	}
	
	/**
	 * getBezoutRec() returns the factors x and y of bezouts equation as well as the gcd for the two integers this and b in recursive fashion
	 * 
	 * @param b The other integer, the bezout factors x and y are calculated for this (x) and b (y) 
	 * @return An array of three AseInteger holding x (element 0) and y (element 1) and the gcd (element 2)
	 */
	public AseInteger[] getBezoutRec(AseInteger b) {
		
		AseInteger[] retArray = new AseInteger[3];
		AseInteger a = this;
		
		// a=bq+r
		AseInteger[] res = a.divideAndRemainder(b);
		AseInteger q = res[0];
		AseInteger r = res[1];
		
		// are we finished? (remainder == zero)
		if(r.compareTo(AseInteger.ZERO) == 0){
			// a = bq -> b is the gcd, b = a*0 + b*1
			AseInteger x = AseInteger.ZERO;
			AseInteger y = AseInteger.ONE;
			retArray[0] = x;
			retArray[1] = y;
			retArray[2] = b;
		} else {
			// get the result of the extended euclidean algorithm for (b,r) -> gcd = b*x + r*y
			retArray = b.getBezoutRec(r);
			AseInteger x = retArray[0];
			AseInteger y = retArray[1];
			AseInteger gcd = retArray[2];
			
			// gcd = b*x + r*y AND r = a + (-q)b -> gcd = b*x + ( a+(-q)b)y 
			//-> gcd = a*y + b*x + b*(-q)y -> gcd = a*y + b*(x+y(-q)) -> X=y, Y = x-qy
			AseInteger X = y;
			AseInteger Y = x.subtract(q.multiply(y));
			retArray[0] = X;
			retArray[1] = Y;
			retArray[2] = gcd;
		}
		return retArray;
	}
	
	public AseInteger getGcdFact(AseInteger b) {
		AseInteger a = this;
		NavigableMap<AseInteger, Integer> aPrimeFactors = a.getPrimeFactors();
		NavigableMap<AseInteger, Integer> bPrimeFactors = b.getPrimeFactors();
		AseInteger gcd = AseInteger.ONE;
		
		Entry<AseInteger, Integer> entry = aPrimeFactors.firstEntry();
		do{
			Integer exponentB = bPrimeFactors.get(entry.getKey());
			AseInteger prime = entry.getKey();
			Integer exponentA = entry.getValue();
			
			if(exponentB != null){
				if( exponentA.compareTo(exponentB) < 0)
				{
					gcd = gcd.multiply( prime.pow( exponentA ) );
					System.out.println("prime: " + prime + " exponent: " + exponentA);
				} else {
					gcd = gcd.multiply( prime.pow( exponentB ) );
					System.out.println("prime: " + prime + " exponent: " + exponentB);
				}
			}
			entry = aPrimeFactors.higherEntry(prime);
		}while(entry != null);
		
		return gcd;
	}
	
	/**
	 * getPrimeFactors() calculates the prime factorization of this
	 * 
	 * @return Returns the prime factorization in a key value map, the keys being the primes, the values being the exponents
	 */
	public NavigableMap<AseInteger, Integer> getPrimeFactors(){
		NavigableMap <AseInteger, Integer> retmap = new TreeMap<AseInteger, Integer>();
		AseInteger number = this;
		AseInteger prime = new AseInteger("2");
		Integer exponent = 0;
		
		while(prime.multiply(prime).compareTo(number)<=0){
			AseInteger[] result = number.divideAndRemainder(prime);
			AseInteger quotient = result[0];
			AseInteger remainder = result[1];
			if(remainder.compareTo(AseInteger.ZERO) == 0){ // prime did divide number
				number = quotient;
				exponent = new Integer( exponent.intValue()+1 );
				retmap.put(prime, exponent);
			} else {
				exponent = new Integer(0);
				prime = prime.nextProbablePrime();
			}
		}
		if(prime.compareTo(number) == 0){
			exponent = new Integer( exponent.intValue()+1 );
			retmap.put(number, exponent);
		} else {
			retmap.put(number, 1);
		}
		
		System.out.println("Retmap" + retmap);
		return retmap;
	}
	
	// calculate phi(this) using the getPrimeFactors method
	public AseInteger getPhi(){
		AseInteger phi = AseInteger.ONE;
		NavigableMap<AseInteger, Integer> primeFactors = this.getPrimeFactors();
		Entry<AseInteger, Integer> primePlusExponent = primeFactors.firstEntry();
		while(primePlusExponent != null){
			final AseInteger prime = primePlusExponent.getKey();
			final int exp = primePlusExponent.getValue().intValue();
			phi = phi.multiply(prime.pow(exp-1).multiply(prime.subtract(AseInteger.ONE)));
			primePlusExponent = primeFactors.higherEntry(prime);
		}
			
		return phi;
	}
}
