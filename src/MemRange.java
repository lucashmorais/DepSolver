import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;


public class MemRange implements MinGettable<IntegerRange> {
	
	TreeMap<IntegerRange, IntegerRange> rangesMap;	
	
	public MemRange ()
	{
		rangesMap = new TreeMap<IntegerRange, IntegerRange>();
	}
	
	public MemRange (MemRange old)
	{
		rangesMap = new TreeMap<IntegerRange, IntegerRange>(old.rangesMap);
	}
	
	public void add(int start, int end)
	{
		add(new IntegerRange(start, end));
	}
	
	public void add(IntegerRange range)
	{
		
		
		rangesMap.put(range, range);
	}
	
	public boolean remove(IntegerRange range)
	{
		return true;
	}

	//TODO: Implementar comparação levando em conta os limites mínimos e máximos!
	//TODO: Procurar evitar o uso da função de interseção de IntegerRange!
	public boolean intersects(MemRange x)
	{		
		return false;
	}

	//TODO: Fix!
	public boolean intersects(IntegerRange x)
	{
		return true;
	}
	
	@Override
	public IntegerRange getMin() {
		return null;
	}
	
	public static void main (String args[])
	{
		
	}
}
