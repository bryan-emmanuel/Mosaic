@echo off
if not exist %JAVA_HOME% goto ERROR_JAVA
if not exist %RESIN_HOME% goto ERROR_RESIN

cd /d %~dp0

PATH=%JAVA_HOME%\bin;%PATH%
%RESIN_HOME%\httpd -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=12345 -server-root %CD%\web -conf %CD%\resin.conf

goto END

:ERROR_JAVA
echo Error: JAVA_HOME is not set.
pause
goto END

:ERROR_RESIN
echo Error: RESIN_HOME is not set.
pause

:END
