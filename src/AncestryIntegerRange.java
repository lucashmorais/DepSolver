import java.util.ArrayList;


public class AncestryIntegerRange extends IntegerRange {
	ArrayList<Object> children = null;
	public AncestryIntegerRange(int A, int B) {
		super(A, B);
	}
	
	public ArrayList<Object> becomeAParent(Object child)
	{
		if (children == null)
			children = new ArrayList<Object>();
		
		children.add(child);
		
		return children;
	}
	
	@Override
	public RangeRelation relationTo (IntegerRange x)
	{
		int A = x.a;
		int B = x.b;
		int a = this.a;
		int b = this.b;
		
		//Notice that some cases use the fact that a <= b.
		if (b < A || a > B)
			return RangeRelation.ISDISJOINTTO;
		else if (a < A && b >= A && b < B)
			return RangeRelation.LOWINTERSECTS;
		else if (a > A && a <= B && b > B)
			return RangeRelation.HIGHINTERSECTS;
		else if (a > A && a < B && b < B)
			return RangeRelation.ISCONTAINED;
		else if (a == A && b < B)
			return RangeRelation.ISLOWCONTAINED;
		else if (a > A && b == B)
			return RangeRelation.ISHIGHCONTAINED;
		else if (a < A && b == B)
			return RangeRelation.HIGHCONTAINS;
		else if (a == A && b > B)
			return RangeRelation.LOWCONTAINS;
		else if (a < A && b > B)
			return RangeRelation.CONTAINS;
		else if (a == A && b == B)
			return RangeRelation.COINCIDES;
		else
			throw new Error("Could not correctly compare two Integer Ranges.");
	}
	
	@Override
	public String toString() {
		return "[" + this.a + "," + this.b + "; " + this.children.toString() + "]";
	}

	public static void testToString()
	{
		AncestryIntegerRange range = new AncestryIntegerRange(0, 2);
		range.becomeAParent(new Call(10, "João"));
		range.becomeAParent(new Call(11, "Maria"));
		System.out.println(range);
	}
	
	public static void main (String[] args)
	{
		testToString();
	}
}
