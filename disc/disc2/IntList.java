public class IntList {
	public int first;
	public IntList rest;

	public IntList(int f, IntList r) {
		first = f;
		rest = r;
	}

	public static IntList square(IntList L) {
		if (L == null) {
			return L;
		}
		first = L.first * L.first;
		rest = square(L.rest);
		IntList Q = new IntList(first, rest);
		return Q;
	}

	public static IntList squareMutative(IntList L) {
		if (L == null) {
			return L;
		}
		L.first = L.first * L.first;
		L.rest = squareMutative(L.rest);
		return L;
	} 

	public static IntList squareIterative(IntList L) {
		if (L == null) {
			return L;
		}
		IntList Q = new IntList(L.first * L.first, L.rest);    
		IntList P = L;
        IntList K = Q;
		while (P.rest != null) {
			P = P.rest;
			K.rest = new IntList(P.first * P.first, null);
            K = K.rest;
		}
		return Q;
	}

	public static IntList squareMutativeIterative(IntList L) {
		IntList P = L;
		while (P != null) {
			P.first = (int) Math.pow(P.first, 2);
			P = P.rest;
		}
		return L;
	}

	public static void main(String[] args) {
		IntList L = new IntList(15, null);
		L = new IntList(10, L);
		L = new IntList(5, L);
		System.out.println(square(L).first);
		// System.out.println(squareMutative(L).first);
		System.out.println(squareIterative(L).first);
		System.out.println(squareMutativeIterative(L).first);
	}
}