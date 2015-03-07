
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
			return (this.a >= x.a && this.a <= x.b) || (x.a >= this.a && x.a <= this.b);
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
	}
