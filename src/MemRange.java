import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


public class MemRange {
	//TODO: Start using the following values to avoid useless intersection computation
	int minRange;
	int maxRange;
	
	ArrayList<IntegerRange> ranges;	
	
	public MemRange ()
	{
		detachedPositions = new ArrayList<Integer>();
		ranges = new ArrayList<IntegerRange>();
	}
	
	private class IntegerRange
	{
		public final int a;
		public final int b;
		
		public IntegerRange (int A, int B)
		{
			a = A;
			b = B;
		}
		
		public boolean intersects (IntegerRange x)
		{
			return (this.a >= x.a && this.a <= x.b) || (x.a >= this.a && x.a <= this.b);
		}
	}
	
	public void add(int start, int end)
	{
		//TODO: Adicionar em posição informada sobre Collections binarySort!
		ranges.add(new IntegerRange(start, end));
	}
	
	public int getMin()
	{
		return minRange;
	}
	
	public int getMax()
	{
		return maxRange;
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
	
	public boolean contains (int pos)
	{
		if (pos >= this.getMin() && pos <= this.getMax())
		{
		}
		
		return false;
	}
	
	public boolean intersects(MemRange x)
	{		
		if (!(this.getMin() >= x.getMin() && this.getMin() <= x.getMax() || x.getMin() >= this.getMin() && x.getMin() <= this.getMax()))
			return false;
		
		
				
		return true;		
	}
	
	public static void main (String args[])
	{
		
	}
}
