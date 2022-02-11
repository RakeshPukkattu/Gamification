@echo off
SET CURRENT_DIR=%cd%
SET "JAVA_HOME=C:\Program Files\Java\jdk1.8.0_301"
SET "PATH=C:\Program Files\Java\jdk1.8.0_191\301"
SET "M2_HOME=C:\Program Files\apache-maven-3.8.2"
SET "PATH=C:\Program Files\apache-maven-3.8.2\bin"

call mvn -U -f %CURRENT_DIR%\pom.xml clean install
call mvn spring-boot:run
