@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV vars
@REM M2_HOME - location of maven's installed home dir
@REM MAVEN_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@REM MAVEN_BATCH_PAUSE - set to 'on' to wait for a keystroke before ending
@REM MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM     e.g. to debug Maven itself, use
@REM set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM MAVEN_SKIP_RC - flag to disable loading of mavenrc files
@REM ----------------------------------------------------------------------------

@setlocal

set ERROR_CODE=0

@REM To isolate internal variables from possible post scripts, we use another setlocal
@setlocal

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto OkJHome

echo.
echo Error: JAVA_HOME not found in your environment. >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto init

echo.
echo Error: JAVA_HOME is set to an invalid directory. >&2
echo JAVA_HOME = "%JAVA_HOME%" >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

@REM ==== END VALIDATION ====

:init

@REM Find the project base dir, i.e. the directory that contains the folder ".mvn".
@REM Fallback to current working directory if not found.

set MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
IF "%MAVEN_PROJECTBASEDIR%"=="" set MAVEN_PROJECTBASEDIR=%CD%

@REM Extension to allow automatically downloading the maven-wrapper.jar from Maven-central
@REM This allows using the maven wrapper in projects that prohibit checking in binary data.
if exist "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" (
    if "%MAVEN_VERBOSE%" == "true" (
        echo Found %MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar
    )
) else (
    if "%MAVEN_VERBOSE%" == "true" (
        echo Couldn't find %MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar, downloading it ...
    )
    if not exist "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper" mkdir "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper"
    powershell -Command "&{'[Net.ServicePointManager]::SecurityProtocol = [Net.ServicePointManager]::SecurityProtocol -bor 3072; $webClient = New-Object Net.WebClient; $webClient.DownloadFile(\"https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar\", \"%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar\")}" || powershell -Command "&{(New-Object Net.WebClient).DownloadFile('https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar', '%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar')}"
    if "%ERRORLEVEL%" == "0" (
        if "%MAVEN_VERBOSE%" == "true" (
            echo Successfully downloaded %MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar
        )
    ) else (
        if "%MAVEN_VERBOSE%" == "true" (
            echo Failed to download %MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar
        )
        echo.
        echo Error: Failed to download maven-wrapper.jar. Retrying using an alternative URL if available ...
        echo.
        for /F "tokens=1,2 delims==" %%A in ("%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties") do (
            if "%%A"=="wrapperUrl" goto downloadWrapper
        )
        goto fail
        :downloadWrapper
        for /F "tokens=2 delims==" %%A in ("%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties") do (
            if not "%%A"=="" (
                powershell -Command "&{'[Net.ServicePointManager]::SecurityProtocol = [Net.ServicePointManager]::SecurityProtocol -bor 3072; $webClient = New-Object Net.WebClient; $webClient.DownloadFile(\"%%A\", \"%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar\")}" || powershell -Command "&{(New-Object Net.WebClient).DownloadFile('%%A', '%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar')}"
            )
        )
    )
)
@REM if exist "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" (
@REM     if "%MAVEN_VERBOSE%" == "true" (
@REM         echo Found %MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar
@REM     )
@REM )

setlocal enabledelayedexpansion
for /F "usebackq delims=" %%a in ("%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties") do (
    if "!%%a:~0,1!" NEQ "#" (
        set "%%a"
    )
)
set "WRAPPER_JAR=!MAVEN_PROJECTBASEDIR!\.mvn\wrapper\maven-wrapper.jar"
set "WRAPPER_URL=!wrapperUrl!"
set "WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain"

set DOWNLOAD_URL="%WRAPPER_URL%"

set "MAVEN_OPTS=!MAVEN_OPTS! "-Dorg.slf4j.simpleLogger.defaultLogLevel=warn""

@REM For old Maven this is a workaround to an infinite loop situation caused by recursive file search.
if "!CDPATH!"=="" (
    set "CDPATH=.;"
)

@REM To differentiate between Windows and non-Windows (bash) shells, we need to check for windows first.
if not "%OS%"=="Windows_NT" goto configureBash

:configureBatch
@setlocal
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain
for /F "tokens=1,2 delims==" %%a in ("%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties") do (
    if "%%a"=="distributionUrl" goto distributionUrlFound
)
echo Maven wrapper properties not found in %MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties. Falling back to using Maven 3.9.5 distribution
set DOWNLOAD_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.5/apache-maven-3.9.5-bin.zip
goto endDownload

:distributionUrlFound
for /F "tokens=1,2 delims==" %%a in ("%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties") do (
    if "%%a"=="distributionUrl" set DOWNLOAD_URL=%%b
)

:endDownload
@REM if MAVEN_PROJECTBASEDIR is not set, then assume the current directory is the Maven project base directory
if "%MAVEN_PROJECTBASEDIR%"=="" (
	set "MAVEN_PROJECTBASEDIR=%CD%"
	if "%MAVEN_VERBOSE%" == "true" echo Using default value of MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR%
)

@REM Fallback to old method if JAVA_HOME environment variable is not set.
if not "%JAVA_HOME%"=="" goto have_java_home
for /F "delims=" %%a in ('where java.exe') do (
    set "JAVA_HOME=%%~dpa"
    goto have_java_home
)

:have_java_home
@REM To isolate internal variables from possible post scripts, we use another setlocal
@setlocal

set "CLASSWORLDS_JAR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
set "CLASSWORLDS_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain"

"%JAVA_HOME%\bin\java.exe" %JVM_CONFIG_MAVEN_PROPS% %MAVEN_OPTS% %MAVEN_DEBUG_OPTS% -classpath %CLASSWORLDS_JAR% %CLASSWORLDS_LAUNCHER% %MAVEN_CMD_LINE_ARGS%
if %errorlevel% neq 0 (
    endlocal &cmd /C exit /b %errorlevel%
)

endlocal &goto postExec
exit /b

:postExec
@endlocal
cmd /C exit /b %errorlevel%

