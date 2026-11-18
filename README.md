# DỰ ÁN THỰC NGHIỆM ĐO ĐẠC VÀ XÁC ĐỊNH NGƯỠNG CUTOFF TỐI ƯU CHO MERGESORT

Dự án thuộc môn học Cấu trúc dữ liệu và Giải thuật (DSA) - Trường Đại học Công nghệ - Đại học Quốc gia Hà Nội (UET-VNU) (chương trình chuẩn Algorithms 4th Edition - Sedgewick & Wayne).

---

## 1. MỤC TIÊU DỰ ÁN
- Xác định giá trị ngưỡng `CUTOFF` chuyển sang **Insertion Sort** tối ưu nhất đối với cấu hình máy tính cá nhân thực tế (Intel Core i5-12500H 12C/16T, 16GB RAM, Windows 11, JDK 25 HotSpot 64-bit).
- So sánh hiệu năng thực tế giữa:
  - Ngưỡng trong giáo trình cũ: `CUTOFF = 7` (chuẩn Java 6).
  - Ngưỡng trong giáo trình mới: `CUTOFF = 10`.
  - Mergesort thuần gốc: `CUTOFF = 1`.
  - Điểm cực tiểu thực tế đo được trên máy: $CUTOFF_{opt}$.
- Đánh giá trên 4 dạng phân bố dữ liệu:
  1. `RANDOM_UNIFORM`: Dữ liệu phân bố đều ngẫu nhiên.
  2. `NEARLY_SORTED`: Dữ liệu gần như đã sắp xếp (3% xáo trộn).
  3. `REVERSE_SORTED`: Dữ liệu nghịch đảo hoàn toàn.
  4. `FEW_UNIQUE`: Dữ liệu trùng lặp nhiều (chỉ gồm 10 khóa khác nhau).
- Ứng dụng phương pháp luận khoa học theo chuyên đề **1.3/1.4 Analysis of Algorithms**:
  - JVM Warm-up để HotSpot JIT biên dịch mã máy tối ưu.
  - Lọc nhiễu Garbage Collection (`System.gc()`).
  - Đo thời gian bằng `System.nanoTime()`.
  - Thực hiện 10 lần lặp độc lập cho mỗi điểm tham số và lấy trung vị (Median) và độ lệch chuẩn (Std Dev).

---

## 2. CẤU TRÚC DỰ ÁN

```
mergesort-cutoff-project/
├── src/
│   ├── algorithm/
│   │   ├── InsertionSort.java         # Thuật toán chèn trên mảng con [lo, hi]
│   │   ├── MergeSortCutoff.java       # Mergesort có ngưỡng cutoff tùy chỉnh
│   │   └── MergeSortTracked.java      # Phiên bản đếm số phép so sánh & truy cập mảng
│   ├── benchmark/
│   │   ├── BenchmarkRunner.java       # Bộ đo tự động chuẩn khoa học
│   │   └── DataGenerator.java         # Bộ sinh dữ liệu test có seed cố định
│   └── test/
│       └── CorrectnessTest.java       # Unit test tính đúng đắn & tính ổn định
├── scripts/
│   ├── run_experiments.bat            # Script chạy tự động trọn gói 1 click
│   └── analyze_and_plot.py            # Phân tích thống kê & vẽ đồ thị (Python)
├── data/
│   ├── raw_results.csv                # Dữ liệu thô từng lần lặp
│   ├── summary_statistics.csv         # Thống kê mô tả (Mean, Median, Std, Compares, ...)
│   └── comparison_table.md            # Bảng Markdown so sánh các mốc Cutoff
├── plots/                             # Các biểu đồ xuất bản độ nét cao
│   ├── cutoff_vs_time_random.png      # Đồ thị tìm CUTOFF tối ưu (đáy chữ U)
│   ├── speedup_comparison.png         # % Tăng tốc so với Mergesort gốc
│   ├── distribution_sensitivity.png   # Độ nhạy trên 4 phân bố
│   ├── loglog_growth_plot.png         # Đồ thị Log-Log xác thực O(N log N)
│   └── theoretical_cost_model.png     # Đồ thị mô hình chi phí lý thuyết
├── report/
│   └── Bao_Cao_Thuc_Nghiem_Mergesort_Cutoff.md
└── ai_logs/
    └── chat_interaction_log.md        # Minh bạch học thuật: Log thảo luận AI
```

---

## 3. HƯỚNG DẪN TÁI LẬP KẾT QUẢ (REPRODUCIBILITY)

### Yêu cầu môi trường
- **Java:** JDK 17+ (đã kiểm thử hoàn hảo trên Java 25 LTS).
- **Python:** 3.10+ (cần các thư viện: `numpy`, `pandas`, `matplotlib`).

### Cách 1: Chạy tự động (Khuyến nghị trên Windows)
Chỉ cần nhấp đúp hoặc chạy script:
```cmd
scripts\run_experiments.bat
```

### Cách 2: Chạy từng bước qua dòng lệnh

1. **Biên dịch mã nguồn Java:**
   ```cmd
   mkdir bin
   javac -encoding UTF-8 -d bin src/algorithm/*.java src/benchmark/*.java src/test/*.java
   ```

2. **Chạy kiểm thử đúng đắn & ổn định:**
   ```cmd
   java -cp bin test.CorrectnessTest
   ```

3. **Chạy bộ đo thực nghiệm thu thập số liệu:**
   ```cmd
   java -Xmx4g -cp bin benchmark.BenchmarkRunner
   ```

4. **Xử lý số liệu và xuất đồ thị:**
   ```cmd
   python scripts/analyze_and_plot.py
   ```

---

## 4. TÀI LIỆU THAM KHẢO
1. Robert Sedgewick & Kevin Wayne, *Algorithms 4th Edition*, Addison-Wesley, 2011.
2. Bài giảng Princeton COS 226 / DSA: Section 1.4 Analysis of Algorithms, Section 2.1 Elementary Sorts, Section 2.2 Mergesort.
