package F28DA_CW1;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class ListWordMapTest {
	

	//	testing if it creates a new word entry with many positions
	//	testing if it returns the number of items
	//	testing if it removes multiple word positions
	//	testing if it adds a new word position to a existent word entry
	
	@Test
	public void addingPositionsToEntryAndRemovePositionsAndEntry() 
	{
		
		IWordMap map = new ListWordMap();
		
		assertEquals(map.numberOfEntries(), 0);
		
		String wordKey = "word";
		
		WordPosition pos;
		
		for(int k = 0; k < 100; k++) 
		{
			int line = 10 + k;
			
			String file = "f" + k + ".txt";
			
			pos = new WordPosition(file, line, wordKey);
			
			map.addPos(wordKey, pos);
		}

		assertEquals(map.numberOfEntries(), 1);
		
		Iterator<IPosition> positions = null;
		
		try 
		{
			positions = map.positions(wordKey);
			
		} catch (WordException e) 
		{
			fail();
		}
		
		
		int positionsCounter = 0;
		

		for(int k = 0; k < 100; k++) 
		{
			
			while ( positions.hasNext() ) 
			{
				positionsCounter++;

				positions.next();

			}
			
		}
		

		assertEquals(positionsCounter, 100);
		
	}
	
	
	

	//	testing if it returns the number of items
	//	testing if it removes all word entries
	
	@Test
	public void addingEntriesPositionsAndRemoveThem()
	{
		IWordMap map = new ListWordMap();
		
		assertEquals(map.numberOfEntries(), 0);
		
		String wordKey;
		int line;
		String file;
		WordPosition pos;
		
		for( int k = 0; k < 100; k++ )
		{
			wordKey = "word" + k;
			
			for( int j = 0; j < 10; j++ )
			{
				line = 10 + j;
				file = "file" + j + ".txt";

				pos = new WordPosition(file, line, wordKey);
				
				map.addPos(wordKey, pos);
			}
			
		}


		assertEquals(map.numberOfEntries(), 100);
		
		for( int k = 0; k < 100; k++ )
		{
			wordKey = "word" + k;
			
			try
			{
				map.removeWord(wordKey);
				
			} catch (WordException e) 
			{
				fail();
			}
			
		}
		
		assertEquals(map.numberOfEntries(), 0);
	}

	
	// ...

	@Test
	public void signatureTest() {
        try {
            IWordMap map = new ListWordMap();
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
