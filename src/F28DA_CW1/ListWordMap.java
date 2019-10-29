package F28DA_CW1;

import java.util.Iterator;
import java.util.LinkedList;

public class ListWordMap implements IWordMap
{
	// Java linked list which will store Word Entries
	private LinkedList<WordEntry> list = new LinkedList<WordEntry>();

	
	@Override
	public void addPos(String word, IPosition pos)
	{
		// checks if entry was found
		boolean entryFound = false;
		
		// loops through the linked list of entry to find the entry
		for(int i = 0; i < this.list.size(); i++) 
		{
			// stores the word entry key in a string variable, for comparison
			String temp = this.list.get(i).getKey();
			
			// check if the word entry key string is equal to the string given
			if( temp.equals(word) )
			{
				// entry was found
				entryFound = true;
				
				// position was added to the word entry
				this.list.get(i).addPosition(pos);
				
				// finishes loop execution
				break;
			}
		}
		
		// if entry was not found, create a new entry and add it to linked list
		if(!entryFound)
		{
			// creates a temp word entry
			WordEntry temp = new WordEntry(word,pos);
			
			// adds it to the linked list
			this.list.add(temp);
		}
	}

	@Override
	public void removeWord(String word) throws WordException
	{
		// checks if any word was removed from the map
		boolean wasWordRemoved = false;
		
		// loops through the linked list
		for(int i = 0; i < list.size(); i++)
		{
			// Instantiates a temporary string containing an index element's word for comparison
			String tempWord = list.get(i).getKey();
			
			// compares the temporary string to test if it matches the given word string
			if(tempWord.equals(word))
			{
				// removes the index element matching the condition
				list.remove(i);
				wasWordRemoved = true;
				
				// Finishes the loop execution
				break;
			}
		}
		
		// if no word was removed, then the word exception error is thrown stating that no word entry was removed
		if(!wasWordRemoved) throw new WordException("No word entry was found");
	}

	@Override
	public void removePos(String word, IPosition pos) throws WordException
	{
		// looping through the list
		for (int i = 0; i < this.list.size(); i++)
		{
			// Instantiates a temporary string containing an index element's word for comparison
			String tempWord = list.get(i).getKey();
			
			// compares the temporary string to test if it matches the given word string
			if(tempWord.equals(word))
			{
				
				// if word has the positions array list empty
				if(list.get(i).isWordPositionsEmpty())
				{
					
					// remove word from map
					this.removeWord(tempWord);

					// throw word exception saying that no word entry was found
					throw new WordException("No word entry found");
				}
				

				// removes word position from word entry
				list.get(i).removePosition(pos);
				
				// Finishes the loop execution
				break;
			}
		}

	}

	@Override
	public Iterator<String> words()
	{
		// Instantiates a temporary linked list to contain words
		LinkedList<String> tempWordList = new LinkedList<String>();

		// for of loop to get each word entry list element
		for(WordEntry we: this.list)
		{
			// adds each element string to a temporary string
			String temp = we.getKey();
			
			// adds each string to the temporary word string list
			tempWordList.add(temp);
		}
		
		// it returns an Iterator containing all the different words, not repeated
		return tempWordList.iterator();
	}

	@Override
	public Iterator<IPosition> positions(String word) throws WordException
	{
		// gets the word entry matching this key string
		WordEntry we = this.getWordEntry(word);
		
		
		// if no word entry was found then throw word exception
		if( we == null) throw new WordException("No word entry was found");
		
		
		// store the existing positions in a temporary word position iterator
		Iterator<IPosition> tempPosIterator = we.getPositionIterator();
		
		
		// if the iterator is empty, then throw a word exception
		if( tempPosIterator == null) throw new WordException("No word positions found");
		
		
		// if there positions, then return the iterator
		return tempPosIterator;
	}

	@Override
	public int numberOfEntries()
	{
		// it returns word position linked list size
		return this.list.size();
	}

	private WordEntry getWordEntry(String word)
	{
		// loops through the linked list of entry to find the entry
		for(int i = 0; i < this.list.size(); i++) 
		{
			// stores the word entry key in a string variable, for comparison
			String temp = this.list.get(i).getKey();
			
			// check if the word entry key string is equal to the string given
			if( temp.equals(word) )
			{
				// it returns the word entry
				return this.list.get(i);
			}
		}
		return null;
	}

}
