import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.NavigableMap;


public class ReadMemRange extends MemRange {
	TreeMap<AncestryIntegerRange, Call> rangesMap;	
	
	public ReadMemRange ()
	{
		rangesMap = new TreeMap<AncestryIntegerRange, Call>();
	}
	
	private boolean putIfValid(AncestryIntegerRange range, Call call)
	{
		if (range.a > range.b)
			return false;
		rangesMap.put(range, call);
		return true;
	}
	
	public ArrayList<Call> add(AncestryIntegerRange range, Call call)
	{		
		boolean thereIsLow, thereIsHigh;
		boolean addRange = false;
		Entry<AncestryIntegerRange, Call> lower, higher;
		AncestryIntegerRange lowKey = null, highKey = null;
		AncestryIntegerRange newLowerLow, newUpperLow, lowerRange, upperRange, lowIntersection, highIntersection, lowerHigh, upperHigh;		
		Call lowVal = null, highVal = null;
		ArrayList<Call> callList = new ArrayList<Call>();
		NavigableMap<AncestryIntegerRange, Call> miniMap;
		
		if (rangesMap.isEmpty())
		{
			rangesMap.put(range, call);
			return callList;
		}			
		
		boolean fromInclusive, toInclusive;		
		
		lower = rangesMap.floorEntry(range);
		higher = rangesMap.floorEntry(new AncestryIntegerRange(range.b, range.b));
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
		
		miniMap.clear();
		
		//TODO: Continuar daqui!
		//Notar que o código que segue não faz o coalescing dos intervalos [0,3] e [4,7]!
		if (thereIsLow)
		{			
			if (call == lowVal)
			{
				switch (range.relationTo(lowKey))
				{
				case CONTAINS:
				case COINCIDES:
				case LOWCONTAINS:
				case HIGHCONTAINS:		addRange = true;
										break;
				case HIGHINTERSECTS:	range.a = lowKey.a;
										addRange = true;
										break;
				case LOWINTERSECTS:		throw new Error("Impossible range relation occured.");
				
				case ISCONTAINED:		rangesMap.put(lowKey, lowVal);
										break;
				case ISDISJOINTTO:		rangesMap.put(lowKey, lowVal);
										addRange = true;
										break;
				case ISHIGHCONTAINED:	rangesMap.put(lowKey, lowVal);
										break;
				case ISLOWCONTAINED:	rangesMap.put(lowKey, lowVal);
										break;
				}
			}
			else
			{
				newLowerLow = new AncestryIntegerRange(lowKey.a, range.a - 1);
				newUpperLow = new AncestryIntegerRange(range.b + 1, lowKey.b);
				lowerRange = new AncestryIntegerRange(range.a, lowKey.a - 1);
				upperRange = new AncestryIntegerRange(lowKey.b + 1, range.b);
				highIntersection = new AncestryIntegerRange(range.a, lowKey.b);
				
				
				switch (range.relationTo(lowKey))
				{
				case CONTAINS:			throw new Error("Impossible range relation occured.");
					
				case COINCIDES:			lowKey.becomeAParent(call);
										rangesMap.put(lowKey, lowVal);
										addRange = false;
										break;
				case LOWCONTAINS:		range = upperRange;
										addRange = true;
										lowKey.becomeAParent(call);
										rangesMap.put(lowKey, lowVal);
										break;
				case HIGHCONTAINS:		range = lowerRange;
										addRange = true;
										lowKey.becomeAParent(call);
										rangesMap.put(lowKey, lowVal);
										break;					
				case HIGHINTERSECTS:	range = upperRange;
										addRange = true;
										highIntersection.becomeAParent(call);
										rangesMap.put(newLowerLow, lowVal);
										rangesMap.put(highIntersection, lowVal);
										break;
				case LOWINTERSECTS:		throw new Error("Impossible range relation occured.");
				
				case ISCONTAINED:		rangesMap.put(newLowerLow, lowVal);
										rangesMap.put(newUpperLow, lowVal);
										range.becomeAParent(call);
										rangesMap.put(range, lowVal);					//Range aqui assume o papel do intervalo antigo
										addRange = false;
										break;
				case ISDISJOINTTO:		rangesMap.put(lowKey, lowVal);
										addRange = true;
										break;
				case ISHIGHCONTAINED:	rangesMap.put(newLowerLow, lowVal);
										range.becomeAParent(call);
										rangesMap.put(range, lowVal);
										addRange = false;
										break;
				case ISLOWCONTAINED:	rangesMap.put(newUpperLow, lowVal);
										range.becomeAParent(call);
										rangesMap.put(range, lowVal);
										break;
				}			
			}			
		}
		else
			addRange = true;
		
		//TODO: Remover atribuições lógicas redundantes!
		if (thereIsHigh)
		{
			if (call == highVal)
			{
				switch (range.relationTo(highKey))
				{
				case CONTAINS:			
				case COINCIDES:
				case LOWCONTAINS:
				case HIGHCONTAINS:		addRange = true;							//highKey pode ser diferente de lowKey
										break;
				case HIGHINTERSECTS:												//lowKey existe e já foi adicionada
										break;
				case LOWINTERSECTS:		range.b = highKey.b;						//highKey não precisa ser adicionada
										addRange = true;
										break;
				case ISCONTAINED:		addRange = false;							//lowKey = highKey existe; range não será adicionada
										break;
				case ISDISJOINTTO:		rangesMap.put(highKey, highVal);			//highKey deve ser readicionada
										addRange = true;
										break;
				case ISHIGHCONTAINED:	addRange = false;							//lowKey = highKey já foi adicionada
										break;
				case ISLOWCONTAINED:	addRange = false;							//lowKey = highKey já foi adicionada
										break;
				}			
			}
			else
			{	
				lowerHigh = new AncestryIntegerRange(highKey.a, range.a - 1);
				upperHigh = new AncestryIntegerRange(range.b + 1, highKey.b);
				lowerRange = new AncestryIntegerRange(range.a, highKey.a - 1);
				upperRange = new AncestryIntegerRange(highKey.b + 1, range.b);
				highIntersection = new AncestryIntegerRange(range.a, highKey.b);
				lowIntersection = new AncestryIntegerRange(highKey.a, range.b);
				
				switch (range.relationTo(highKey))
				{
				case CONTAINS:			addRange = false;
										highKey.becomeAParent(call);
										rangesMap.put(lowerRange, call);
										rangesMap.put(upperRange, call);
										rangesMap.put(highKey, highVal);
				case COINCIDES:			addRange = false;
										highKey.becomeAParent(call);
										rangesMap.put(highKey, highVal);
				case LOWCONTAINS:		addRange = false;
										highKey.becomeAParent(call);
										rangesMap.put(highKey, highVal);
										rangesMap.put(upperRange, call);										
				case HIGHCONTAINS:		addRange = false;							
										highKey.becomeAParent(call);
										rangesMap.put(lowerRange, call);
										rangesMap.put(highKey, highVal);
										break;
				case HIGHINTERSECTS:	addRange = false;							
										highIntersection.becomeAParent(call);
										rangesMap.put(lowerHigh, highVal);
										rangesMap.put(upperRange, call);
										rangesMap.put(highIntersection, highVal);
										break;
				case LOWINTERSECTS:		addRange = true;							
										//TODO tratar todos os subcasos!
										//TODO continuar daqui!
										rangesMap.put(, highVal);
										break;
				case ISCONTAINED:		addRange = false;							
										range.becomeAParent(call);
										rangesMap.put(range, highVal);
										rangesMap.put(lowerHigh, highVal);
										rangesMap.put(upperHigh, highVal);
										break;
				case ISDISJOINTTO:		addRange = false;
										rangesMap.put(highKey, highVal);
										rangesMap.put(range, call);
										break;
				case ISHIGHCONTAINED:	addRange = false;
										range.becomeAParent(call);
										rangesMap.put(lowerHigh, highVal);
										rangesMap.put(range, highVal);
										break;
				case ISLOWCONTAINED:	addRange = false;
										range.becomeAParent(call);
										rangesMap.put(upperHigh, highVal);
										rangesMap.put(range, highVal);
										break;
				}				
			}
		}
		
		if (addRange)
			rangesMap.put(range, call);
		
		return callList;
	}
	
}
