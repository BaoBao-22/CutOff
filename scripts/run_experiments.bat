@echo off
setlocal
cd /d "%~dp0\.."
chcp 65001 > nul
set PYTHONIOENCODING=utf-8
set PYTHONUTF8=1

echo ===============================================================
echo   CHUONG TRINH TU DONG THUC NGHIEM MERGESORT CUTOFF
echo ===============================================================
echo.

if not exist "bin" mkdir bin

echo [1/4] Bien dich toan bo ma nguon Java...
javac -encoding UTF-8 -d bin src\algorithm\*.java src\benchmark\*.java src\test\*.java
if errorlevel 1 goto BUILD_ERROR

echo [2/4] Kiem tra tinh dung dan va on dinh...
java -cp bin test.CorrectnessTest
if errorlevel 1 goto TEST_ERROR

echo [3/4] Tien hanh do dac thuc nghiem (BenchmarkRunner)...
java -Xmx4g -cp bin benchmark.BenchmarkRunner
if errorlevel 1 goto BENCH_ERROR

echo [4/4] Phan tich so lieu va xuat do thi bieu dien (Python)...
python scripts\analyze_and_plot.py
if errorlevel 1 goto PY_ERROR
goto END

:BUILD_ERROR
echo [LOI] Bien dich Java that bai!
pause
exit /b 1

:TEST_ERROR
echo [LOI] Unit test that bai!
pause
exit /b 1

:BENCH_ERROR
echo [LOI] Chay BenchmarkRunner that bai!
pause
exit /b 1

:PY_ERROR
echo [LOI] Xu ly so lieu Python that bai!
pause
exit /b 1

:END
echo.
echo ===============================================================
echo   TOAN BO QUA TRINH THUC NGHIEM DA HOAN TAT THANH CONG!
echo ===============================================================
pause