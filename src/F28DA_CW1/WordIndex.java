package F28DA_CW1;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

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
			// Linked List implementation of the word map
			wordPossMap = new ListWordMap();
			
			

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
					break;

				case "add":
					File textFile = new File(textFilesFolder, commandReader.nextWord().getWord()+".txt");
					WordTxtReader wordReader = new WordTxtReader(textFile);
					while (wordReader.hasNextWord()) {
						WordPosition word = wordReader.nextWord();
						
						
						// adding word to the map
						// casting word position variable into IPosition
						IPosition pos = (IPosition) word;
						
						// adding word position to word position map
						wordPossMap.addPos(word.getWord(), pos);
						
						
					}
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
						int i = 0;
						while(poss.hasNext()) {
							
							// increments the number of existing word positions
							i++;
							
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
									

									System.out.println(tempPosition.getFileName());
									
									// breaks the inner for loop once the position was added to file
									break;
								}
							}
							
						}
						
						// function that prints the results of the search, by taking the word positions per file and its amount
						printSearchResults(wordPositionsFile, i, nb);
						
					} catch (WordException e) {
						System.err.println("not found");
					}
					// ...
					break;

				case "remove":
					
					// loads the file to be removed
					File textFileToRemove = new File(textFilesFolder, commandReader.nextWord().getWord().toUpperCase()+".txt");
					
					// Initialises the file reader
					WordTxtReader wrdReader = new WordTxtReader(textFileToRemove);
					
					// Initialises a removed words counter
					int removedWordCounter = 0;

					// reads the whole text file by looping under the condition of having a next word
					while (wrdReader.hasNextWord()) {
						
						// Stores the word in a temporary variable
						WordPosition tempWordPos = wrdReader.nextWord();
						
						// casting word position variable into IPosition
						IPosition pos = (IPosition) tempWordPos;
						
						// catch any error during the position removal
						try {
							
							// removing word from word map
							wordPossMap.removePos(tempWordPos.getWord(), pos);
							
							
							
						} catch (WordException e) {

							
							// increments removed word counter
							removedWordCounter++;
						}
						
						
					}
					
					System.out.println(removedWordCounter + " word positions removed");

					break;

				case "overview":
					// print overview
					// ...
					// Question
					~
					
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
	private static void printSearchResults(WPositionsFile[] wordPositionsFile, int amountPositions, int numberFilesToPrint)
	{
		System.out.println("Before sorting");
		for(int i = 0; i < wordPositionsFile.length; i++) {
			System.out.println( "File: " + wordPositionsFile[i].getFileName() + ",		word entries: " + wordPositionsFile[i].getAmountPositions() );
		}
		Arrays.sort(wordPositionsFile, new ComparatorSortByPositionsAmount());
		

		System.out.println("After sorting");
		for(int i = 0; i < wordPositionsFile.length; i++) {
			System.out.println( "File: " + wordPositionsFile[i].getFileName() + ",		word entries: " + wordPositionsFile[i].getAmountPositions() );
		}
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
