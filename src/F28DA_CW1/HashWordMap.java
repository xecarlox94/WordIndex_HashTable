package F28DA_CW1;

import java.util.Iterator;

public class HashWordMap implements IWordMap, IHashMonitor {
	
	public HashWordMap(float f) {}

	@Override
	public void addPos(String word, IPosition pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeWord(String word) throws WordException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removePos(String word, IPosition pos) throws WordException {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterator<String> words() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<IPosition> positions(String word) throws WordException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numberOfEntries() {
		// TODO Auto-generated method stub
		return 0;
	}

	public float averNumProbes() {
		// TODO Auto-generated method stub
		return 0f;
	}

	@Override
	public float getMaxLoadFactor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getLoadFactor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int hashCode(String s) {
		// TODO Auto-generated method stub
		return 0;
	}

}
