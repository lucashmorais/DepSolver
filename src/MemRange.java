import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Random;
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
		boolean thereIsLow, thereIsHigh;
		Entry<IntegerRange, Call> lower, higher;
		IntegerRange lowKey = null, highKey = null, newLow, newUpperLow;
		Call lowVal = null, highVal = null;
		ArrayList<Call> callList = new ArrayList<Call>();
		NavigableMap<IntegerRange, Call> miniMap, auxMap;
		
		if (rangesMap.isEmpty())
		{
			rangesMap.put(range, call);
			return callList;
		}			
		
		boolean fromInclusive, toInclusive;		
		
		//TODO: Testar higher com ceilingEntry
		lower = rangesMap.floorEntry(range);
		higher = rangesMap.floorEntry(new IntegerRange(range.b, range.b));
		if (lower != null)
		{
			lowKey = lower.getKey();
			lowVal = lower.getValue();
			fromInclusive = lowKey.b >= range.a;
			thereIsLow = true;
		}
		else
		{
			fromInclusive = false;
			thereIsLow = false;
		}
		
		if (higher != null)
		{
			highKey = higher.getKey();
			highVal = higher.getValue();
			toInclusive = range.b >= highKey.a;
			thereIsHigh = true;
		}
		else
		{
			toInclusive = false;
			thereIsHigh = false;
		}				
		
		if (thereIsHigh && thereIsLow)
		{
			miniMap = rangesMap.subMap(lowKey, fromInclusive, highKey, toInclusive);
		}
		else if (thereIsLow)
		{
			miniMap = rangesMap.tailMap(lowKey, fromInclusive);
		}
		else if (thereIsHigh)
		{
			miniMap = rangesMap.headMap(highKey, toInclusive);
		}
		else
		{
			rangesMap.put(range, call);
			return callList;
		}
		
		callList.addAll(miniMap.values());	
		
		//System.out.println(miniMap);
		
		auxMap = new TreeMap<>(miniMap);
		
		miniMap.clear();
		
		if (thereIsLow)
		{			
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
			
			
		}
		
		if (thereIsHigh)
		{
			if (call == highVal)
			{
				switch (range.relationTo(highKey))
				{
				case CONTAINS:			
										break;
				case HIGHINTERSECTS:	
										rangesMap.put(highKey, highVal);
										break;
				case LOWINTERSECTS:		
										rangesMap.put(highKey, highVal);
										break;
				case ISCONTAINED:		
										break;
				case ISDISJOINTTO:		
										rangesMap.put(highKey, highVal);
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
				case LOWINTERSECTS:		
										break;
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
			
			rangesMap.put(highKey, highVal);
		}
		
		return callList;
	}
	
	public ArrayList<Call> add(MemRange x)
	{
		ArrayList<Call> callList = new ArrayList<Call>();
		
		for (Entry<IntegerRange, Call> entry: x.rangesMap.entrySet())
		{
			callList.addAll(add(entry.getKey(), entry.getValue()));
		}
		
		return callList;
	}

	public void plainAdd(IntegerRange range, Call call)
	{
		rangesMap.put(range, call);
	}
	
	public void plainAdd(int start, int end, Call call)
	{
		rangesMap.put(new IntegerRange(start, end), call);
	}
	
	public void plainAdd(MemRange x)
	{
		for (Entry<IntegerRange, Call> e: x.rangesMap.entrySet())
		{
			rangesMap.put(e.getKey(), e.getValue());
		}
	}
	
	//TODO: Implementar comparação levando em conta os limites mínimos e máximos!
	public ArrayList<Call> intersects(MemRange x)
	{	
		ArrayList<Call> callList = new ArrayList<Call>();
		if (rangesMap.isEmpty())
			return callList;
		
		int thisMin = rangesMap.firstKey().a;
		int thisMax = rangesMap.lastKey().b;
		int thatMin = x.rangesMap.firstKey().a;
		int thatMax = x.rangesMap.lastKey().b;
		
		IntegerRange thisRough = new IntegerRange(thisMin, thisMax);
		IntegerRange thatRough = new IntegerRange(thatMin, thatMax);
		
		if (!thisRough.intersects(thatRough))
			return null;
		
		for (IntegerRange range: x.rangesMap.keySet())
		{
			callList.addAll(this.intersects(range));
		}
		
		return callList;
	}

	public ArrayList<Call> intersects(IntegerRange range)
	{
		Entry<IntegerRange, Call> lower, higher;
		IntegerRange lowKey, highKey;
		ArrayList<Call> callList = new ArrayList<Call>();
		boolean fromInclusive = false, toInclusive = false;	
		boolean thereIsLow, thereIsHigh;
		
		//TODO: Testar higher com ceiling
		lower = rangesMap.floorEntry(range);
		higher = rangesMap.floorEntry(new IntegerRange(range.b, range.b));
		lowKey = lower.getKey();
		highKey = higher.getKey();
		
		thereIsLow = lowKey != null;
		thereIsHigh = highKey != null;
		
		if (thereIsLow) fromInclusive = lowKey.b >= range.a;
		if (thereIsHigh) toInclusive = range.b >= highKey.a;
		
		NavigableMap<IntegerRange, Call> miniMap;
		
		if (thereIsLow && thereIsHigh)
			miniMap = rangesMap.subMap(lowKey, fromInclusive, highKey, toInclusive);
		else if (thereIsLow)
			miniMap = rangesMap.tailMap(lowKey, fromInclusive);
		else if (thereIsHigh)
			miniMap = rangesMap.headMap(highKey, toInclusive);
		else
			return new ArrayList<Call>();
		
		callList.addAll(miniMap.values());
		
		return callList;
	}
	
	@Override
	public IntegerRange getMin() {
		return rangesMap.firstKey();
	}
	
	public static void testRandomAdd (int numOfAdds)
	{
		MemRange mem = new MemRange();
		Random rand = new Random();
		final int min = 0;
		final int max = 100;
		final int size = 4;
		
		for (int i = 0; i < numOfAdds; i++)
		{
			int start = rand.nextInt(max);
			Call call = new Call(i, "call");
			IntegerRange range = new IntegerRange(start, start + size);
			
			System.out.println("Adding range " + range + "...");
			mem.add(range, call);
			System.out.println("MemRange at iteration " + i + ": " + mem.rangesMap);
		}
	}
	
	public static void teste3 ()
	{
		Call call1 = new Call(10, "Ronaldo");
		Call call2 = new Call(8, "Romário");
		MemRange test = new MemRange();
		
		test.add(0, 7, call1);
		test.add(5, 10, call2);
		
		System.out.println(test.rangesMap.toString());
	}
	
	public static void teste2 ()
	{
		Call call = new Call(10, "Ronaldo");
		MemRange test = new MemRange();
		
		test.add(0, 7, call);
		test.add(5, 10, call);
		
		System.out.println(test.rangesMap.toString());
	}
	
	public static void teste1 ()
	{
		MemRange test = new MemRange();
		
		test.add(0, 7, new Call(11, "Ronaldo"));
		test.add(5, 6, new Call(2, "George"));
		
		System.out.println(test.rangesMap.toString());		
	}
	
	public static void teste0 ()
	{
		MemRange test = new MemRange();
		
		test.add(0, 7, new Call(11, "Ronaldo"));
		test.add(15, 20, new Call(2, "George"));
		
		System.out.println(test.rangesMap.toString());
	}
	
	public static void main (String args[])
	{
		//teste0();
		//teste1();
		//teste2();
		//teste3();
		testRandomAdd(10);
	}
}
