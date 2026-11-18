package algorithm;

/**
 * Phiên bản Mergesort hỗ trợ đếm chính xác số phép so sánh (Compares) 
 * và số lần truy cập mảng (Array Accesses) phục vụ phân tích theo Cost Model (Slide 16, 32 chủ đề 1.4).
 */
public class MergeSortTracked {

    public static class Metrics {
        public long compares = 0;
        public long arrayAccesses = 0;

        public void reset() {
            compares = 0;
            arrayAccesses = 0;
        }
    }

    private MergeSortTracked() {}

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi, Metrics metrics) {
        // Sao chép sang mảng phụ aux[]: 2 * (hi - lo + 1) truy cập mảng (đọc a, ghi aux)
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
            metrics.arrayAccesses += 2;
        }

        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = aux[j++];
                metrics.arrayAccesses += 2;
            } else if (j > hi) {
                a[k] = aux[i++];
                metrics.arrayAccesses += 2;
            } else {
                metrics.compares++;
                metrics.arrayAccesses += 2; // đọc aux[j] và aux[i]
                if (aux[j].compareTo(aux[i]) < 0) {
                    a[k] = aux[j++];
                    metrics.arrayAccesses += 2; // đọc aux[j], ghi a[k]
                } else {
                    a[k] = aux[i++];
                    metrics.arrayAccesses += 2; // đọc aux[i], ghi a[k]
                }
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void insertionSort(Comparable[] a, int lo, int hi, Metrics metrics) {
        for (int i = lo; i <= hi; i++) {
            for (int j = i; j > lo; j--) {
                metrics.compares++;
                metrics.arrayAccesses += 2; // so sánh a[j] và a[j-1]
                if (a[j].compareTo(a[j - 1]) < 0) {
                    // swap a[j] và a[j-1]: 4 array accesses (2 reads, 2 writes)
                    Comparable swap = a[j];
                    a[j] = a[j - 1];
                    a[j - 1] = swap;
                    metrics.arrayAccesses += 4;
                } else {
                    break;
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi, int cutoff, Metrics metrics) {
        if (hi <= lo + cutoff - 1) {
            insertionSort(a, lo, hi, metrics);
            return;
        }

        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid, cutoff, metrics);
        sort(a, aux, mid + 1, hi, cutoff, metrics);
        merge(a, aux, lo, mid, hi, metrics);
    }

    @SuppressWarnings("rawtypes")
    public static Metrics sort(Comparable[] a, int cutoff) {
        Metrics metrics = new Metrics();
        if (a == null || a.length <= 1) return metrics;
        if (cutoff < 1) cutoff = 1;
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length - 1, cutoff, metrics);
        return metrics;
    }
}
