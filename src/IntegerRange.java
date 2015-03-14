
public class IntegerRange implements Comparable<IntegerRange>, MinGettable<Integer>
	{
		int a;
		int b;
		
		public IntegerRange (int A, int B)
		{
			a = A;
			b = B;
		}
		
		public boolean intersects (IntegerRange x)
		{
			int a = x.a;
			int b = x.b;
			int A = this.a;
			int B = this.b;
			
			return (A >= a && A <= b) || (B >= a && B <= b);
		}
		
		public RangeRelation relationTo (IntegerRange x)
		{
			int A = x.a;
			int B = x.b;
			int a = this.a;
			int b = this.b;
			
			//Notice that some cases use the fact that a <= b.
			if (a < A && b >= A && b < B)
				return RangeRelation.LOWINTERSECTS;
			else if (a > A && a <= B && b > B)
				return RangeRelation.HIGHINTERSECTS;
			else if (a > A && a < B && b < B)
				return RangeRelation.ISCONTAINED;
			else if (a == A && b < B)
				return RangeRelation.ISLOWCONTAINED;
			else if (a > A && b == B)
				return RangeRelation.ISHIGHCONTAINED;
			else if (a <= A && b >= B)
				return RangeRelation.CONTAINS;
			else if (b < A || a > B)
				return RangeRelation.ISDISJOINTTO;
			else
				throw new Error("Could not correctly compare two Integer Ranges.");
		}

		@Override
		public int compareTo(IntegerRange other) {
			return this.a - other.a;
		}
		
		/**
		 * This method implements IntegerRange coalescing.
		 * It should be noted that it does not free the 'other'
		 * object.
		 */
		public void coalesceWith(IntegerRange other)
		{
			if (this.a > other.a)
				this.a = other.a;
			if (this.b < other.b)
				this.b = other.b;
		}

		@Override
		public Integer getMin() {
			return this.a;
		}
		
		@Override
		public boolean equals(Object other)
		{
			if (other.getClass() != IntegerRange.class)
				return false;
			
			return this.a == ((IntegerRange) other).a;
		}
		
		@Override
		public String toString() {
			return "[" + this.a + "," + this.b + "]";
		}
	}
