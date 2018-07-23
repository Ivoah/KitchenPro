OUTPUT="KitchenPro.jar"

all:
	javac *.java
	jar cvfe $(OUTPUT) KitchenPro *.class quantities.txt

run:
	java -jar $(OUTPUT)
