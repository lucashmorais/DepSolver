import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class Call implements Cloneable{
	//TODO: Trocar estas estruturas de dados por MemRanges!
	MemRange reads;
	MemRange writes;
	HashSet<Call> RAWDependencies;
	HashSet<Call> WAWDependencies;
	HashSet<Call> WARDependencies;
	HashSet<Call> AllRAWDependencies;
	int callNumber;
	String procedureName;
	
	public HashSet<Call> getAllRAWDependencies() {
		return new HashSet<Call>(AllRAWDependencies);
	}	
	
	public HashSet<Call> getRAWDependencies() {
		return new HashSet<Call>(RAWDependencies);
	}

	public HashSet<Call> getWAWDependencies() {
		return new HashSet<Call>(WAWDependencies);
	}

	public HashSet<Call> getWARDependencies() {
		return new HashSet<Call>(WARDependencies);
	}
	
	public Call clone()
	{
		Call copy = new Call(callNumber, procedureName, reads, writes);
		copy.setAllRAW(AllRAWDependencies);
		copy.setRAW(RAWDependencies);
		copy.setWAR(WARDependencies);
		copy.setWAW(WAWDependencies);
		
		return copy;		
	}
	
	private void initializeDepSets()
	{
		RAWDependencies = new HashSet<Call>();
		AllRAWDependencies = new HashSet<Call>();
		WAWDependencies = new HashSet<Call>();
		WARDependencies = new HashSet<Call>();
	}
	
	public Call ()
	{
		reads = new MemRange();
		writes = new MemRange();
		callNumber = -1;
		procedureName = "UNMD";
		initializeDepSets();
	}
	
	public Call (int num)
	{
		reads = new MemRange();
		writes = new MemRange();
		callNumber = num;
		procedureName = "UNMD";
		initializeDepSets();
	}

	public Call (int num, String name)
	{
		reads = new MemRange();
		writes = new MemRange();
		callNumber = num;
		procedureName = name;
		initializeDepSets();
	}
	
	public Call (int num, String name, MemRange newReads, MemRange newWrites)
	{
		this.reads = new MemRange(newReads);
		this.writes = new MemRange(newWrites);
		callNumber = num;
		procedureName = name;
		initializeDepSets();
	}
	
	public void setName (String name)
	{
		procedureName = name;
	}
	
	@Deprecated
	public MemRange getWrites()
	{
		return new MemRange(writes);
	}
	
	@Deprecated
	public MemRange getReads()
	{
		return new MemRange(reads);
	}
	
	public void addRAW (Call dep)
	{
		RAWDependencies.add(dep);
		AllRAWDependencies.add(dep);
	}
	
	public void addRAW (ArrayList<Call> list)
	{
		if (list == null)
			return;
		RAWDependencies.addAll(list);
		AllRAWDependencies.addAll(list);
	}
	
	public void addWAW (Call dep)
	{
		WAWDependencies.add(dep);
	}
	
	public void addWAW (ArrayList<Call> list)
	{
		if (list == null)
			return;
		WAWDependencies.addAll(list);
	}
	
	public void addWAR (Call dep)
	{
		WARDependencies.add(dep);
	}
	
	public void addWAR (ArrayList<Call> list)
	{
		if (list == null)
			return;
		WARDependencies.addAll(list);
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
