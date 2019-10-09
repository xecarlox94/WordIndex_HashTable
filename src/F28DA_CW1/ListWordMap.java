package F28DA_CW1;

import java.util.Iterator;
import java.util.LinkedList;

public class ListWordMap implements IWordMap
{
	// Java linked list which will store the word position data
	private LinkedList<WordEntry> list = new LinkedList<WordEntry>();

	
	@Override
	public void addPos(String word, IPosition pos)
	{
		// Instantiates a new word entry
		WordPosition wp = new WordPosition(pos.getFileName(), pos.getLine(), word);

		// adds a new element to the linked list
		list.add(wp);
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
			String tempWord = list.get(i).getWord();
			
			// compares the temporary string to test if it matches the given word string
			if(tempWord.equals(word))
			{
				// removes the index element matching the condition
				list.remove(i);
				wasWordRemoved = true;
			}
		}
		
		// if no word was removed, then the word exception error is thrown stating that no entry was removed
		if(!wasWordRemoved) throw new WordException("No word position entry was removed");
	}

	@Override
	public void removePos(String word, IPosition pos) throws WordException
	{
		// checks if the word position entry was removed
		boolean wasWordRemoved;
		
		// Instantiates a new word position with the data given
		WordPosition wp = new WordPosition(pos.getFileName(), pos.getLine(), word);
		
		// removes the word position entry from the linked list and returns true
		// if the linked list did not contain the entry it will return false
		wasWordRemoved = list.remove(wp);
		
		// if no entry was removed, then throw the word exception stating that no word position entry was removed
		if(!wasWordRemoved) throw new WordException("No word position entry was removed");
	}

	@Override
	public Iterator<String> words()
	{
		// Instantiates a temporary linked list to contain words
		LinkedList<String> tempWordList = new LinkedList<String>();
		
		// loops through the linked list
		for(int i = 0; i < list.size(); i++)
		{
			// stores the word string of each word position linked list 
			String tempString = list.get(i).getWord();
			
			// tests if the temporary string word linked list already contains the temporary string
			if(!tempWordList.contains(tempString))
			{
				// adding the string if the temporary words list does not contain the temporary string yet
				tempWordList.add(tempString);
			}
		}
		// it returns an Iterator containing all the different words, not repeated
		return tempWordList.iterator();
	}

	@Override
	public Iterator<IPosition> positions(String word) throws WordException
	{
		// Instantiates a temporary position linked list containing word positions
		LinkedList<IPosition> tempPositionList = new LinkedList<IPosition>();
		
		// loops through the linked list
		for(int i = 0; i < list.size(); i++)
		{
			// store current list element temporary variable
			// store its string in a variable
			WordPosition tempWord = list.get(i);
			String tempString = tempWord.getWord();
			
			// check if temporary string matches with given word string
			if(tempString.equals(word))
			{
				// adding word to temporary position list
				tempPositionList.add(tempWord);
			}
		}
		
		//returns temporary position iterator containing word positions that match the word
		return tempPositionList.iterator();
	}

	@Override
	public int numberOfEntries()
	{
		// it returns word position linked list size
		return this.list.size();
	}


}
