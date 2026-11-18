package algorithm;

/**
 * Cài đặt Mergesort kết hợp CUTOFF dùng Insertion sort cho mảng kích thước nhỏ.
 * Dựng trực tiếp từ slide bài giảng Mergesort (Sedgewick & Wayne - Algorithms 4th Edition).
 */
public class MergeSortCutoff {

    private MergeSortCutoff() {}

    /**
     * Hợp nhất hai nửa a[lo..mid] và a[mid+1..hi] dùng mảng phụ aux[]
     * Trích xuất nguyên bản từ Slide 7 bài giảng 2.2 Mergesort
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        // Sao chép sang mảng phụ aux[]
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }

        // Trộn hai nửa đã sắp xếp
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if      (i > mid)                   a[k] = aux[j++];
            else if (j > hi)                    a[k] = aux[i++];
            else if (less(aux[j], aux[i]))      a[k] = aux[j++];
            else                                a[k] = aux[i++];
        }
    }

    /**
     * Sắp xếp đệ quy mảng con a[lo..hi] với cải tiến CUTOFF
     * Trích xuất nguyên bản từ Slide 18 bài giảng 2.2 Mergesort
     */
    @SuppressWarnings("rawtypes")
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi, int cutoff) {
        // Cải tiến thực tế: Nếu kích thước mảng con <= cutoff, dùng Insertion Sort
        if (hi <= lo + cutoff - 1) {
            InsertionSort.sort(a, lo, hi);
            return;
        }

        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid, cutoff);
        sort(a, aux, mid + 1, hi, cutoff);
        merge(a, aux, lo, mid, hi);
    }

    /**
     * Điểm vào sắp xếp mảng a với ngưỡng CUTOFF tùy biến
     * Cấp phát mảng phụ aux duy nhất 1 lần (Slide 8 bài giảng)
     */
    @SuppressWarnings("rawtypes")
    public static void sort(Comparable[] a, int cutoff) {
        if (a == null || a.length <= 1) return;
        if (cutoff < 1) cutoff = 1;
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length - 1, cutoff);
    }

    /**
     * Sắp xếp theo phiên bản Mergesort gốc không cutoff (cutoff = 1)
     */
    @SuppressWarnings("rawtypes")
    public static void sort(Comparable[] a) {
        sort(a, 1);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }
}
