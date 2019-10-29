package F28DA_CW1;

import static org.junit.Assert.*;

import org.junit.Test;

public class HashWordMapTest {

	// Add your own tests, for example to test the method hashCode from HashWordMap
	
	//	- creates a new word entry with many positions
	//	- returns the number of items
	//	- add a new word position to a existent word entry
	//	- add many word entries and remove all of them
	//
	//	- removes word entries
	//	- removes word positions
	//	- removes word entries if all positions are removed
	//
	//	- throws error if looking for positions of a non existent word entry
	//	- throws error if removing a non existing word entry
	//	
	//	- if string hashcode is over is over the integer over the integer.max value, 
	//	returns the module of int.max_value
	
	// ...
	
	@Test
	public void signatureTest() {
        try {
            IWordMap map = new HashWordMap(0.5f);
            String word1 = "test1";
            String word2 = "test2";
            IPosition pos1 = new WordPosition("test.txt", 4, word1);
            IPosition pos2 = new WordPosition("test.txt", 5, word2);      
            map.addPos(word1, pos1);
            map.addPos(word2, pos2);
            map.words();
            map.positions(word1);
            map.numberOfEntries();
            map.removePos(word1, pos1);
            map.removeWord(word2);
        } catch (Exception e) {
            fail("Signature of solution does not conform");
        }
	}

}
