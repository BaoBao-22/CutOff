import os
import sys
if sys.stdout.encoding != 'utf-8':
    sys.stdout.reconfigure(encoding='utf-8')
if sys.stderr.encoding != 'utf-8':
    sys.stderr.reconfigure(encoding='utf-8')
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

# Thiết lập phong cách đồ thị học thuật
plt.style.use('seaborn-v0_8-whitegrid' if 'seaborn-v0_8-whitegrid' in plt.style.available else 'default')
plt.rcParams['font.sans-serif'] = 'DejaVu Sans'
plt.rcParams['axes.edgecolor'] = '#333333'
plt.rcParams['axes.linewidth'] = 0.8

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DATA_DIR = os.path.join(BASE_DIR, "data")
PLOTS_DIR = os.path.join(BASE_DIR, "plots")
os.makedirs(PLOTS_DIR, exist_ok=True)

summary_file = os.path.join(DATA_DIR, "summary_statistics.csv")
raw_file = os.path.join(DATA_DIR, "raw_results.csv")

if not os.path.exists(summary_file):
    print(f"File {summary_file} không tồn tại! Vui lòng chạy BenchmarkRunner trước.")
    exit(1)

df = pd.read_csv(summary_file)
print("Đã nạp dữ liệu thống kê thành công. Số bản ghi:", len(df))

# -------------------------------------------------------------
# 1. Đồ thị: U-Shape Curve (CUTOFF vs Thời gian thực thi)
# -------------------------------------------------------------
fig, ax = plt.subplots(figsize=(10, 6), dpi=300)
df_random = df[df['distribution'] == 'RANDOM_UNIFORM']
sizes_to_plot = [100000, 250000, 500000, 1000000]
colors = ['#2ca02c', '#1f77b4', '#ff7f0e', '#d62728']

for idx, n in enumerate(sizes_to_plot):
    sub = df_random[df_random['n'] == n].sort_values('cutoff')
    if sub.empty:
        continue
    ax.plot(sub['cutoff'], sub['median_ms'], marker='o', label=f'N = {n:,}', color=colors[idx], linewidth=1.8, markersize=5)
    
    # Đánh dấu điểm tối ưu (min median_ms)
    min_row = sub.loc[sub['median_ms'].idxmin()]
    ax.scatter(min_row['cutoff'], min_row['median_ms'], color='black', s=60, zorder=5)
    ax.annotate(f"Opt: C={int(min_row['cutoff'])}", 
                (min_row['cutoff'], min_row['median_ms']),
                textcoords="offset points", xytext=(0, 10), ha='center', fontsize=8, weight='bold')

# Đánh dấu mốc 7 và 10
ax.axvline(x=7, color='purple', linestyle='--', alpha=0.6, label='Cutoff = 7 (Sách cũ / Java 6)')
ax.axvline(x=10, color='blue', linestyle=':', alpha=0.7, label='Cutoff = 10 (Sách mới)')

ax.set_title("Mergesort with Cutoff: Execution Time vs. Cutoff Value\n(Random Uniform Distribution)", fontsize=13, weight='bold', pad=12)
ax.set_xlabel("Cutoff Value (Threshold to Insertion Sort)", fontsize=11)
ax.set_ylabel("Median Running Time (ms)", fontsize=11)
ax.set_xticks(sorted(df_random['cutoff'].unique()))
ax.legend(frameon=True, facecolor='white', framealpha=0.9)
plt.tight_layout()
plot1_path = os.path.join(PLOTS_DIR, "cutoff_vs_time_random.png")
plt.savefig(plot1_path)
plt.close()
print(f"Đã lưu biểu đồ 1: {plot1_path}")

# -------------------------------------------------------------
# 2. Đồ thị: Speedup (%) so với Baseline Mergesort gốc (CUTOFF = 1)
# -------------------------------------------------------------
fig, ax = plt.subplots(figsize=(10, 6), dpi=300)

for idx, n in enumerate(sizes_to_plot):
    sub = df_random[df_random['n'] == n].sort_values('cutoff')
    if sub.empty:
        continue
    t_base = sub[sub['cutoff'] == 1]['median_ms'].values[0]
    speedup = ((t_base - sub['median_ms']) / t_base) * 100
    ax.plot(sub['cutoff'], speedup, marker='s', label=f'N = {n:,}', color=colors[idx], linewidth=1.8, markersize=5)

ax.axhline(0, color='gray', linestyle='--', alpha=0.5)
ax.axvline(x=7, color='purple', linestyle='--', alpha=0.6, label='Cutoff = 7')
ax.axvline(x=10, color='blue', linestyle=':', alpha=0.7, label='Cutoff = 10')

ax.set_title("Performance Improvement (% Speedup) relative to Pure Mergesort (Cutoff=1)", fontsize=13, weight='bold', pad=12)
ax.set_xlabel("Cutoff Value", fontsize=11)
ax.set_ylabel("Speedup (%) [Higher is better]", fontsize=11)
ax.set_xticks(sorted(df_random['cutoff'].unique()))
ax.legend(frameon=True, facecolor='white', framealpha=0.9)
plt.tight_layout()
plot2_path = os.path.join(PLOTS_DIR, "speedup_comparison.png")
plt.savefig(plot2_path)
plt.close()
print(f"Đã lưu biểu đồ 2: {plot2_path}")

# -------------------------------------------------------------
# 3. Đồ thị: So sánh hành vi trên 4 dạng phân bố dữ liệu (N = 500,000)
# -------------------------------------------------------------
fig, ax = plt.subplots(figsize=(10, 6), dpi=300)
n_dist_target = 500000 if 500000 in df['n'].values else df['n'].max()

dist_colors = {
    'RANDOM_UNIFORM': '#1f77b4',
    'NEARLY_SORTED': '#2ca02c',
    'REVERSE_SORTED': '#d62728',
    'FEW_UNIQUE': '#9467bd'
}

dist_labels = {
    'RANDOM_UNIFORM': 'Ngẫu nhiên đều (Random Uniform)',
    'NEARLY_SORTED': 'Gần như đã sắp xếp (Nearly Sorted - 3% xáo trộn)',
    'REVERSE_SORTED': 'Nghịch đảo hoàn toàn (Reverse Sorted)',
    'FEW_UNIQUE': 'Nhiều khóa trùng lặp (Few Unique - 10 keys)'
}

for dist in df['distribution'].unique():
    sub = df[(df['distribution'] == dist) & (df['n'] == n_dist_target)].sort_values('cutoff')
    if sub.empty:
        continue
    ax.plot(sub['cutoff'], sub['median_ms'], marker='^', label=dist_labels.get(dist, dist), 
            color=dist_colors.get(dist, '#333333'), linewidth=1.8, markersize=5)

ax.set_title(f"Cutoff Sensitivity across Data Distributions (N = {n_dist_target:,})", fontsize=13, weight='bold', pad=12)
ax.set_xlabel("Cutoff Value", fontsize=11)
ax.set_ylabel("Median Running Time (ms)", fontsize=11)
ax.set_xticks(sorted(df['cutoff'].unique()))
ax.legend(frameon=True, facecolor='white', framealpha=0.9)
plt.tight_layout()
plot3_path = os.path.join(PLOTS_DIR, "distribution_sensitivity.png")
plt.savefig(plot3_path)
plt.close()
print(f"Đã lưu biểu đồ 3: {plot3_path}")

# -------------------------------------------------------------
# 4. Đồ thị Log-Log Plot theo chủ đề 1.4 Analysis of Algorithms
# -------------------------------------------------------------
fig, ax = plt.subplots(figsize=(9, 6), dpi=300)
cutoffs_to_show = [1, 7, 10, 16]
markers = ['o', 's', '^', 'D']

for i, c in enumerate(cutoffs_to_show):
    sub = df_random[df_random['cutoff'] == c].sort_values('n')
    if sub.empty:
        continue
    ax.plot(sub['n'], sub['median_ms'], marker=markers[i], label=f'Cutoff = {c}', linewidth=1.8)

ax.set_xscale('log')
ax.set_yscale('log')
ax.set_title("Log-Log Plot: Running Time T(N) vs. Input Size N\n(Tilde Model Verification: O(N log N))", fontsize=13, weight='bold', pad=12)
ax.set_xlabel("Input Size N (log scale)", fontsize=11)
ax.set_ylabel("Median Time (ms) (log scale)", fontsize=11)
ax.legend(frameon=True, facecolor='white', framealpha=0.9)
plt.tight_layout()
plot4_path = os.path.join(PLOTS_DIR, "loglog_growth_plot.png")
plt.savefig(plot4_path)
plt.close()
print(f"Đã lưu biểu đồ 4: {plot4_path}")

# -------------------------------------------------------------
# 5. Đồ thị: Theoretical Cost Model (Compares & Array Accesses) vs Empirical
# -------------------------------------------------------------
fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(14, 5.5), dpi=300)
sub_cost = df_random[df_random['n'] == 100000].sort_values('cutoff')

ax1.plot(sub_cost['cutoff'], sub_cost['compares'] / 1e6, marker='o', color='#e377c2', linewidth=1.8)
ax1.set_title("Number of Key Compares (N = 100,000)", fontsize=12, weight='bold')
ax1.set_xlabel("Cutoff Value", fontsize=10)
ax1.set_ylabel("Compares (Millions)", fontsize=10)
ax1.axvline(x=7, color='purple', linestyle='--', alpha=0.5, label='C=7')
ax1.axvline(x=10, color='blue', linestyle=':', alpha=0.5, label='C=10')
ax1.legend()

ax2.plot(sub_cost['cutoff'], sub_cost['array_accesses'] / 1e6, marker='s', color='#17becf', linewidth=1.8)
ax2.set_title("Number of Array Accesses (N = 100,000)", fontsize=12, weight='bold')
ax2.set_xlabel("Cutoff Value", fontsize=10)
ax2.set_ylabel("Array Accesses (Millions)", fontsize=10)
ax2.axvline(x=7, color='purple', linestyle='--', alpha=0.5, label='C=7')
ax2.axvline(x=10, color='blue', linestyle=':', alpha=0.5, label='C=10')
ax2.legend()

plt.tight_layout()
plot5_path = os.path.join(PLOTS_DIR, "theoretical_cost_model.png")
plt.savefig(plot5_path)
plt.close()
print(f"Đã lưu biểu đồ 5: {plot5_path}")

# -------------------------------------------------------------
# 6. Xuất bảng Markdown tổng hợp so sánh các mốc Cutoff quan trọng
# -------------------------------------------------------------
table_md_path = os.path.join(DATA_DIR, "comparison_table.md")
with open(table_md_path, "w", encoding="utf-8") as f:
    f.write("# BẢNG TỔNG HỢP SO SÁNH HIỆU NĂNG CÁC MỐC CUTOFF QUAN TRỌNG\n\n")
    f.write("| Phân bố | N | T(C=1) ms | T(C=7) ms | T(C=10) ms | T(C_opt) ms | C_opt | Cải thiện C=10 vs C=1 | Cải thiện C_opt vs C=1 |\n")
    f.write("| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |\n")
    
    for dist in df['distribution'].unique():
        for n in df[df['distribution'] == dist]['n'].unique():
            sub = df[(df['distribution'] == dist) & (df['n'] == n)]
            t_base = sub[sub['cutoff'] == 1]['median_ms'].values[0]
            t_7 = sub[sub['cutoff'] == 7]['median_ms'].values[0]
            t_10 = sub[sub['cutoff'] == 10]['median_ms'].values[0]
            min_row = sub.loc[sub['median_ms'].idxmin()]
            c_opt = int(min_row['cutoff'])
            t_opt = min_row['median_ms']
            
            gain_10 = ((t_base - t_10) / t_base) * 100
            gain_opt = ((t_base - t_opt) / t_base) * 100
            
            f.write(f"| {dist} | {n:,} | {t_base:.2f} | {t_7:.2f} | {t_10:.2f} | {t_opt:.2f} | **{c_opt}** | +{gain_10:.1f}% | **+{gain_opt:.1f}%** |\n")

print(f"Đã xuất bảng so sánh Markdown tại: {table_md_path}")
print("Tất cả tác vụ xử lý số liệu và trực quan hóa hoàn tất!")
