package F28DA_CW1;

import java.util.ArrayList;

public class WordEntry implements Entry<String, ArrayList<IPosition>> {
	
	// String containing string word
	String word;
	
	// List containing all positions
	ArrayList<IPosition> positions;
	

	@Override
	public String getKey()
	{
		return this.word;
	}

	@Override
	public ArrayList<IPosition> getValue() {
		return this.positions;
	}

}
