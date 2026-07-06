# PowerShell script to compile and run AI Quiz Generator
Write-Host "[BUILD] Compiling AI Quiz Generator..." -ForegroundColor Cyan

$outDir = "target\classes"
if (-not (Test-Path $outDir)) {
    New-Item -ItemType Directory -Force -Path $outDir | Out-Null
}

$sources = Get-ChildItem -Path "src" -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d $outDir $sources

if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Compilation failed!" -ForegroundColor Red
    exit $LASTEXITCODE
}

Write-Host "[START] Launching AI Quiz Generator..." -ForegroundColor Green
Write-Host ""
java -cp $outDir com.aiquiz.Main
