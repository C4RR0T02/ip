#!/usr/bin/env bash

# create bin directory if it doesn't exist
if [ ! -d "../bin" ]
then
    mkdir ../bin
fi

# delete output from previous run
if [ -e "./ACTUAL.TXT" ]
then
    rm ACTUAL.TXT
fi

# compile the code into the bin folder, terminates if error occurred
if ! javac -cp ../src/main/java -Xlint:none -d ../bin ../src/main/java/*.java
then
    echo "********** BUILD FAILURE **********"
    exit 1
fi

# Define your setup file
SETUP_FILE="tests/setup.txt"

# Check if setup file exists
if [ ! -f "$SETUP_FILE" ]; then
    echo "⚠️  Error: Setup file not found at $SETUP_FILE"
    exit 1
fi

# Loop through all test files in 'tests' folder
for test_file in tests/test_*.txt
do
    # A. Merge Setup + Test Case
    MERGED_INPUT="merged_input.tmp"
    cat "$SETUP_FILE" "$test_file" > "$MERGED_INPUT"

    # B. Define Expectation File
    # This turns "tests/test_01.txt" into "tests/expected_01.txt"
    expected_file="${test_file/test_/expected_}"

    # Check if expected file exists before running
    if [ ! -f "$expected_file" ]; then
        echo "⚠️  Missing expected file for $test_file. Skipping."
        continue
    fi

    echo "Running test: $test_file..."

    # C. Run Java (Using the MERGED input, capturing errors with 2>&1)
    java -classpath ../bin Carrot < "$MERGED_INPUT" > ACTUAL.TXT 2>&1

    # D. Compare Output
    # Create UNIX version of expected output
    cp "$expected_file" EXPECTED-UNIX.TXT
    dos2unix ACTUAL.TXT EXPECTED-UNIX.TXT > /dev/null 2>&1

    diff ACTUAL.TXT EXPECTED-UNIX.TXT
    if [ $? -eq 0 ]; then
        echo "✅ PASSED: $test_file"
    else
        echo "❌ FAILED: $test_file"
        echo "See differences above."
        rm "$MERGED_INPUT"
        exit 1 # Stop immediately on first failure
    fi
done

# Clean up temp file
rm "$MERGED_INPUT" 2> /dev/null
echo "🎉 All tests passed!"
exit 0
