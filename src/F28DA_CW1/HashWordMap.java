package F28DA_CW1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

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
	private int probesCounter;
	
	// operations counter
	private int operationsCounter;
	
	// Constructor overloading
	public HashWordMap()
	{
		// Assigning default float value of 0.5
		this( 0.5f );
	}
	
	// Main Constructor 
	public HashWordMap(float f)
	{
		// set operations and probes counters to 0
		this.probesCounter = 0;
		this.operationsCounter = 0;
		
		
		// initial table length
		int initialTableLength = 13;
		
		// Sets max load factor according the value given
		this.maxLoadFactor = f;
		
		// Sets the initial word entry array to 13
		this.table = new WordEntry[initialTableLength];
		
		// Sets the initial number of word entry counter
		this.wordEntryCounter = 0;
		
		// Sets the prime2 integer variable necessary for double hashing function
		this.prime2 = this.getPreviousPrimeNumber(initialTableLength);
	}

	@Override
	public void addPos(String word, IPosition pos)
	{
		// gets word entry index
		int index = this.getWordEntryIndex(word);
		
		if( index == -1)
		{
			// new word entry object
			WordEntry wordEntry = new WordEntry(word, pos);
			
			// if no word entry with this key was found
			// add a new word entry, along with its first position
			this.addWordEntry(wordEntry);
			
			
		} else
		{
			// if word entry was found, add a position to its value
			this.table[index].addPosition(pos);

			// increment number operations
			this.operationsCounter++;
		}

	}
	
	
	@Override
	public void removeWord(String word) throws WordException
	{
		// gets word entry index
		int index = this.getWordEntryIndex(word);
		
		// if not found
		if( index == -1 )
		{
			// throw word exception saying that no word entry was found
			throw new WordException("No word entry found");
			
		} else {
			// Empty the index table meant to remove word entry
			this.table[index] = null;
			
			// decrease word counter
			this.wordEntryCounter--;

			// increment number operations
			this.operationsCounter++;
		}
	}

	@Override
	public void removePos(String word, IPosition pos) throws WordException 
	{
		// gets word entry index
		int index = this.getWordEntryIndex(word);
		
		// if no word entry was found
		if(index == -1) {
			// throw a word exception saying there is no word entry with this key
			throw new WordException("No word entry found");
			
		} else 
		{
			// it removes the position from the entry
			this.table[index].removePosition(pos);
			
			// if there is no positions left in the IPosition array list
			if( this.table[index].getValue().isEmpty() )
			{
				// remove the word entry entirely
				this.table[index] = null;;
				
				// decrease word entry counter
				this.wordEntryCounter--;
			}
			

			// increment number operations
			this.operationsCounter++;
		}
		
	}
	
	// adds a word entry to the hash table
	private void addWordEntry(WordEntry wordEntry)
	{
		// will catch any error resultant from adding a new word entry
		try {

			// if hash table over load factor, resize table
			if( this.isOverLoadFactor() )
			{
				// resize table with same elements, before adding the current position
				this.resizeTable();
			}
			
			// stores the initial index to improve performance
			int initialIndex = this.hashFunction(wordEntry.getKey());


			// it initialises the index variable to keep the current table index
			int index = initialIndex;
			
			// loop counter, it is necessary to check if all indexes were searched
			int counter = 0;
			
			while (true)
			{
				
				// if the table index is null
				boolean isIndexNull = this.table[index] == null;
				
				// if the table index entry is the same as the given word entry
				boolean isSameEntry = false;
				
				// if all indexes were searched
				boolean hasProbedAllIndexes = (initialIndex == index) && (counter != 0);
				
				// increment loop counter by one
				counter++;
				
				// break the loop if the table was completely probed
				if(hasProbedAllIndexes)
				{
					// stop the loop
					break;
				}
				
				// if the index has a entry
				if(!isIndexNull)
				{
					// check if the given entry is the same as the index entry
					isSameEntry = this.table[index].getKey().equals(wordEntry.getKey());
				}
				
				// it should probe if the index is not null and it is not the same entry
				boolean shouldProbe = !isIndexNull && !isSameEntry ;
				
				
				// if table array is not available or it is not an entry already
				if( shouldProbe )
				{
					
					// if the word entry was not found increment using double hash
					index = index + this.hashFunction2(wordEntry.getKey());

					// modulus of index to keep the index within the the bounds
					index = index % this.table.length;
					
					// increment number of probes
					this.probesCounter++;
					
				} else if( isSameEntry )
				{
					// in this case add the missing word positions values in the existent index entry
					
					// store the given entry array list of positions
					ArrayList<IPosition> posArraylist = wordEntry.getValue();
					
					// add the missing positions to this entry
					this.table[index].addPosition(posArraylist);
					
					// stop the while loop after adding the positions
					break;
					
				} else {
					
					
					// word entry is added to the table
					this.table[index] = wordEntry;
					
					// word entry counter is incremented by one 
					this.wordEntryCounter++;
					
					
					// while loop execution is stopped
					break;
					
				}
				
			}
			
		} catch (WordException e)
		{
			// catches the error and stops the adding word entry process
			System.err.println(e);
		}
		
	}
	
	
	// gets the word entry index from hash table
	private int getWordEntryIndex(String word)
	{
		// stores the initial index value to improve performance
		int initialIndex = this.hashFunction(word);
		
		

		// modulus of hash code by table length
		// to keep the index within the the table array bounds
		int index = initialIndex;
		
		
		if( ( this.table[index] != null ) && ( this.table[index].getKey().equals(word) ) )
		{
			return index;
		}

		// word entry was not found
		boolean wasWordEntryFound = false;

		while ( !wasWordEntryFound )
		{
			
			
			// if the word entry was not found increment using double hash
			index = index + this.hashFunction2(word);

			// modulus of index to keep the index within the the table array bounds
			index = index % this.table.length;

			
			
			// if index table is not empty and the key matches with the word
			if( (this.table[index] != null) && ( this.table[index].getKey().equals(word) ))
			{
				// then the word entry was found
				wasWordEntryFound = true;
			}
			
			// if value of index is equal to the initial index value
			// it means that all elements were searched
			if( index == initialIndex )
			{
				
				
				// return -1 to inform that there is no entry in the array
				// but more importantly, to avoid a infinite loop
				return -1;
			}
			
		}
		
		return index;
	}

	@Override
	public Iterator<String> words()
	{
		// creates a new string linked list
		LinkedList<String> tempWordList = new LinkedList<String>();
		
		// loops through the entire hash table
		for(int k = 0; k < this.table.length; k++)
		{
			// if table index holds a value
			if(this.table[k] != null)
			{
				// stores the key string in the string linked list
				tempWordList.add(this.table[k].getKey());
			}
		}
		
		// it returns a string iterator
		return tempWordList.iterator();
		
	}

	@Override
	public Iterator<IPosition> positions(String word) throws WordException 
	{
		// gets the index matching the key
		int index = this.getWordEntryIndex(word);
		
		// if not found
		if( index == -1 )
		{
			// throw word exception saying that no word entry was found
			throw new WordException("No word entry found");
			
		} else {
			// if index is valid
			// store the entry
			WordEntry wordEntry = this.table[index];
			
			// store the IPosition array list values
			ArrayList<IPosition> arrListPos = wordEntry.getValue();
			
			// if list of arrays is empty
			if ( arrListPos.size() == 0 )
			{
				// removes the word
				this.removeWord(word);
				
				// throws error saying that there is no position in this word entry
				throw new WordException("No word positions found");
			}
			
			// return an iterator
			return arrListPos.iterator();
		}
	}

	@Override
	public int numberOfEntries()
	{
		// return the word entries counter
		return this.wordEntryCounter;
	}

	@Override
	public float averNumProbes()
	{
		// casting the probes count to float and store it in a temporary variable
		float tempProbes = (float) this.probesCounter;
		float tempOperations = (float) this.operationsCounter;
		return ( tempProbes / tempOperations );
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
		float tableLength = (float) this.table.length;
		float numberEntries = (float) this.numberOfEntries();
		return ( numberEntries / tableLength );
	}

	
	
	
	// It returns the polynomial accumulation to determine the string hash code
	@Override
	public int hashCode(String s)
	{
		// Prime number to hash the string
		long hashPrimeNumber = 31;
		
		// this long integer accumulates the polynomial of each string character
		long polynomialAccumulator = 0;
		
		// gets the long integer of the string length
		long stringLength = (long) s.length();
		
		
		// looping through each
		for(long i = 0; i < stringLength ; i++)
		{
			// Getting character at position
			char c = s.charAt( (int) i);
			
			
			
			// gets the ASCII code for character, and casting it to long integer
			long asciiCodeCharacter = (long) c;
			
			// prime number's power
			// needs to be the "inverse" of the current i
			long power = stringLength - i - 1;
			
			// prime number's power necessary for character's polynomial
			long primeNumberPower = this.powerOfNumber(hashPrimeNumber,  power);
			
			// each character's polynomial
			long polynomial = asciiCodeCharacter * primeNumberPower;

			// adding each character accumulator to the accumulator

			polynomialAccumulator += polynomial;
			
		}
		
		// it makes sure the number is always positive
		polynomialAccumulator = this.ifNegativeReturnModuleLong(polynomialAccumulator);
		
		// it stores the max integer value in a long integer variable
		long maxIntLong = (long) java.lang.Integer.MAX_VALUE;
		
		// it returns the result of the polynomial accumulator module of the absolute max integer
		long finalResultLong = polynomialAccumulator % maxIntLong; 
		
		// it casts the result polynomial accumulator
		int finalResult = (int) finalResultLong;
		
		// returning the final integer value
		return finalResult;
	}
	
	
	
	// it returns a long integer of a number to the power of n
	private long powerOfNumber(long number, long power)
	{
		// initiates the accumulator
		long acumulator = 1;
		
		// it loops and powers the number to the power
		for(long i = 0; i < power; i++)
		{
			// if the power is zero it returns the number itself
			if( i == 0)
			{
				acumulator = 1 * number;
			}
			else
			{
				// it continuously muliplies the number
				acumulator *= number;
			}
			
		}
		
		// returns the numbers power
		return acumulator;
	}
	
	
	public long ifNegativeReturnModuleLong(long longNumber)
	{
		// the java language does not support unsigned long integers
		//so the result of this function might be negative  long integers
		
		// if the long integers is over the integer absolute maximum value
		// it will loop from the absolute minimum value to the absolute maximum value
		
		// The implementation of this table is array based so negative indexes are entirely forbidden
		
		// To bypass this issue it is necessary to do the module of the negative long integers by
		// java's long integers absolute minimum since it is exactly half of the java long integers data type and
		// the integer absolute positive value is smaller by one, since it includes zero
		
				
				// if the long number is negative
				if ( longNumber < 0 )
				{
					// subtracting the polynomial negative value by the integer absolute minimum
					longNumber = Long.MIN_VALUE % longNumber;
					
					// change the signal
					longNumber = -longNumber;
				}
				
				// returns always a positive long integer
				return longNumber;
	}
	
	
	
	// compression function, which compresses entries in the hash table
	private int hashFunction(String word)
	{		
		// it is equal to the modulus of the string hash code and the table length
		return ( this.hashCode(word) % this.table.length );
	}
	
	// double hashing function that probes during the hash table operations
	private int hashFunction2(String word)
	{
		// it is equal to the modulus of the string hash code and a given prime number
		return ( this.prime2 - ( hashCode(word) % this.prime2 ) );
	}
	
	
	// returns the prime number just after the given integer or returns the integer itself if it is already a prime number
	// it wont get a bigger number if not necessary by checking the first integer, to save allocated memory space
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
	
	// Gets the previous prime number
	// it does not check if the first number is a prime number
	private int getPreviousPrimeNumber(int integer)
	{
		do {
			// decrements the integer, while is is not prime
			integer--;
			
		} while ( !isPrime(integer) );
		
		return integer;
	}
	
	// Checks is number is prime
	private boolean isPrime(int integer)
	{
		// initial divisor
		int i = 2;
		
		while( i < integer)
		{
			// if a number is divisible by any number other than itself or 1
			// the number is a prime number
			if( integer % i == 0) return false;
			
			// incrementing the divisor variable
			i++;
		}
		
		// the integer is a prime number
		return true;
	}
	
	private boolean isOverLoadFactor()
	{
		// checks if the load factor is above the maximum specified in the constructor
		return ( this.maxLoadFactor <= this.getLoadFactor() );
	}
	
	private boolean isEmpty()
	{
		// it checks if table is empty
		return ( this.numberOfEntries() == 0);
	}
	
	
	// resizing function when the load factor goes over the maximum load factor
	private void resizeTable() throws WordException
	{
		// temporary linked list to store all the entries, temporarily
		LinkedList<WordEntry> tempList = new LinkedList<WordEntry>();
		
		for(int i = 0; i < this.table.length; i++)
		{
			if(this.table[i] != null)
			{
				// add to the temporary list
				tempList.add(this.table[i]);
				
				// remove word from hash table
				this.removeWordEntry(i);
			}
		}
		
		// if current table is not empty, throw an error
		if( !this.isEmpty() )
		{
			throw new WordException("Fatal: Hash table map could not resize properly");
		}

		// gets the the double the size of the current hash table
		int doubleCurrentTableLength = this.table.length * 2;
		
		// it determines the new hash table length which needs to be a prime number
		// if the number is already prime it will keep it otherwise it will use the next prime
		int newTableLength = this.getNextPrimeNumber(doubleCurrentTableLength);
		
		// it sets the new double hashing prime number
		// it will return the next smaller prime number to the double the current table size
		this.prime2 = this.getPreviousPrimeNumber(doubleCurrentTableLength);
		
		
		// create a new word index array
		this.table = new WordEntry[newTableLength];
		
		// add all existing word entries to the new resized table
		while( !tempList.isEmpty() )
		{
			// removes each word entry
			WordEntry wordEntry = tempList.remove();
			
			
			// only add word entries that hold values
			if( !wordEntry.getValue().isEmpty() )
			{
				
				// add to new hash table
				this.addWordEntry(wordEntry);
			}
		}

	}
	
	private void removeWordEntry(int index)
	{
		// this removes a word entry from the array
		this.table[index] = null;
		
		// it decreases the word counter
		this.wordEntryCounter--;
		
	}
	
	
}
