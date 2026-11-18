package test;

import algorithm.InsertionSort;
import algorithm.MergeSortCutoff;

import java.util.Arrays;
import java.util.Random;

/**
 * Kiểm thử tính đúng đắn (Correctness) và tính ổn định (Stability) của Mergesort cải tiến CUTOFF.
 */
public class CorrectnessTest {

    // Lớp đối tượng dùng để kiểm thử tính ổn định (Stability)
    static class Item implements Comparable<Item> {
        final int key;
        final int originalIndex;

        Item(int key, int originalIndex) {
            this.key = key;
            this.originalIndex = originalIndex;
        }

        @Override
        public int compareTo(Item o) {
            return Integer.compare(this.key, o.key);
        }

        @Override
        public String toString() {
            return "(" + key + ", idx=" + originalIndex + ")";
        }
    }

    public static void main(String[] args) {
        System.out.println("=== BẮT ĐẦU KIỂM THỬ TÍNH ĐÚNG ĐẮN VÀ ỔN ĐỊNH ===");
        testEmptyAndSingle();
        testRandomArrays();
        testStability();
        System.out.println(">>> TẤT CẢ CÁC BÀI TEST ĐỀU THÀNH CÔNG VƯỢT QUA 100%!");
    }

    private static void testEmptyAndSingle() {
        Integer[] empty = new Integer[0];
        MergeSortCutoff.sort(empty, 7);
        if (empty.length != 0) throw new AssertionError("Lỗi với mảng rỗng");

        Integer[] single = new Integer[]{42};
        MergeSortCutoff.sort(single, 7);
        if (single[0] != 42) throw new AssertionError("Lỗi với mảng 1 phần tử");

        System.out.println("[PASS] Mảng rỗng và mảng 1 phần tử.");
    }

    private static void testRandomArrays() {
        Random rng = new Random(42);
        int[] cutoffs = {1, 2, 5, 7, 10, 16, 32, 64};
        int[] sizes = {5, 10, 20, 50, 100, 1000, 50000};

        for (int n : sizes) {
            Integer[] base = new Integer[n];
            for (int i = 0; i < n; i++) {
                base[i] = rng.nextInt(100000);
            }

            Integer[] expected = base.clone();
            Arrays.sort(expected);

            for (int c : cutoffs) {
                Integer[] copy = base.clone();
                MergeSortCutoff.sort(copy, c);
                if (!Arrays.equals(copy, expected)) {
                    throw new AssertionError("Sai kết quả với N = " + n + ", CUTOFF = " + c);
                }
            }
        }
        System.out.println("[PASS] Sắp xếp mảng ngẫu nhiên với các CUTOFF từ 1 đến 64.");
    }

    private static void testStability() {
        // Kiểm tra tính ổn định: Giữ nguyên thứ tự tương đối của các phần tử có cùng key
        int n = 1000;
        Item[] items = new Item[n];
        Random rng = new Random(12345);
        for (int i = 0; i < n; i++) {
            items[i] = new Item(rng.nextInt(10), i); // chỉ có 10 khóa khác nhau
        }

        for (int cutoff : new int[]{1, 5, 7, 10, 16, 32}) {
            Item[] copy = items.clone();
            MergeSortCutoff.sort(copy, cutoff);

            // Kiểm tra tính tăng dần của key và tính ổn định của originalIndex
            for (int i = 1; i < n; i++) {
                if (copy[i].key < copy[i - 1].key) {
                    throw new AssertionError("Không tăng dần tại CUTOFF = " + cutoff);
                }
                if (copy[i].key == copy[i - 1].key) {
                    if (copy[i].originalIndex <= copy[i - 1].originalIndex) {
                        throw new AssertionError("Vi phạm tính ổn định (Stability) tại CUTOFF = " + cutoff);
                    }
                }
            }
        }
        System.out.println("[PASS] Xác nhận thuật toán bảo toàn tính ổn định (Stable Sort).");
    }
}
