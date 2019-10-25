package F28DA_CW1;

import java.util.Iterator;
import java.util.LinkedList;

public class HashWordMap implements IWordMap, IHashMonitor 
{
	
	// DELETE METHOD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public void printWholeTable() {
		for(int i = 0; i< this.table.length; i++)
		{
			if( this.table[i] != null)
			{
				System.out.println("i: " + i + ", key: " + this.table[i].getKey() + ", positions: " + this.table[i].getPositionIterator());
			}
			else 
			{
				System.out.println("i: " + i + " null");
			}
			
			this.isEmptyDelete();
		}
	}
	
	// DELETE METHOD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	private void isEmptyDelete()
	{
		System.out.println("is empty? " + this.isEmpty() );
	}

	// DELETE METHOD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public WordEntry getWordEntry(String s)
	{
		return this.table[this.getWordEntryIndex(s)];
	}
	
	
	
	
	
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
	
	// Constructor overloading
	public HashWordMap()
	{
		// Assigning default float value of 0.5
		this( 0.5f);
	}
	
	// Main Constructor 
	public HashWordMap(float f)
	{
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
		}

	}
	


	@Override
	public void removeWord(String word) throws WordException
	{
		int index = this.getWordEntryIndex(word);
		
		// if not found
		if( index == -1 )
		{
			// trow word exception saying that no word entry was found
			throw new WordException("No word entry found");
			
		} else {
			// Empty the index table meant to remove word entry
			this.table[index] = null;
			
			// decrease word counter
			this.wordEntryCounter--;
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

			// stores this value to improve performance
			int hashCode = this.hashCode(wordEntry.getKey());


			// modulus of hash code by table length
			// to keep the index within the the table array bounds
			int index = hashCode % this.table.length;
			
			// checks if index is available
			boolean wasWordEntryAdded = false;
			
			while ( !wasWordEntryAdded )
			{
				
				// if table array is not available or it is not an entry already
				if( this.table[index] != null )
				{
					// if the word entry was not found increment using double hash
					index = index + this.hashFunction2(wordEntry.getKey());

					// modulus of index to keep the index within the the bounds
					index = index % this.table.length;
					
				} else
				{
					
					
					// word entry is added to the table
					this.table[index] = wordEntry;
					
					// word entry counter is incremented by one 
					this.wordEntryCounter++;
					
					// while loop execution is stopped
					wasWordEntryAdded = true;
				}
				

				
			}
			
		} catch (WordException e)
		{
			// catches the error and stops the adding word entry process
			System.err.println(e);
		}
		
		
		
	}

	
	// overloaded add word entry function
//	private void addWordEntry(WordEntry we) 
//	{
//		// cast value to IPosition array 
//		IPosition[] tempIPositions = (IPosition[]) we.getValue().toArray();
//		
//		// Overloading the function
//		this.addWordEntry(we.getKey(), tempIPositions);
//	}
	
	
	// gets the word entry index from hash table
	private int getWordEntryIndex(String word)
	{
		// stores this value to improve performance
		int hashCode = this.hashCode(word);

		// modulus of hash code by table length
		// to keep the index within the the table array bounds
		int index = hashCode % this.table.length;
		
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
			
			// if value of index is equal to the module of word hash code by the table length
			// it means that all elements were searched
			if( index == ( hashCode % this.table.length ) )
			{
				// return -1 to inform that there is no entry in the array
				// but more importantly, to avoid a infinite loop
				return -1;
			}
			
		}
		
		return index;
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

	@Override
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
		float tableLength = (float) this.table.length;
		float numberEntries = (float) this.numberOfEntries();
		return ( numberEntries / tableLength );
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
			// if a number is divisable by any number other than itself or 1
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
