# syntax: make jar="jarname" class="classname" run
# run is normal, runv is verbose

run:
	javac methods/*.java
	jar cfm methods.jar Manifest.txt methods/*
	java -jar methods.jar $(jar) $(class)

runv:
	javac methods/*.java
	jar cfm methods.jar Manifest.txt methods/*
	java -jar methods.jar -v $(jar) $(class)
