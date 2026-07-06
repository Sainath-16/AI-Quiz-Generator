@echo off
echo [BUILD] Compiling AI Quiz Generator...
if not exist "target\classes" mkdir "target\classes"

REM Compile all Java packages using relative paths (avoids folder space issues)
javac -encoding UTF-8 -d "target\classes" src\main\java\com\aiquiz\*.java src\main\java\com\aiquiz\model\*.java src\main\java\com\aiquiz\service\*.java src\main\java\com\aiquiz\util\*.java
if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %errorlevel%
)

echo [START] Launching AI Quiz Generator...
echo.
java -cp "target\classes" com.aiquiz.Main
