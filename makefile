JFLAGS = -g -d .
JC  = javac
JVM = java

MAINPACKAGE = hackbotui
PACKAGES = hackbotcore hackbotui hackbotutil
CLASSES = *.java

MAIN = $(MAINPACKAGE).TestGame

default:
	$(JC) $(JFLAGS) $(CLASSES)

run: default
	$(JVM) -ea $(MAIN)

clean:
	$(RM) -r $(PACKAGES)
	$(RM) *.class
