public class Fibo {
	public static int fib(int n) {
		int fib_num;
		if (n == 1) {
			fib_num = 0;
			return fib_num;
		} else if (n == 2) {
			fib_num = 1;
			return fib_num;
		} else {
			return fib(n-1) + fib(n-2);
		}
	}

	public static void main(String[] args) {
		System.out.println(fib(5));
	}
}