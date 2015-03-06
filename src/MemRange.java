import java.util.ArrayList;
import java.util.Collections;
@SuppressWarnings("all")

public class MemRange implements MinGettable<IntegerRange> {
	//TODO: Start using the following values to avoid useless intersection computation
	int minRange;
	int maxRange;
	boolean isSorted;
	boolean isCoalesced;
	
	ArrayList<IntegerRange> ranges;	
	
	public MemRange ()
	{
		ranges = new ArrayList<IntegerRange>();
		isSorted = false;
		isCoalesced = false;
	}
	
	public MemRange (MemRange old)
	{
		ranges = new ArrayList<IntegerRange>(old.ranges);
		isSorted = old.isSorted;
		isCoalesced = old.isCoalesced;
		minRange = old.minRange;
		maxRange = old.maxRange;
	}
	
	public void add(int start, int end)
	{
		add(new IntegerRange(start, end));
	}
	
	public void add(IntegerRange range)
	{
		int start = range.a;
		int end = range.b;
		
		ranges.add(range);
		if (start < minRange)
			minRange = range.a;
		if (end > maxRange)
			maxRange = end;
		isSorted = false;		
	}
	
	public void addAll(ArrayList<IntegerRange> additionalRanges)
	{
		for (IntegerRange range: additionalRanges)
			add(range);
	}
	
	public void remove(int start, int end)
	{
		IntegerRange removing = new IntegerRange(start, end);
		IntegerRange last = lastStartingBefore(ranges, removing);
		
		if (last.b >= removing.a)
		{
			if (last.a < removing.a)
				last.b = removing.a - 1;
			
			remove(last);
		}
	}
	
	public boolean remove(IntegerRange range)
	{
		return ranges.remove(range);
	}
	
	public void sort()
	{
		Collections.sort(ranges);
		isSorted = true;
	}
	
	//TODO: Terminar de implementar esta busca binária!
	//TODO: Entender como procurar os elementos de comparação com máxima eficiência!
	private <T> boolean arrayContains(ArrayList<T> list, T key)
	{
		int a, b;
		
		a = 0;
		b = list.size() - 1;
		
		for (;;)
		
		return true;
	}
	
	/**
	 * Function for finding the last IntegerRange of 'ranges' starting on position
	 * not greater than "x.a"
	 * @param ranges ArrayList to be analysed
	 * @param x
	 * @return
	 */
	//TODO: Testar!
	private <T extends MinGettable<Integer>> T lastStartingBefore(ArrayList<T> ranges, T x) {
		IntegerRange res = null;
		
		int value0 = x.getMin();		
		int min = 0;
		int max = ranges.size() - 1;
		
		int m = (min + max) / 2;
		
		while (true)
		{		
			T current = ranges.get(m);
			int medVal = current.getMin();
			
			if (value0 < medVal)
			{
				max = m;
				m = (min + m) / 2;
			}
			else if (value0 > medVal)
			{
				min = m + 1;
				m = ((m + 1) + max) / 2;
			}
			else
			{
				return current;
			}
			
			if (min == max)
				return ranges.get(min);
		}
	}
	
	private <T extends MinGettable<Integer>> int lastStartingBeforePos(ArrayList<T> ranges, T x) {
		IntegerRange res = null;
		
		int value0 = x.getMin();		
		int min = 0;
		int max = ranges.size() - 1;
		
		int m = (min + max) / 2;
		
		while (true)
		{						
			T current = ranges.get(m);
			int medVal = current.getMin();
			
			if (value0 < medVal)
			{
				max = m;
				m = (min + m) / 2;
			}
			else if (value0 > medVal)
			{
				min = m + 1;
				m = ((m + 1) + max) / 2;
			}
			else
			{
				return m;
			}
			
			if (min == max)
				return min;
		}
	}

	//TODO: Implementar comparação levando em conta os limites mínimos e máximos!
	//TODO: Procurar evitar o uso da função de interseção de IntegerRange!
	public boolean intersects(MemRange x)
	{		
		MemRange lower, higher;
		
		if (!isSorted)
			sort();
		
		if (this.minRange < x.minRange)
		{
			lower = this;
			higher = x;
		}	
		else
		{
			lower = x;
			higher = this;
		}
		
		if (lower.maxRange < higher.minRange)
			return false;

		//TODO: Reduzir ainda mais a complexidade de tempo procurando elementos do MemRange
		//		para o qual a intersecção contém o menor número de IntegerRanges!
		for (int i = lastStartingBeforePos(lower.ranges, higher.getMin()); i < lower.ranges.size(); i++)
		{
			IntegerRange current = lower.ranges.get(i);
			
			if (higher.intersects(current))
				return true;
		}
				
		return false;
	}

	//TODO: Fix!
	public boolean intersects(IntegerRange x)
	{
		if (this.minRange < x.a)
		{
			if (this.maxRange < x.a)
				return false;
		}
		else
		{
			if (x.b < this.minRange)
				return false;
		}
		
		return lastStartingBefore(ranges, x).b >= x.a;
	}
	
	//TODO: Finish!
	public boolean removeMinInterceptor(IntegerRange x)
	{
		IntegerRange last;
		
		if (this.minRange < x.a)
		{
			if (this.maxRange < x.a)
				return false;
		}
		else
		{
			if (x.b < this.minRange)
				return false;
		}
		
		return lastStartingBefore(ranges, x).b >= x.a;
	}
	
	/**
	 * Function that overwrites the current 'ranges' object with another
	 * ArrayList free of intersecting IntegerRanges.
	 */
	public void coalesceOverRange ()
	{
		ArrayList<IntegerRange> newRanges = new ArrayList<IntegerRange>();
		int nIter = ranges.size() - 1;
		IntegerRange current, next;
		
		if (!isSorted)
			sort();
		
		for (int i = 0; i < nIter;)
		{
			int nextIter = i + 1;
			
			current = ranges.get(i);
			next = ranges.get(nextIter);
			
			if (current.b >= next.a)
			{
				current.coalesceWith(next);
				newRanges.add(current);
				i = nextIter + 1;
			}
			else
			{
				newRanges.add(next);
				newRanges.add(current);
				i = nextIter;
			}
		}
		
		isCoalesced = true;		
		ranges = newRanges;
	}
	
	@Override
	public IntegerRange getMin() {
		if (!isSorted)
			sort();
		return this.ranges.get(0);
	}
	
	public static void main (String args[])
	{
		
	}
}
