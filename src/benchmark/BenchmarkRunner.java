package benchmark;

import algorithm.MergeSortCutoff;
import algorithm.MergeSortTracked;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Bộ điều khiển thực nghiệm đo đạc khoa học theo chuẩn 1.4 Analysis of Algorithms.
 * Bao gồm JVM Warmup, Garbage Collection filter, lặp lại đo nhiều lần và tính Median/Mean/Std.
 */
public class BenchmarkRunner {

    private static final int WARMUP_ITERATIONS = 8;
    private static final int REPETITIONS = 10;

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println(" BẮT ĐẦU CHƯƠNG TRÌNH THỰC NGHIỆM ĐO ĐẠC MERGESORT CUTOFF");
        System.out.println(" Môi trường: Java " + System.getProperty("java.version") 
                           + " (" + System.getProperty("os.name") + " " + System.getProperty("os.arch") + ")");
        System.out.println("===============================================================\n");

        // 1. Chạy JVM Warm-up
        performWarmup();

        // 2. Không gian tham số thực nghiệm
        // Kích thước mảng N
        int[] sizes = {10_000, 50_000, 100_000, 250_000, 500_000, 1_000_000};
        
        // Dải CUTOFF khảo sát chi tiết
        int[] cutoffs = {1, 2, 4, 6, 7, 8, 9, 10, 11, 12, 14, 16, 20, 24, 32, 48, 64, 96, 128};

        // Các dạng phân bố
        DataGenerator.Distribution[] distributions = {
            DataGenerator.Distribution.RANDOM_UNIFORM,
            DataGenerator.Distribution.NEARLY_SORTED,
            DataGenerator.Distribution.REVERSE_SORTED,
            DataGenerator.Distribution.FEW_UNIQUE
        };

        String rawCsvPath = "data/raw_results.csv";
        String summaryCsvPath = "data/summary_statistics.csv";

        try (PrintWriter rawOut = new PrintWriter(new FileWriter(rawCsvPath));
             PrintWriter sumOut = new PrintWriter(new FileWriter(summaryCsvPath))) {

            // Header cho CSV
            rawOut.println("distribution,n,cutoff,trial,time_ms");
            sumOut.println("distribution,n,cutoff,runs,mean_ms,median_ms,std_ms,min_ms,max_ms,compares,array_accesses");

            long totalCases = (long) distributions.length * sizes.length * cutoffs.length;
            long currentCase = 0;

            for (DataGenerator.Distribution dist : distributions) {
                System.out.println("\n-------------------------------------------------------------");
                System.out.println(">>> ĐANG KHẢO SÁT PHÂN BỐ DỮ LIỆU: " + dist);
                System.out.println("-------------------------------------------------------------");

                for (int n : sizes) {
                    System.out.printf("--> Tiến hành đo với N = %,d ...\n", n);

                    // Sinh dữ liệu gốc với seed cố định để đảm bảo tính tái lập (Reproducibility)
                    long seed = 20261104L + n;
                    Integer[] baseData = DataGenerator.generate(n, dist, seed);

                    for (int cutoff : cutoffs) {
                        currentCase++;
                        List<Double> times = new ArrayList<>();

                        // Chạy lặp lại REPETITIONS lần
                        for (int r = 1; r <= REPETITIONS; r++) {
                            Integer[] arr = baseData.clone();

                            // Dọn dẹp bộ nhớ trước mỗi lần đo để tránh GC pause
                            if (n >= 500_000) {
                                System.gc();
                                Thread.sleep(10);
                            }

                            long start = System.nanoTime();
                            MergeSortCutoff.sort(arr, cutoff);
                            long end = System.nanoTime();

                            double elapsedMs = (end - start) / 1_000_000.0;
                            times.add(elapsedMs);

                            // Ghi kết quả thô
                            rawOut.printf("%s,%d,%d,%d,%.4f\n", dist.name(), n, cutoff, r, elapsedMs);
                        }

                        // Đo số phép so sánh và truy cập mảng theo mô hình lý thuyết (1 lần chạy mẫu)
                        Integer[] trackArr = baseData.clone();
                        MergeSortTracked.Metrics metrics = MergeSortTracked.sort(trackArr, cutoff);

                        // Thống kê mô tả
                        times.sort(Double::compareTo);
                        double median = times.get(times.size() / 2);
                        double sum = 0.0;
                        for (double t : times) sum += t;
                        double mean = sum / times.size();

                        double sumSq = 0.0;
                        for (double t : times) sumSq += Math.pow(t - mean, 2);
                        double std = Math.sqrt(sumSq / times.size());
                        double min = times.get(0);
                        double max = times.get(times.size() - 1);

                        // Ghi kết quả tổng hợp
                        sumOut.printf("%s,%d,%d,%d,%.4f,%.4f,%.4f,%.4f,%.4f,%d,%d\n",
                                dist.name(), n, cutoff, REPETITIONS, mean, median, std, min, max,
                                metrics.compares, metrics.arrayAccesses);

                        if (currentCase % 20 == 0 || cutoff == 1 || cutoff == 7 || cutoff == 10) {
                            System.out.printf("    [Tiến độ %d/%d] Dist=%s | N=%d | Cutoff=%-3d | Median=%.3f ms | Mean=%.3f ms\n",
                                    currentCase, totalCases, dist.name(), n, cutoff, median, mean);
                        }
                    }
                    rawOut.flush();
                    sumOut.flush();
                }
            }

            System.out.println("\n>>> ĐÃ HOÀN THÀNH TOÀN BỘ THÍ NGHIỆM THỰC NGHIỆM THÀNH CÔNG!");
            System.out.println("Dữ liệu thô lưu tại: " + rawCsvPath);
            System.out.println("Dữ liệu tổng hợp lưu tại: " + summaryCsvPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * JVM Warm-up để HotSpot JIT Compiler tối ưu mã máy trước khi đo.
     */
    private static void performWarmup() {
        System.out.println("Đang thực hiện JVM Warm-up (Khởi động JIT compiler & nạp class)...");
        int warmupSize = 50_000;
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            Integer[] dummy = DataGenerator.generate(warmupSize, DataGenerator.Distribution.RANDOM_UNIFORM, 9999 + i);
            MergeSortCutoff.sort(dummy, 10);
            Integer[] dummy2 = DataGenerator.generate(warmupSize, DataGenerator.Distribution.RANDOM_UNIFORM, 8888 + i);
            MergeSortCutoff.sort(dummy2, 1);
        }
        System.out.println("JVM Warm-up hoàn tất!\n");
    }
}
