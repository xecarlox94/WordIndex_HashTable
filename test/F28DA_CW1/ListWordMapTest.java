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
		// initialises the map
		IWordMap map = new ListWordMap();
		
		// asserts the initial amount of entries
		assertEquals(map.numberOfEntries(), 0);

		
		// initialising the global variables
		String wordKey = "word";
		
		WordPosition pos;
		
		
		// adding multiple positions to word entry
		for(int k = 0; k < 100; k++) 
		{
			int line = 10 + k;
			
			String file = "f" + k + ".txt";
			
			pos = new WordPosition(file, line, wordKey);
			
			map.addPos(wordKey, pos);
		}

		// asserts the current number of entries
		assertEquals(map.numberOfEntries(), 1);
		
		// initialises a IPosition iterator variable
		Iterator<IPosition> positions = null;
		
		try 
		{
			// Initialises word position
			positions = map.positions(wordKey);
			
		} catch (WordException e) 
		{
			// fails if something unexpected happens
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
		
		// assert final number of entries, after removing all the entries
		assertEquals(positionsCounter, 100);
		
	}
	
	
	

	//	testing if it returns the number of items
	//	testing if it removes all word entries
	
	@Test
	public void addingEntriesPositionsAndRemoveThem()
	{
		// initialises the map
		IWordMap map = new ListWordMap();
		
		
		// asserts the initial amount of entries
		assertEquals(map.numberOfEntries(), 0);
		
		// initialising the global variables
		String wordKey;
		int line;
		String file;
		WordPosition pos;
		
		// it adds 100 word entries
		for( int k = 0; k < 100; k++ )
		{
			wordKey = "word" + k;
			
			// it adds 10 positions to each word
			for( int j = 0; j < 10; j++ )
			{
				line = 10 + j;
				file = "file" + j + ".txt";

				pos = new WordPosition(file, line, wordKey);
				
				map.addPos(wordKey, pos);
			}
			
		}


		// asserting number of entries
		assertEquals(map.numberOfEntries(), 100);
		
		// it removes all the words inserted before
		for( int k = 0; k < 100; k++ )
		{
			wordKey = "word" + k;
			
			try
			{
				map.removeWord(wordKey);
				
			} catch (WordException e) 
			{
				// if something unexpected happens, fail
				fail();
			}
			
		}
		
		// assert final number of entries, after removing all the entries
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
