public class ExamPrepArray {
    public static int[] flatten(int[][] x) {
        int totalLength = 0;

        for (int[] item : x) {
            totalLength += item.length;
        }

        int[] a = new int[totalLength];
        int aIndex = 0;

        for (int[] ints : x) {
            for (int anInt : ints) {
                a[aIndex] = anInt;
                aIndex++;
            }
        }

        return a;
    }

    public static void printArray(int[] items) {
        for (int item : items) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[][] x = new int[][] {{1, 2, 3}, {}, {7, 8}};
        int[] a = flatten(x);
        printArray(a);
    }
}