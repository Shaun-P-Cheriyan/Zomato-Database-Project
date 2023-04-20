
build: Project49.class

Project49.class: Project49.java
	javac Project49.java

run: Project49.class
	java -cp .:mssql-jdbc-11.2.0.jre18.jar Project49

clean:
	rm Project49.class
	rm MyDatabase.class
