package F28DA_CW1;

import java.util.Iterator;

public class HashWordMap implements IWordMap, IHashMonitor 
{
	// word entry array hash table
	private WordEntry[] table;
	
	// maximum load factor value
	private float maxLoadFactor;
	
	// number entries in the table
	private int wordEntryCounter;
	
	// second hash function prime number
	private int prime2;
	
	// probes counter
	// private int probesCounter;
	
	// Constructor overloading
	public HashWordMap()
	{
		// Assigning default float value of 0.5
		this((float) 0.5);
	}
	
	// Main Constructor 
	public HashWordMap(float f)
	{
		// Sets max load factor according the value given
		this.maxLoadFactor = f;
		
		// Sets the initial word entry array to 13
		this.table = new WordEntry[13];
		
		// Sets the initial number of word entry counter
		this.wordEntryCounter = 0;
	}

	@Override
	public void addPos(String word, IPosition pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeWord(String word) throws WordException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removePos(String word, IPosition pos) throws WordException {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterator<String> words() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<IPosition> positions(String word) throws WordException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numberOfEntries()
	{
		// return the word entries counter
		return this.wordEntryCounter;
	}

	public float averNumProbes() {
		// TODO Auto-generated method stub
		return 0f;
	}

	@Override
	public float getMaxLoadFactor()
	{		
		// returns the max load factor given at the constructor
		return this.maxLoadFactor;
	}

	@Override
	public float getLoadFactor()
	{
		// returns the current table load factor
		return ( this.numberOfEntries() / this.table.length );
	}

	@Override
	public int hashCode(String s)
	{
		// It returns the polynomial accumulation to determine the string hash code
		
		// it accumulates the polynomial of each string character
		int polynomialAccumulator = 0;
		for(int i = 0; i < s.length() ; i++)
		{
			// Getting character at each position
			char c = s.charAt(i);
			
			
			// return a polynomial equal to the power (length - i - 1) of a prime number 33
			// 33 is the next prime number after 31 (used in the Java language to create hash code)
			int polynomial = (int) Math.pow(33, (s.length() - i - 1) );
			
			// multiplying the power of 33 by the character ASCII code
			polynomial *= (int) c;

			// adding each character accumulator to the accumulator			
			polynomialAccumulator += polynomial;
			
		}
		
		// returning the polynomial accumulator
		return polynomialAccumulator;
	}
	
	
	// adds a word entry to the map
	private void addWordEntry(String word)
	{
		
	}
	
	// compression function, which compresses entries in the hash table
	private int hashFunction(String word)
	{
		// it is equal to the modulus of the string hash code and the table length
		return ( hashCode(word) % this.table.length );
	}
	
	// double hashing function that probes during the hash table operations
	private int hashFunction2(String word)
	{
		// it is equal to the modulus of the string hash code and a given prime number
		return ( this.prime2 - ( hashCode(word) % this.prime2 ) );
	}
	
	
	// Gets the prime number just after the given integer
	// if the integer is already a prime number it will return it
	// it wont get a bigger number if not necessary to save allocated memory space
	private int getNextPrimeNumber(int integer)
	{
		// if number is already prime, it will not run the for loop further more
		while ( !isPrime(integer) )
		{
			// increments the integer
			integer++;
		}

		return integer;
	}
	
	// Checks is number is prime
	private boolean isPrime(int integer)
	{
		// initial divisor
		int i = 2;
		
		while( i < integer)
		{
			// if a number is divisable by any number other than itself or 1
			// the number is a prime number
			if( integer % i == 0) return false;
			
			// incrementing the divisor variable
			i++;
		}
		
		// the integer is a prime number
		return true;
	}

	
}
