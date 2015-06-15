import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


public class Call implements Cloneable{
	TreeSet<Integer> readPositions;
	TreeSet<Integer> writePositions;
	HashSet<Call> RAWDependencies;
	HashSet<Call> WAWDependencies;
	HashSet<Call> WARDependencies;
	HashSet<Call> AllRAWDependencies;
	HashSet<Call> RAWDependents;
	int callNumber;
	String procedureName;
	
	public HashSet<Call> getAllRAWDependencies() {
		return AllRAWDependencies;
	}	
	
	public HashSet<Call> getRAWDependencies() {
		return RAWDependencies;
	}

	public HashSet<Call> getWAWDependencies() {
		return new HashSet<Call>(WAWDependencies);
	}

	public HashSet<Call> getWARDependencies() {
		return new HashSet<Call>(WARDependencies);
	}

	public HashSet<Call> getRAWDependents() {
		return RAWDependents;
	}
	
	public Call clone()
	{
		Call copy = new Call(callNumber, procedureName, readPositions, writePositions);
		copy.setAllRAW(AllRAWDependencies);
		copy.setRAW(RAWDependencies);
		copy.setWAR(WARDependencies);
		copy.setWAW(WAWDependencies);
		copy.setRAWDependents(RAWDependents);
		
		return copy;		
	}
	
	private void initializeDepSets()
	{
		RAWDependencies = new HashSet<Call>();
		AllRAWDependencies = new HashSet<Call>();
		WAWDependencies = new HashSet<Call>();
		WARDependencies = new HashSet<Call>();
		RAWDependents = new HashSet<Call>();
	}
	
	public Call ()
	{
		readPositions = new TreeSet<Integer>();
		writePositions = new TreeSet<Integer>();
		callNumber = -1;
		procedureName = "UNMD";
		initializeDepSets();
	}
	
	public Call (int num)
	{
		readPositions = new TreeSet<Integer>();
		writePositions = new TreeSet<Integer>();
		callNumber = num;
		procedureName = "UNMD";
		initializeDepSets();
	}

	public Call (int num, String name)
	{
		readPositions = new TreeSet<Integer>();
		writePositions = new TreeSet<Integer>();
		callNumber = num;
		procedureName = name;
		initializeDepSets();
	}
	
	public Call (int num, String name, Set<Integer> newReadPos, Set<Integer> newWritePos)
	{
		readPositions = new TreeSet<Integer>(newReadPos);
		writePositions = new TreeSet<Integer>(newWritePos);
		callNumber = num;
		procedureName = name;
		initializeDepSets();
	}
	
	public void setName (String name)
	{
		procedureName = name;
	}
	
	public void addRead(int pos)
	{
		readPositions.add(pos);
	}
	
	public void addWrite(int pos)
	{
		writePositions.add(pos);
	}
	
	public boolean readsFrom(int pos)
	{
		return readPositions.contains(pos);
	}
	
	public boolean writesTo(int pos)
	{
		return writePositions.contains(pos);
	}
	
	public TreeSet<Integer> getWrites()
	{
		return new TreeSet<Integer>(writePositions);
	}
	
	public TreeSet<Integer> getReads()
	{
		return new TreeSet<Integer>(readPositions);
	}
	
	public void addRAW (Call dep)
	{
		RAWDependencies.add(dep);
		AllRAWDependencies.add(dep);
	}

	public void addDerivedRAW (Call dep)
	{
		AllRAWDependencies.add(dep);
	}

	public void addAllDerivedRAW (HashSet<Call> deps)
	{
		AllRAWDependencies.addAll(deps);
	}
	
	public void addWAW (Call dep)
	{
		WAWDependencies.add(dep);
	}
	
	public void addWAR (Call dep)
	{
		WARDependencies.add(dep);
	}

	public void addRAWDependent (Call dependent)
	{
		RAWDependents.add(dependent);
	}
	
	public String toString ()
	{
		return procedureName + "_" + callNumber;
	}
	
	public String getProcedureName ()
	{
		return procedureName;
	}
	
	public int getCallNumber ()
	{
		return callNumber;
	}

	public void removeAllFromRAW(Set<Call> rawDependencies2) {
		RAWDependencies.removeAll(rawDependencies2);
	}

	public void removeAllFromWAW(HashSet<Call> wawDependencies2) {
		WAWDependencies.removeAll(wawDependencies2);		
	}

	public void removeAllFromWAR(HashSet<Call> warDependencies2) {
		WARDependencies.removeAll(warDependencies2);		
	}
	
	public void setRAW(Set<Call> rawDeps)
	{
		RAWDependencies.clear();
		RAWDependencies.addAll(rawDeps);
	}
	
	public void setWAW(Set<Call> wawDeps)
	{
		WAWDependencies.clear();
		WAWDependencies.addAll(wawDeps);		
	}
	
	public void setWAR(Set<Call> warDeps)
	{
		WARDependencies.clear();
		WARDependencies.addAll(warDeps);		
	}
	
	public void setAllRAW(Set<Call> allRAWDeps)
	{
		AllRAWDependencies.clear();
		AllRAWDependencies.addAll(allRAWDeps);
	}

	public void setRAWDependents(Set<Call> newRAWDependents)
	{
		RAWDependents.clear();
		RAWDependents.addAll(newRAWDependents);
	}

	public boolean trulyDependsOfAny(HashSet<Call> callSet)
	{
		if (callSet.size() < RAWDependencies.size())
		{
			for (Call c: callSet)
			{
				if (RAWDependencies.contains(c))
					return true;
			}
		}
		else
		{
			for (Call c: RAWDependencies)
			{
				if (callSet.contains(c))
					return true;
			}
		}
		
		return false;
	}
	
	public boolean dependsOfAny(HashSet<Call> callSet)
	{
		for (Call c: callSet)
		{
			if (RAWDependencies.contains(c) || WAWDependencies.contains(c) || WARDependencies.contains(c))
				return true;
		}
		
		return false;
	}
}
