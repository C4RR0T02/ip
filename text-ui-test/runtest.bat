@ECHO OFF
setlocal enabledelayedexpansion

REM =================================================================
REM 1. COMPILE
REM =================================================================

REM create bin directory if it doesn't exist
if not exist ..\bin mkdir ..\bin

REM delete output from previous run
if exist ACTUAL.TXT del ACTUAL.TXT

REM compile the code into the bin folder
javac  -cp ..\src\main\java -Xlint:none -d ..\bin ..\src\main\java\*.java
IF ERRORLEVEL 1 (
    echo ********** BUILD FAILURE **********
    exit /b 1
)

REM =================================================================
REM 2. TEST LOOP
REM =================================================================

REM Define setup file
set "SETUP_FILE=tests\setup.txt"

REM Check if setup file exists
if not exist "%SETUP_FILE%" (
    echo Error: Setup file not found at %SETUP_FILE%
    exit /b 1
)

REM Loop through all test files in 'tests' folder starting with test_
for %%f in (tests\test_*.txt) do (
    set "test_file=%%f"

    REM A. Merge Setup + Test Case
    type "%SETUP_FILE%" "%%f" > merged_input.tmp

    REM B. Define Expectation File
    REM Replace 'test_' with 'expected_'
    set "expected_file=!test_file:test_=expected_!"

    REM Check if expected file exists
    if not exist "!expected_file!" (
        echo Warning: Missing expected file for %%f. Skipping.
    ) else (
        echo Running test: %%f...

        REM C. Run Java (Carrot) with Error Capture
        java -classpath ..\bin Carrot < merged_input.tmp > ACTUAL.TXT 2>&1

        REM D. Compare Output
        FC ACTUAL.TXT "!expected_file!" > nul

        IF ERRORLEVEL 1 (
            echo X FAILED: %%f
            echo See differences below:
            echo ----------------------------------------
            FC ACTUAL.TXT "!expected_file!"
            echo ----------------------------------------
            del merged_input.tmp
            exit /b 1
        ) else (
            echo - PASSED: %%f
        )
    )
)

REM Clean up temp file
if exist merged_input.tmp del merged_input.tmp

echo All tests passed!
exit /b 0
