public class HelloNumbers {
    public static void main(String[] args) {
        int x = 0;
        int s = 0;
        while (x < 9) {
            System.out.print(s + " ");
            x = x + 1;
            s = s + x;
        }
        System.out.println(s);
    }
}
