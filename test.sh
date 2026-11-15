#!/usr/bin/env bash
# Builds the project, then runs all tests
set -e
"$(dirname "$0")/build.sh"
java -cp out com.copycheck.AllTests
