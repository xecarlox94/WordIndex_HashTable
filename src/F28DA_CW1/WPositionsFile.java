package F28DA_CW1;

import java.util.ArrayList;
import java.util.Comparator;


/**
 * 
 * Helper class to hold positions of a single word per each file 
 * 
 */

public class WPositionsFile {
	
	// File's name
	private String fileName;
	
	// Array list IPosition
	private ArrayList<IPosition> wordPositions = new ArrayList<IPosition>();
	
	
	// assigns the name of the file and initializes the word positions array list
	public WPositionsFile(String fileName)
	{
		this.fileName = fileName;
		this.wordPositions = new ArrayList<IPosition>();
	}
	
	
	// adds a word position 
	public void addPosition(IPosition position)
	{
		this.wordPositions.add(position);
	}
	
	// returns the word position array list
	public ArrayList<IPosition> getPositions()
	{
		return this.wordPositions;
	}
	
	// returns the amount of word position in the file
	public int getAmountPositions()
	{
		return this.wordPositions.size();
	}
	
	// returns the file name
	public String getFileName()
	{
		return this.fileName;
	}

}










