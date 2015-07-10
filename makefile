JFLAGS = -g -d .
JC  = javac
JVM = java

PACKAGE = spybot
CLASSES = *.java

MAIN = $(PACKAGE).TestGame

default:
	$(JC) $(JFLAGS) $(CLASSES)

run: default
	$(JVM) $(MAIN)

clean:
	$(RM) $(PACKAGE) *.class
