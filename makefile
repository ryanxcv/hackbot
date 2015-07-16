JFLAGS = -g -d .
JARFLAGS = cfm
JC  = javac
JVM = java
JAR = jar

MAINPACKAGE = hackbotui
PACKAGES = hackbotcore hackbotui hackbotutil
RESOURCES = img sound
CLASSES = *.java

MAIN = $(MAINPACKAGE).TestGame

default:
	$(JC) $(JFLAGS) $(CLASSES)

run: default
	$(JVM) -ea $(MAIN)

jar: default
	$(JAR) $(JARFLAGS) Hackbot.jar MANIFEST $(PACKAGES) $(RESOURCES)

clean:
	$(RM) -r $(PACKAGES)
	$(RM) *.class
	$(RM) *.jar
