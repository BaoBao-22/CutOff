# NHẬT KÝ THẢO LUẬN VỚI AI (AI INTERACTION LOG)

**Đề tài:** Nghiên cứu & Thực nghiệm xác định ngưỡng CUTOFF tối ưu cho Mergesort  
**Môn học:** Cấu trúc Dữ liệu và Giải thuật (DSA)  
**Tài liệu tham chiếu:** Algorithms 4th Edition (Sedgewick & Wayne)  

---

## 1. MỤC ĐÍCH SỬ DỤNG AI THEO QUY ĐỊNH CỦA ĐỀ BÀI
Theo quy định của đề bài:
> *"Bạn được dùng AI để thảo luận, viết script chạy thí nghiệm, xây dựng bộ dữ liệu test. Bạn cần tự mình dựng cài đặt mergesort từ nội dung bài giảng, không dùng các cài đặt khác. Xem lại chủ đề 1.3/1.4 Algorithm Analysis để ôn tập về thí nghiệm và thống kê kết quả thí nghiệm."*

Toàn bộ quá trình sử dụng AI được ghi lại minh bạch nhằm đảm bảo tính trung thực học thuật.

---

## 2. NỘI DUNG TRAO ĐỔI VÀ ĐÓNG GÓP

### Phiên 1: Lên kế hoạch và định hình phương pháp luận thực nghiệm
- **Hỏi AI:** Phân tích yêu cầu đề tài, đối chiếu hai tài liệu bài giảng `14AnalysisOfAlgorithms.pdf` và `22Mergesort.pdf`. Tại sao giáo trình cũ dùng CUTOFF = 7 còn giáo trình mới dùng CUTOFF = 10?
- **AI tư vấn:**
  - Giải thích bản chất suy giảm hiệu năng của Mergesort thuần trên mảng con ngắn: overhead lời gọi đệ quy và 2 lượt duyệt mảng phụ `aux[]`.
  - Phân tích cơ chế của Insertion sort trên mảng ngắn: tận dụng tính cục bộ của dữ liệu (cache locality), không tốn mảng phụ, sắp xếp tại chỗ.
  - Xây dựng kế hoạch thực nghiệm 5 giai đoạn tuân thủ phương pháp khoa học (Scientific Method của Knuth/Sedgewick): kiểm soát biến ngoại lai (JIT warmup, GC pause, đo bằng nanoTime, lặp nhiều lần lấy Median).

### Phiên 2: Xây dựng mã nguồn thuật toán chuẩn bài giảng
- **Sinh viên & AI thảo luận:** Cài đặt Mergesort và Insertion sort chuẩn từng dòng theo Slide 7, 8, 18, 52 của môn học.
- **Cam kết kỹ thuật:** Không dùng mã nguồn thư viện bên ngoài hay các biến thể tối ưu lai phức tạp không có trong bài giảng.
- **Xây dựng kiểm thử:** Viết unit test xác minh tính đúng đắn trên mảng ngẫu nhiên và đặc biệt là kiểm tra **tính ổn định (Stability)** của thuật toán khi ghép CUTOFF.

### Phiên 3: Thiết kế công cụ tương tác cho sinh viên tự thực nghiệm
- **Yêu cầu của sinh viên:** Muốn trực tiếp chạy test trên máy, tự nhập kích thước dữ liệu $N$, tự quét các giá trị CUTOFF và tự chọn ra con số tối ưu cho cấu hình máy của mình.
- **AI hỗ trợ:**
  - Viết chương trình tương tác menu [`InteractiveBenchmark.java`](file:///d:/Đại%20học/DSA/mergesort-cutoff-project/src/benchmark/InteractiveBenchmark.java) và file batch [`CHAY_THU_NGHIEM_CUA_BAN.bat`](file:///d:/Đại%20học/DSA/mergesort-cutoff-project/CHAY_THU_NGHIEM_CUA_BAN.bat).
  - Viết script Python [`plot_my_results.py`](file:///d:/Đại%20học/DSA/mergesort-cutoff-project/scripts/plot_my_results.py) tự động vẽ biểu đồ trực quan hóa dữ liệu đo thực tế từ máy của sinh viên.
  - Cung cấp khung báo cáo khoa học đầy đủ để sinh viên điền số liệu thực nghiệm và giải thích hiện tượng.
