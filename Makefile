run:
	javac methods/*.java
	jar cfm methods.jar Manifest.txt methods/*
	java -jar methods.jar
