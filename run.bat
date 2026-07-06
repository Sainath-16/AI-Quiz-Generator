@echo off
echo [BUILD] Compiling AI Quiz Generator...
if not exist target\classes mkdir target\classes

REM Find all .java files and compile them
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -d target\classes @sources.txt
if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed!
    del sources.txt
    pause
    exit /b %errorlevel%
)
del sources.txt

echo [START] Launching AI Quiz Generator...
echo.
java -cp target\classes com.aiquiz.Main
