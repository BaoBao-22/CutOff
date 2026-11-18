package benchmark;

import java.util.Random;

/**
 * Bộ sinh dữ liệu thử nghiệm chuẩn khoa học, có khả năng tái lập (Reproducible) với seed cố định.
 */
public class DataGenerator {

    public enum Distribution {
        RANDOM_UNIFORM,
        NEARLY_SORTED,
        REVERSE_SORTED,
        FEW_UNIQUE
    }

    /**
     * Sinh mảng Integer[] theo kích thước n và phân bố chỉ định.
     */
    public static Integer[] generate(int n, Distribution dist, long seed) {
        Integer[] a = new Integer[n];
        Random rng = new Random(seed);

        switch (dist) {
            case RANDOM_UNIFORM:
                for (int i = 0; i < n; i++) {
                    a[i] = rng.nextInt(n * 10);
                }
                break;

            case NEARLY_SORTED:
                // Mảng tăng dần ban đầu
                for (int i = 0; i < n; i++) {
                    a[i] = i;
                }
                // Xáo trộn khoảng 3% số cặp phần tử
                int swaps = Math.max(1, (int) (n * 0.03));
                for (int s = 0; s < swaps; s++) {
                    int i = rng.nextInt(n);
                    int j = rng.nextInt(n);
                    Integer tmp = a[i];
                    a[i] = a[j];
                    a[j] = tmp;
                }
                break;

            case REVERSE_SORTED:
                // Mảng giảm dần hoàn toàn
                for (int i = 0; i < n; i++) {
                    a[i] = n - i;
                }
                break;

            case FEW_UNIQUE:
                // Chỉ chứa 10 giá trị phân biệt từ 0 đến 9
                for (int i = 0; i < n; i++) {
                    a[i] = rng.nextInt(10);
                }
                break;
        }

        return a;
    }
}
