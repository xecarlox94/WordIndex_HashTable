package F28DA_CW1;

import java.util.ArrayList;
import java.util.Iterator;

public class WordEntry implements Entry<String, ArrayList<IPosition>> {
	
	// String containing string word
	private String word;
	
	// List containing all positions
	private ArrayList<IPosition> positions;
	
	
	// Constructor
	public WordEntry(String word) {
		this.word = word;
		this.positions = new ArrayList<IPosition>();
	}
	
	// Constructor overload
	public WordEntry(String word, IPosition pos)
	{
		// uses the main constructor
		this(word);
		
		// add first position
		this.positions.add(pos);
	}
	
	// Constructor overload
	public WordEntry(String word, IPosition[] posArray)
	{
		// uses the main constructor
		this(word);
		
		// loops through the IPosition given array
		for(int i = 0; i < posArray.length; i++)
		{
			// it adds each positions to the array list
			this.addPosition(posArray[i]);
		}
	}

	@Override
	public String getKey()
	{
		return this.word;
	}

	@Override
	public ArrayList<IPosition> getValue() {
		return this.positions;
	}
	
	// adds a word position
	public void addPosition(IPosition pos)
	{
		// checks if position was found
		boolean posFound = false;
		
		// loops through the array list
		for (int i = 0; i < this.positions.size(); i++)
		{
			// Stores the current position in the loop
			IPosition temp = this.positions.get(i);
			
			// checks if any array list element is equal to pos 
			if( temp.equals(pos) )
			{
				// the position was found
				posFound = true;
				
				// finishes loop execution
				break;
			}
		}
		
		// if there is no position, then add the unique position value
		if(!posFound) this.positions.add(pos);
	}
	
	// removes a position from the word entry's positions array list  
	public boolean removePosition(IPosition pos)
	{
		// returns true if the position was found or false if it was not found
		return this.positions.remove(pos);
	}
	
	// returns an iterator composed of word positions
	public Iterator<IPosition> getPositionIterator()
	{
		// return null if the array list is empty
		if(this.positions.isEmpty()) return null;
		
		// return the iterator if the array list has elements
		return this.positions.iterator();
	}
	
	// returns the amount of word positions in a word entry
	public int getAmountWordPositions()
	{
		return this.positions.size();
	}
	
	// returns if the positions array list is empty
	public boolean isWordPositionsEmpty()
	{
		return this.positions.isEmpty();
	}

}
