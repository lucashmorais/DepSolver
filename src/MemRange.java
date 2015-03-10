import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;


public class MemRange implements MinGettable<IntegerRange> {
	
	TreeMap<IntegerRange, Call> rangesMap;	
	
	public MemRange ()
	{
		rangesMap = new TreeMap<IntegerRange, Call>();
	}
	
	public MemRange (MemRange old)
	{
		rangesMap = new TreeMap<IntegerRange, Call>(old.rangesMap);
	}
	
	public ArrayList<Call> add(int start, int end, Call call)
	{
		return add(new IntegerRange(start, end), call);
	}
	
	public ArrayList<Call> add(IntegerRange range, Call call)
	{
		Entry<IntegerRange, Call> lower, higher;
		IntegerRange lowKey, highKey, newLow, newUpperLow;
		Call lowVal, highVal;
		ArrayList<Call> callList = new ArrayList<Call>();
		boolean fromInclusive, toInclusive;		
		
		lower = rangesMap.floorEntry(range);
		higher = rangesMap.ceilingEntry(new IntegerRange(range.b, range.b));
		lowKey = lower.getKey();
		highKey = higher.getKey();
		lowVal = lower.getValue();
		highVal = higher.getValue();
		
		fromInclusive = lowKey.b >= range.a;
		toInclusive = range.b >= highKey.a;
		
		NavigableMap<IntegerRange, Call> miniMap = rangesMap.subMap(lowKey, fromInclusive, highKey, toInclusive);
		
		callList.addAll(miniMap.values());
		
		miniMap.clear();
		
		if (call == lowVal)
		{
			switch (range.relationTo(lowKey))
			{
			case CONTAINS:			rangesMap.put(range, call);
									break;
			case HIGHINTERSECTS:	range.a = lowKey.a;
									rangesMap.put(range, call);
									break;
			case LOWINTERSECTS:		throw new Error("Impossible range relation occured.");
			
			case ISCONTAINED:		rangesMap.put(lowKey, lowVal);
									break;
			case ISDISJOINTTO:		rangesMap.put(lowKey, lowVal);
									rangesMap.put(range, call);
									break;
			case ISHIGHCONTAINED:	rangesMap.put(lowKey, lowVal);
									break;
			case ISLOWCONTAINED:	rangesMap.put(lowKey, lowVal);
									break;
			}
		}
		else
		{
			newLow = new IntegerRange(lowKey.a, range.a - 1);
			newUpperLow = new IntegerRange(range.b + 1, lowKey.b);
			
			switch (range.relationTo(lowKey))
			{
			case CONTAINS:			rangesMap.put(range, call);
									break;
			case HIGHINTERSECTS:	rangesMap.put(newLow, lowVal);
									rangesMap.put(range, call);
									break;
			case LOWINTERSECTS:		throw new Error("Impossible range relation occured.");
			
			case ISCONTAINED:		rangesMap.put(newLow, lowVal);
									rangesMap.put(newUpperLow, highVal);
									rangesMap.put(range, call);
									break;
			case ISDISJOINTTO:		rangesMap.put(lowKey, lowVal);
									rangesMap.put(range, call);
									break;
			case ISHIGHCONTAINED:	rangesMap.put(newLow, lowVal);
									rangesMap.put(range, call);
									break;
			case ISLOWCONTAINED:	rangesMap.put(newUpperLow, lowVal);
									rangesMap.put(range, call);
									break;
			}			
		}
		
		if (call == highVal)
		{
			switch (range.relationTo(highKey))
			{
			case CONTAINS:			
									break;
			case HIGHINTERSECTS:	
									break;
			case LOWINTERSECTS:		throw new Error("Impossible range relation occured.");
			
			case ISCONTAINED:		
									break;
			case ISDISJOINTTO:		rangesMap.put(highKey, highVal);
									break;
			case ISHIGHCONTAINED:	
									break;
			case ISLOWCONTAINED:	
									break;
			}			
		}
		else
		{			
			switch (range.relationTo(highKey))
			{
			case CONTAINS:			
									break;
			case HIGHINTERSECTS:	
									break;
			case LOWINTERSECTS:		throw new Error("Impossible range relation occured.");
			
			case ISCONTAINED:		
									break;
			case ISDISJOINTTO:		rangesMap.put(highKey, highVal);
									break;
			case ISHIGHCONTAINED:	
									break;
			case ISLOWCONTAINED:	
									break;
			}				
		}
		
		return callList;
	}

	//TODO: Implementar comparação levando em conta os limites mínimos e máximos!
	public boolean intersects(MemRange x)
	{	
		int thisMin = rangesMap.firstKey().a;
		int thisMax = rangesMap.lastKey().b;
		int thatMin = x.rangesMap.firstKey().a;
		int thatMax = x.rangesMap.lastKey().b;
		IntegerRange thisRough = new IntegerRange(thisMin, thisMax);
		IntegerRange thatRough = new IntegerRange(thatMin, thatMax);
		Iterator<IntegerRange> iterator;
		
		if (!thisRough.intersects(thatRough))
			return false;
		
		//TODO: Do I really need a tailMap from rangesMap?
		iterator = rangesMap.tailMap(rangesMap.floorKey(thatRough),true).navigableKeySet().iterator();
		
		//TODO: Test wheter iterator is able to retrieve the first item
		while (iterator.hasNext())
		{
			if (this.intersects(iterator.next()))
				return true;
		}
		
		return false;
	}

	public boolean intersects(IntegerRange x)
	{
		IntegerRange tester = rangesMap.floorKey(new IntegerRange(x.b, x.b));
		
		return x.intersects(tester);
	}
	
	@Override
	public IntegerRange getMin() {
		return rangesMap.firstKey();
	}
	
	public static void main (String args[])
	{
		
	}
}
