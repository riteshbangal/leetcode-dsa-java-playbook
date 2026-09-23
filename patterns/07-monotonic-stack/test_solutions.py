#!/usr/bin/env python3
"""Compile Pattern 07 Java solutions and compare them with brute-force oracles."""
from pathlib import Path
import re
import shutil
import subprocess
import tempfile

ROOT = Path(__file__).resolve().parents[2]
CHAPTER = ROOT / "patterns/07-monotonic-stack"


def main():
    java = shutil.which("java")
    if not java:
        raise SystemExit("A JDK (9 or newer) with Java 8 targeting is required.")
    javac = shutil.which("javac")
    compiler = [javac] if javac else [java, "-m", "jdk.compiler/com.sun.tools.javac.Main"]
    cases = [
        ("greater", CHAPTER / "NextGreaterElementRight.java"),
        ("smaller", CHAPTER / "NextSmallerElementRight.java"),
        ("previous", CHAPTER / "PreviousSmallerElementLeft.java"),
        ("span", CHAPTER / "StockSpan.java"),
        ("daily", CHAPTER / "DailyTemperatures.java"),
        ("mapped", CHAPTER / "NextGreaterElementI.java"),
        ("circular", CHAPTER / "NextGreaterElementII.java"),
        ("online", CHAPTER / "StockSpanner.java"),
    ]
    total = 0
    with tempfile.TemporaryDirectory(prefix="monotonic-stack-tests-", dir=ROOT) as temporary:
        for mode, source in cases:
            build = Path(temporary) / mode
            build.mkdir()
            subprocess.run(compiler + ["--release", "8", "-Xlint:all", "-d", str(build),
                str(source), str(CHAPTER / "MonotonicStackRegressionTest.java")], check=True)
            result = subprocess.run([java, "-cp", str(build),
                "MonotonicStackRegressionTest", mode], check=True, capture_output=True, text=True)
            print(result.stdout, end="")
            total += int(re.search(r"Passed (\d+) checks", result.stdout).group(1))

        build = Path(temporary) / "histogram"
        build.mkdir()
        subprocess.run(compiler + ["--release", "8", "-Xlint:all", "-d", str(build),
            str(CHAPTER / "LargestRectangleInHistogram.java"),
            str(CHAPTER / "LargestRectangleInHistogramTest.java")], check=True)
        result = subprocess.run([java, "-cp", str(build), "LargestRectangleInHistogramTest"],
            check=True, capture_output=True, text=True)
        print("histogram: " + result.stdout, end="")
        total += int(re.search(r"Passed (\d+) checks", result.stdout).group(1))
    print("All 9 implementations passed; total checks: " + str(total))


if __name__ == "__main__":
    main()
