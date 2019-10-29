package F28DA_CW1;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

/** Main class for the Word Index program */
public class WordIndex {

	static final File textFilesFolder = new File("TextFiles");
	static final FileFilter commandFileFilter = (File file) -> file.getParent()==null;
	static final FilenameFilter txtFilenameFilter = (File dir, String filename) -> filename.endsWith(".txt");

	public static void main(String[] argv) {
		if (argv.length != 1 ) {
			System.err.println("Usage: WordIndex commands.txt");
			System.exit(1);
		}
		try{
			File commandFile = new File(argv[0]);
			if (commandFile.getParent()!=null) {
				System.err.println("Use a command file in current directory");
				System.exit(1);
			}

			// creating a command reader from a file
			WordTxtReader commandReader = new WordTxtReader(commandFile);
			

			// initialise map
			IWordMap wordPossMap;
			
			
			// Linked List implementation of a word map
			wordPossMap = new ListWordMap();
			
			// hash table implementation of a word map
			//wordPossMap = new HashWordMap();
			
			

			// reading the content of the command file
			while(commandReader.hasNextWord()) {
				// getting the next command
				String command = commandReader.nextWord().getWord();

				switch (command) {
				case "addall":
					assert(textFilesFolder.isDirectory());
					File[] listOfFiles = textFilesFolder.listFiles(txtFilenameFilter);
					Arrays.sort(listOfFiles);
					for (File textFile : listOfFiles) {
						WordTxtReader wordReader = new WordTxtReader(textFile);

						while (wordReader.hasNextWord()) {
							WordPosition wordPos = wordReader.nextWord();
							
							
							// adding word to the map
							// casting word position variable into IPosition
							IPosition pos = (IPosition) wordPos;
							
							// adding word position to word position map
							wordPossMap.addPos(wordPos.getWord(), pos);
							
							
						}
					}
					
					// Iterator holding word keys
					Iterator<String> wordEntryKeys = wordPossMap.words();
					
					// list holding unique file names
					LinkedList<String> filesNames = new LinkedList<String>();
					
					int wordEntryCounter = 0;
					
					// iterates through the Iterator
					while( wordEntryKeys.hasNext() )
					{
						// increments the word entry counter
						wordEntryCounter++;
						
						// stores the string 
						String word = wordEntryKeys.next();

						try
						{
							// gets the positions iterator for each word entry
							Iterator<IPosition> positions = wordPossMap.positions(word);
							
							// while there is still positions in this iterator, iterate
							while( positions.hasNext() )
							{
								// removes a position from the iterator
								// and stores it in a variable
								IPosition position = positions.next();
								

								// stores the word position's file name
								String posfname = position.getFileName();
								
								
								boolean isPositionFileNameSaved = false;
								
								// iterates through the files names linked list
								// to add new unique file names to it
								for(int k = 0; k < filesNames.size(); k++)
								{
									// stores the current linked list filename string
									String fname = filesNames.get(k);
									
									
									// if the current linked list file name is equal
									// to the 
									if ( fname.equals(posfname) )
									{
										// file name is already included in the linked list
										isPositionFileNameSaved = true;
										
										// stops the for loop
										break;
									}
									
								}
								
								// if the file name is not included
								if( !isPositionFileNameSaved )
								{
									// add file name to files names linked list
									filesNames.add(posfname);
								}
								
							}
							
							
							
						} catch (WordException e) {
							// prints the error message
							// it prevents the execution of an empty positions iterator
							System.err.println( word + " word entry has no positions.");
							
							// decreases the word counter by one, if word has no positions
							wordEntryCounter--;
						}
						
					}
					
					
					System.out.println(wordEntryCounter + " entries have been indexed from " + filesNames.size() + " files");
					
					break;

				case "add":
					
					String tempFileName = commandReader.nextWord().getWord()+".txt";
					File textFile = new File(textFilesFolder, tempFileName );
					WordTxtReader wordReader = new WordTxtReader(textFile);
					
					// word entries added
					int wordEntriesAddedCounter = 0;
					
					while (wordReader.hasNextWord()) {
						WordPosition wordPosition = wordReader.nextWord();
						
						
						// adding word to the map
						// casting word position variable into IPosition
						IPosition pos = (IPosition) wordPosition;
						
						try 
						{
							// gets word position from a word entry
							wordPossMap.positions(wordPosition.getWord());
							
						} catch (WordException e) 
						{
							// it catches word exception reporting 
							// that there is no word entry with this string and increases the counter
							wordEntriesAddedCounter++;
						}
						
						
						// adding word position to word position map
						wordPossMap.addPos(wordPosition.getWord(), pos);
					}
					
					System.out.println( wordEntriesAddedCounter + " entries have been indexed from file \"" + tempFileName + "\"");
					
					break;

				case "search":
					
					// Initialises new word position per file (Helper Function) to hold a specific position per each file
					WPositionsFile[] wordPositionsFile = getWPositionFileArray();
					
					int nb = Integer.parseInt(commandReader.nextWord().getWord());
					String word = commandReader.nextWord().getWord();

					
					// search for word entry in map
					// ...
					try {
						Iterator<IPosition> poss = wordPossMap.positions(word);

						while(poss.hasNext()) {
							
							// stores a temporary word position variable
							IPosition tempPosition = poss.next();
							

							
							// loops through the word position per file array
							for(int k = 0; k < wordPositionsFile.length; k++)
							{
								// wposition file name lower case string
								String wordPosFilename = wordPositionsFile[k].getFileName().toLowerCase();
								


								
								// tests if the word position file name is equal to the position file name
								if( wordPosFilename.equals(tempPosition.getFileName().toLowerCase()) )
								{
									
									// adds the current position to the respective file
									wordPositionsFile[k].addPosition(tempPosition);
									
									// breaks the inner for loop once the position was added to file
									break;
								}
							}
							
						}
						
						// function that prints the results of the search, by taking the word positions per file and its amount
						printSearchResults(wordPositionsFile, word, nb);
						
					} catch (WordException e) {
						System.err.println(e);
					}
					// ...
					break;

				case "remove":
					
					String fileRemovedName = commandReader.nextWord().getWord().toUpperCase()+".txt";
					
					// loads the file to be removed
					File textFileToRemove = new File(textFilesFolder, fileRemovedName);
					
					// Initialises the file reader
					WordTxtReader wrdReader = new WordTxtReader(textFileToRemove);
					
					// Initialises a removed words counter
					int removedWordCounter = 0;

					// reads the whole text file by looping under the condition of having a next word
					while ( wrdReader.hasNextWord() ) 
					{
						
						// Stores the word in a temporary variable
						WordPosition tempWordPos = wrdReader.nextWord();
						
						// casting word position variable into IPosition
						IPosition pos = (IPosition) tempWordPos;


						try {
							
							// removing position from word entry
							wordPossMap.removePos(tempWordPos.getWord(), pos);
							
							
							
						} catch (WordException e) 
						{							
							// the removing position throws an error if the word entry does no exist
							
							// increments removed word counter as the word has been removed
							removedWordCounter++;
						}
						
						
					}
					
					// final report message
					System.out.println(removedWordCounter + " word entries were removed from file \"" + fileRemovedName.toLowerCase() + "\"");

					break;

				case "overview":
					
					// storing uniques
					LinkedList<String> filenames = new LinkedList<String>();
					// storing all word entry keys in a temporary string iterator
					Iterator<String> wordKeys = wordPossMap.words();
					
					
					// word entry counter
					int wordsCounter = 0;
					// positions counter
					int positionsCounter = 0;
					
					
					// looping through the iterator
					while ( wordKeys.hasNext() )
					{
						// storing the word entry key string
						String wordEntrykey = wordKeys.next();
						
						try 
						{
							// storing positions iterator per each word entry
							Iterator<IPosition> positions = wordPossMap.positions(wordEntrykey);
							

							// incrementing the counter variable, if does not throw an error
							wordsCounter++;
							
							// looping through the positions iterator
							while ( positions.hasNext() ) 
							{
								// storing the next position in a temporary 
								IPosition pos = positions.next();
								
								// position filename string
								String posFileName = pos.getFileName();
								
								// incrementing the positions counter
								positionsCounter++;
								
								// checks if file name string is in the file name string linked list 
								boolean isFileNameInserted = false;
								
								// looping through the filenames string linked list
								for( int k = 0; k < filenames.size(); k++ )
								{
									// this filesname index string is equal to the position filename
									if ( filenames.get(k).equals(posFileName) )
									{
										// if string is already in, change inserted boolean to true
										isFileNameInserted = true;
									}
								}
								
								// if the string is not in the linked list
								if ( !isFileNameInserted )
								{
									// add the string to the linked list
									filenames.add(posFileName);
								}
								
							}
							
						} catch (WordException e)
						{
							// do nothing
						}
						
					}
					
					
					System.out.println("Overview:\r\n" + 
							"  number of words: " + wordsCounter + "\r\n" + 
							"  number of positions: " + positionsCounter + "\r\n" + 
							"  number of files: " + filenames.size() );
					
					// Question
					
					//	Overview:
						//	number of words: 1304
						//	number of positions: 954
						//	number of files: 20
						
					
					break;

				default:
					break;
				}

			}

		}
		catch (IOException e){ // catch exceptions caused by file input/output errors
			System.err.println(e);
			System.err.println("Check your file name");
			System.exit(1);
		}  
	}
	
	
	// gets the word positions per file, sorts it and prints the amount of files specified and the word positions lines
	private static void printSearchResults(WPositionsFile[] wordPositionsFile, String word, int numberFilesToPrint)
	{
		// sorts the array using the a comparator, sorting the positions descending by the amount of positions
		Arrays.sort(wordPositionsFile, new ComparatorSortByPositionsAmount());

		// stores the string being used by the for loop
		String forLoopString = "";
		
		// it counts how many files contain positions 
		int filesWithPositionsCounter = 0;
		
		// it counts the amount of positions in total
		int amountPositions = 0;
		
		// loops through the array of Word Position file objects
		for( int k = 0; k < wordPositionsFile.length; k++)
		{
			// checks if file has positions
			boolean doesFileHavePositions = wordPositionsFile[k].getAmountPositions() > 0;
			
			amountPositions += wordPositionsFile[k].getAmountPositions();
			
			// if file has positions
			if( doesFileHavePositions ) 
			{
				// increase files with positions counter
				filesWithPositionsCounter++;
			}
			
			// it will print the files that have the most positions, within the range given by numberFilesToPrint
			if( ( k < numberFilesToPrint ) && doesFileHavePositions )
			{
				if( wordPositionsFile[k].getAmountPositions() == 0 ) 
				{
					// prints this message informing that there is no no positions in this file
					System.out.println("  No positions in file " + wordPositionsFile[k].getFileName() );
					
					// it continues to the next loop directly
					continue;
				}
				
				// builds the first line to be printed 
				forLoopString += "  " + wordPositionsFile[k].getAmountPositions() + " in " 
										+ wordPositionsFile[k].getFileName() + "\n   ( lines ";

				// stores the positions in this array
				ArrayList<IPosition> tempArrPositions = wordPositionsFile[k].getPositions();
				
				// it loops through this positions array
				for( int j = 0; j < tempArrPositions.size(); j++ )
				{
					// adding commas to string
					if ( j > 0 ) forLoopString += ", ";
					
					// lines of each position to the string
					forLoopString += tempArrPositions.get(j).getLine();
				}
				
				// adds last parenthesis and new line character
				forLoopString += " ) \n";
			}
		}
		
		// prints the overview message
		String finalReportMessage = "The word \"" + word + "\" occurs " + amountPositions 
									+ " times in " + filesWithPositionsCounter + " files:\n";
		
		// concatenates the overview message with the for loop message
		finalReportMessage += forLoopString;
		
		// prints the final report
		System.out.println(finalReportMessage);
	}


	// returns an word positions per file array
	private static WPositionsFile[] getWPositionFileArray()
	{
		// gets a file list inside the directory
		File[] filesList = textFilesFolder.listFiles(txtFilenameFilter);
		
		// Initialises a word 
		WPositionsFile[] temp = new WPositionsFile[filesList.length];
		
		//loops through the temp array
		for(int j = 0; j < temp.length; j++)
		{
			// Stores filename string
			String sTemp = filesList[j].getName();
			
			// Initialises a new word position file
			temp[j] = new WPositionsFile(sTemp);
			
		}
		
		// returns a new word position array
		return temp;
	}
	
	
	
}
