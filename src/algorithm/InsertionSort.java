package algorithm;

/**
 * Cài đặt Insertion Sort trên đoạn mảng con [lo, hi]
 * Theo đúng nội dung bài giảng DSA (Sedgewick & Wayne - Algorithms 4th Edition)
 */
public class InsertionSort {

    // Không cho phép khởi tạo đối tượng tiện ích
    private InsertionSort() {}

    /**
     * Sắp xếp mảng con a[lo..hi] bằng Insertion Sort
     * Trích xuất trực tiếp từ slide bài giảng Mergesort & Elementary Sorts
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void sort(Comparable[] a, int lo, int hi) {
        for (int i = lo; i <= hi; i++) {
            for (int j = i; j > lo && less(a[j], a[j - 1]); j--) {
                exch(a, j, j - 1);
            }
        }
    }

    /**
     * Sắp xếp toàn bộ mảng a
     */
    @SuppressWarnings("rawtypes")
    public static void sort(Comparable[] a) {
        sort(a, 0, a.length - 1);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    @SuppressWarnings("rawtypes")
    private static void exch(Comparable[] a, int i, int j) {
        Comparable swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }
}
