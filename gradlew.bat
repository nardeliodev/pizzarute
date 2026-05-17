@echo off
setlocal
set APP_HOME=%~dp0
if exist "%JAVA_HOME%\bin\java.exe" (
  set JAVA_CMD="%JAVA_HOME%\bin\java.exe"
) else (
  set JAVA_CMD=java
)
%JAVA_CMD% -cp "%APP_HOME%gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
