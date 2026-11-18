# NHẬT KÝ THẢO LUẬN VỚI AI (AI INTERACTION LOG)

**Đề tài:** Nghiên cứu & Thực nghiệm xác định ngưỡng CUTOFF tối ưu cho Mergesort  
**Trường:** Trường Đại học Công nghệ - Đại học Quốc gia Hà Nội (UET-VNU)  
**Môn học:** Cấu trúc Dữ liệu và Giải thuật (DSA)  
**Sinh viên thực hiện:** Nguyễn Kim Đạt - MSSV: 25023210  
**Tài liệu tham chiếu:** Algorithms 4th Edition (Sedgewick & Wayne)  

---

## 1. MỤC ĐÍCH SỬ DỤNG AI THEO QUY ĐỊNH CỦA ĐỀ BÀI
Theo quy định của đề bài:
> *"Bạn được dùng AI để thảo luận, viết script chạy thí nghiệm, xây dựng bộ dữ liệu test. Bạn cần tự mình dựng cài đặt mergesort từ nội dung bài giảng, không dùng các cài đặt khác. Xem lại chủ đề 1.3/1.4 Algorithm Analysis để ôn tập về thí nghiệm và thống kê kết quả thí nghiệm."*

Toàn bộ quá trình sử dụng AI được ghi lại minh bạch kèm mốc thời gian nhằm đảm bảo tính trung thực học thuật.

---

## 2. TÓM TẮT MỐC THỜI GIAN TRAO ĐỔI VỚI AI

| Phiên | Ngày | Thời gian (GMT+7) | Nội dung chính |
| :---: | :---: | :---: | :--- |
| **Phiên 1** | 04/11/2026 | 16:50 – 16:59 | Phân tích đề bài, lý thuyết CUTOFF 7 vs 10 và lên kế hoạch thực nghiệm 1.3/1.4 |
| **Phiên 2** | 04/11/2026 | 16:59 – 17:05 | Dựng cấu trúc mã nguồn chuẩn bài giảng, unit test và script đo tự động |
| **Phiên 3** | 04/11/2026 | 17:11 – 17:45 | Sửa lỗi chạy script `.bat`, tạo công cụ đo tương tác và làm rõ yêu cầu cần nộp |
| **Phiên 4** | 10/11/2026 | 12:41 | Rà soát từng hàm trong `MergeSortCutoff.java`, `InsertionSort.java` và hướng dẫn tự đo |
| **Phiên 5** | 18/11/2026 | 16:07 – 17:13 | Phân tích số liệu thực tế 16 phiên đo, cập nhật báo cáo hoàn chỉnh và đẩy lên GitHub |

---

## 3. NỘI DUNG TRAO ĐỔI VÀ ĐÓNG GÓP CHI TIẾT

### Phiên 1: Lên kế hoạch và định hình phương pháp luận thực nghiệm
- **Thời gian:** `04/11/2026 | 16:50 – 16:59 (GMT+7)`
- **Hỏi AI:** Phân tích yêu cầu đề tài, đối chiếu hai tài liệu bài giảng `14AnalysisOfAlgorithms.pdf` và `22Mergesort.pdf`. Tại sao giáo trình cũ dùng CUTOFF = 7 còn giáo trình mới dùng CUTOFF = 10?
- **AI tư vấn:**
  - Giải thích bản chất suy giảm hiệu năng của Mergesort thuần trên mảng con ngắn: overhead lời gọi đệ quy và 2 lượt duyệt mảng phụ `aux[]`.
  - Phân tích cơ chế của Insertion sort trên mảng ngắn: tận dụng tính cục bộ của dữ liệu (cache locality), không tốn mảng phụ, sắp xếp tại chỗ.
  - Xây dựng kế hoạch thực nghiệm tuân thủ phương pháp khoa học (Scientific Method của Knuth/Sedgewick): kiểm soát biến ngoại lai (JIT warmup, GC pause, đo bằng `System.nanoTime()`, lặp nhiều lần lấy Median).

### Phiên 2: Xây dựng mã nguồn thuật toán chuẩn bài giảng và script đo
- **Thời gian:** `04/11/2026 | 16:59 – 17:05 (GMT+7)`
- **Sinh viên & AI thảo luận:** Cài đặt Mergesort và Insertion sort chuẩn từng dòng theo Slide 7, 8, 18, 52 của môn học (`InsertionSort.java`, `MergeSortCutoff.java`, `MergeSortTracked.java`).
- **Cam kết kỹ thuật:** Không dùng mã nguồn thư viện bên ngoài hay các biến thể tối ưu lai phức tạp không có trong bài giảng.
- **Xây dựng kiểm thử & bộ đo:** Viết unit test `CorrectnessTest.java` xác minh tính đúng đắn trên mảng ngẫu nhiên và kiểm tra **tính ổn định (Stability)** khi ghép CUTOFF; xây dựng `DataGenerator.java`, `BenchmarkRunner.java`, `scripts/run_experiments.bat` và `scripts/analyze_and_plot.py`.

### Phiên 3: Chạy thử nghiệm, thiết kế công cụ tương tác và làm rõ mục tiêu bài toán
- **Thời gian:** `04/11/2026 | 17:11 – 17:45 (GMT+7)`
- **Yêu cầu của sinh viên:** Chạy script thực nghiệm `scripts/run_experiments.bat`; muốn trực tiếp chạy test trên máy với nhiều giá trị $N$ và CUTOFF khác nhau; làm rõ mục tiêu và quyết định khoa học cần đưa ra theo đề bài.
- **AI hỗ trợ:**
  - Khắc phục lỗi mã hóa UTF-8 (`PYTHONIOENCODING`) và định dạng dòng CRLF khi chạy batch file trên Windows.
  - Viết chương trình tương tác menu `InteractiveBenchmark.java`, file batch `CHAY_THU_NGHIEM_CUA_BAN.bat` và script `plot_my_results.py` (dùng nội bộ trên máy cá nhân để tự chạy thử nghiệm).
  - Giải thích trực quan ý nghĩa đồ thị chữ U, đối chiếu 4 hạng mục sản phẩm cần nộp và cung cấp khung báo cáo khoa học ban đầu.

### Phiên 4: Rà soát mã nguồn cốt lõi theo Slide và hướng dẫn tự thực nghiệm
- **Thời gian:** `10/11/2026 | 12:41 (GMT+7)`
- **Yêu cầu của sinh viên:** Nhờ hướng dẫn tự kiểm chứng lại phần cài đặt cốt lõi theo bài giảng và quy trình tự đo, vẽ biểu đồ trên máy cá nhân.
- **AI hỗ trợ:**
  - Rà soát và giải thích chi tiết từng phương thức trong `InsertionSort.java` (Slide 52) và `MergeSortCutoff.java` (Slide 7, 8, 18), đặc biệt là quy tắc bảo toàn tính ổn định và cấp phát mảng phụ `aux[]` duy nhất 1 lần.
  - Hướng dẫn tự chạy kiểm thử `CorrectnessTest`, tự chạy `CHAY_THU_NGHIEM_CUA_BAN.bat` và xuất đồ thị từ số liệu thật để thay cho số liệu mẫu trong báo cáo.

### Phiên 5: Phân tích số liệu thực tế mới nhất, hoàn thiện báo cáo và đẩy lên GitHub
- **Thời gian:** `18/11/2026 | 16:07 – 17:13 (GMT+7)`
- **Hoạt động của sinh viên:** Tự thực hiện thêm 14 phiên đo toàn dải trên máy cá nhân với nhiều kích thước mảng ($N = 50.000$ đến $N = 8.989.999$), xuất biểu đồ mới nhất (`plots/ket_qua_may_cua_ban.png`), điền thông tin sinh viên và yêu cầu cập nhật báo cáo cùng đẩy bài nộp lên GitHub.
- **AI hỗ trợ:**
  - Tổng hợp thống kê từ toàn bộ 16 phiên đo trong `data/my_machine_results.csv`, đối chiếu với mô hình chi phí trong `data/summary_statistics.csv` và bảng đa phân bố `data/comparison_table.md`.
  - Cập nhật toàn bộ bảng số liệu thật, biểu đồ và phân tích chuyên sâu (bao gồm hiệu ứng bậc thang của cây nhị phân và biến ngoại lai P-Core/E-Core trên Intel Core i5-12500H) vào `report/Bao_Cao_Thuc_Nghiem_Mergesort_Cutoff.md`.
  - Cấu hình `.gitignore` (loại trừ các file chạy thử tương tác cá nhân và thư mục `bin/`), kiểm thử lại mã nguồn, bổ sung mốc thời gian nhật ký AI và đẩy sản phẩm lên repository GitHub (`https://github.com/BaoBao-22/CutOff`).
