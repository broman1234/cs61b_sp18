public class AList {
    private int[] items;

    public AList(int capacity) {
        items = new int[capacity];
    }

    /* my solution */
    public static int[] insert(int[] arr, int item, int position) {
        AList resultArr = new AList(arr.length + 1);
        position = Math.min(arr.length, position);
        resultArr.items[position] = item;
        for (int i = 0, j = 0; i < arr.length; i++, j++) {
            if (j == position) {
                j++;
                resultArr.items[j] = arr[i];
            }else {
                resultArr.items[j] = arr[i];
            }
        }
        return resultArr.items;
    }

    /* solution */
    public static int[] insertSol(int[] arr, int item, int position) {
        AList resultArr = new AList(arr.length + 1);
        position = Math.min(arr.length, position);
        for (int i = 0; i < position; i++) {
            resultArr.items[i] = arr[i];
        }
        resultArr.items[position] = item;
        for (int i = position; i < arr.length; i++) {
            resultArr.items[i + 1] = arr[i];
        }
        return resultArr.items;
    }

    public static void reverse(int[] arr) {
        int halfLength = arr.length / 2;
        for (int i = 0, j = arr.length - 1; i < halfLength; i++, j--) {
            int leftItem = arr[i];
            arr[i] = arr[j];
            arr[j] = leftItem;
        }
    }

    /* my solution */
    public static int[] replicate(int[] arr) {
        int capacity = 0;
        for (int value : arr) {
            capacity += value;
        }
        AList a = new AList(capacity);
        int m = 0;
        for (int i = 0, j = 0; i < arr.length; j += arr[i], i++) {
            m = m + arr[i];
            for (int k = j; k < m; k++) {
                a.items[k] = arr[i];
            }
        }
        return a.items;
    }

    /* solution */
    public static int[] replicateSol(int[] arr) {
        int capacity = 0;
        for (int item : arr) {
            capacity += item;
        }
        AList a = new AList(capacity);
        int i = 0;
        for (int item : arr) {
            for (int counter = 0; counter < item; counter++)  {
                a.items[i] = item;
                i++;
            }
        }
        return a.items;
    }

    public static void printArray(int[] items) {
        for (int item : items) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] arr = new int[] {5, 9, 2, 3};
        AList a = new AList(5);
        a.items = insert(arr, 6, 2);
        a.items = insertSol(a.items, 7, 8);
        printArray(a.items);
        reverse(a.items);
        printArray(a.items);
        //a.items = replicate(arr);
        a.items = replicateSol(arr);
        printArray(a.items);
    }
}