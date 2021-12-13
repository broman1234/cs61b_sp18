public class IntList {
	public int first;
	public IntList rest;

	public IntList(int f, IntList r) {
		first = f;
		rest = r;
	}

	/** Return the size of the list using... recursion! */
	public int size() {
		if (rest == null) {
			return 1;
		}
		return 1 + this.rest.size();
	}

	/** Return the size of the list using no recursion! */
	public int iterativeSize() {
		IntList p = this;
		int totalSize = 0;
		while (p != null) {
			totalSize += 1;
			p = p.rest;
		}
		return totalSize;
	}

	/** Returns the ith value in this list.*/
	public int get(int i) {
		if (i == 0) {
			return first;
		}
		return rest.get(i - 1);
	}

	/** Returns an IntList identical to L, but with
      * each element incremented by x. L is not allowed
      * to change. */
    public static IntList incrList(IntList L, int x) {
        /* Your code here. */
        // IntList Q = new IntList(L.first, L.rest);
        if (L == null) {
            return null;
        }
        int newFirst = L.first + x;
        IntList newRest = incrList(L.rest, x);
        return new IntList(newFirst, newRest);
    }

    /** Returns an IntList identical to L, but with
      * each element incremented by x. Not allowed to use
      * the 'new' keyword. */
    public static IntList dincrList(IntList L, int x) {
        /* Your code here. */
		if (L == null) {
			return null;
		}
		L.first = L.first + x;
		dincrList(L.rest, x);
		return L;
	}

	public static IntList dilsans(IntList x, int y) {
		if (x == null) {
			return null;
		}
		x.rest = dilsans(x.rest, y);
		if (x.first == y) {
			return x.rest;
		}
		return x;
	}

	public void print() {
        IntList p = this;
        while (p != null) {
            System.out.print(p.first + " ");
            p = p.rest;
        }
		System.out.println();
    }

	public static void main(String[] args) {

		IntList L = new IntList(15, null);
		L = new IntList(10, L);
		L = new IntList(5, L);

		//System.out.println(L.iterativeSize());
		//System.out.println(incrList(L, 3).first);
		IntList newList = incrList(L, 4);
		newList.print();
		L.print();
		newList = dincrList(L, 3);
		newList.print();
		L.print();

		/*
		IntList a = new IntList(4, null);
		a = new IntList(7, a);
		a = new IntList(5, a);
		a = new IntList(4, a);
		a = new IntList(3, a);
		a = new IntList(2, a);
		dilsans(a, 4);
		a.print();
		 */
	}
} 
