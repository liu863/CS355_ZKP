JFLAGS = 
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = ZKPServer.java ZKPClient.java Graph.java ZKPUI.java
        

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
