package F28DA_CW1;

import java.util.Comparator;



/**
 * 
 * Comparator to sort word position per file by amount of position, in a descending order
 *
 */


public class ComparatorSortByPositionsAmount implements Comparator<WPositionsFile>
{

	@Override
	public int compare(WPositionsFile wpf1, WPositionsFile wpf2) {
		
		// amount of the first word position per file
		int amount1 = wpf1.getAmountPositions();
		

		// amount of the second word position per file
		int amount2 = wpf2.getAmountPositions();
		
		if( amount1 > amount2 )
		{
			return -1;
		}
		else if ( amount1 < amount2 )
		{
			return 1;
		}
		else {
			return 0;
		}
	}
}
