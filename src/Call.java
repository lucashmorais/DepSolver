import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


public class Call implements Cloneable, Comparable<Call>{
	TreeSet<Integer> readPositions;
	TreeSet<Integer> writePositions;
	HashSet<Call> RAWDependencies;
	HashSet<Call> WAWDependencies;
	HashSet<Call> WARDependencies;
	HashSet<Call> AllDependencies;
	HashSet<Call> AllRAWDependencies;
	HashSet<Call> RAWDependents;
	HashSet<Call> AllDependents;
	int callNumber;
	int numDependencies;
	int numRAWDependencies;
	String procedureName;
	
	public HashSet<Call> getAllRAWDependencies() {
		return AllRAWDependencies;
	}	

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof Call))
			return false;

		if (((Call) other).getCallNumber() == this.callNumber)
			return true;
		return false;
	}

	public int compareTo(Call other)
	{
		return this.callNumber - other.getCallNumber();
	}

	public HashSet<Call> getAllDependencies() {
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

	public HashSet<Call> getAllDependents() {
		return AllDependents;
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
		AllDependencies = new HashSet<Call>();
		AllDependents = new HashSet<Call>();
		WAWDependencies = new HashSet<Call>();
		WARDependencies = new HashSet<Call>();
		RAWDependents = new HashSet<Call>();
		numDependencies = 0;
		numRAWDependencies = 0;
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
		AllDependencies.add(dep);
		numRAWDependencies++;
		numDependencies++;
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
		AllDependencies.add(dep);
		numDependencies++;
	}
	
	public void addWAR (Call dep)
	{
		WARDependencies.add(dep);
		AllDependencies.add(dep);
		numDependencies++;
	}

	public void addRAWDependent (Call dependent)
	{
		RAWDependents.add(dependent);
		AllDependents.add(dependent);
	}

	public void addWAWDependent (Call dependent)
	{
		AllDependents.add(dependent);
	}

	public void addWARDependent (Call dependent)
	{
		AllDependents.add(dependent);
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

	public void setAllDependencies(Set<Call> allDeps)
	{
		AllDependencies.clear();
		AllDependencies.addAll(allDeps);
	}

	public void setRAWDependents(Set<Call> newRAWDependents)
	{
		RAWDependents.clear();
		RAWDependents.addAll(newRAWDependents);
	}

	public void setAllDependents(Set<Call> newAllDependents)
	{
		AllDependents.clear();
		AllDependents.addAll(newAllDependents);
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

	public void resetDependencyCounters()
	{
		//HashSet<Call> all = new HashSet<Call>;
		
		//all.addAll(RAWDependencies);
		//all.addAll(WAWDependencies);
		//all.addAll(WARDependencies);
	
		numRAWDependencies = RAWDependencies.size();
		numDependencies = AllDependencies.size();
		//numDependencies = all.size();
	}
}





