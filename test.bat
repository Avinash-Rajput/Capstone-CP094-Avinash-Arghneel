@echo off
rem Builds the project, then runs all tests
call "%~dp0build.bat" || exit /b 1
java -cp "%~dp0out" com.copycheck.AllTests
