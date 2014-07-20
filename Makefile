# Based on http://stackoverflow.com/questions/12684771/how-can-i-make-makefile-for-java-with-external-jar-file, Freya Ren, July 2014

# Set the file name of your jar package:
JAR_PKG = build/java/BurstiDAtor.jar

# Set your entry point of your java app:
ENTRY_POINT = burstiDAtor/BurstiDAtor


SOURCE_FILES = \
java/burstiDAtor/Burst.java \
java/burstiDAtor/BurstWizard.java \
java/burstiDAtor/BurstiDAtor.java \
java/burstiDAtor/Bursts.java \
java/burstiDAtor/Props.java \
java/burstiDAtor/Settings.java \
java/burstiDAtor/Spikes.java \
java/burstiDAtor/Utils.java

JAVAC = javac
JFLAGS = -encoding UTF-8


vpath %.class build/java
vpath %.java java

# show help message by default
Default:
	@echo "make new: new project, create src, bin, res dirs."
	@echo "make build: build project."
	@echo "make clean: clear classes generated."
	@echo "make rebuild: rebuild project."
	@echo "make run: run your app."
	@echo "make jar: package your project into a executable jar."

build: $(SOURCE_FILES:.java=.class)

# pattern rule
%.class: %.java
	$(JAVAC) -cp java -d build/java $(JFLAGS) $<

rebuild: clean build

.PHONY: new clean run jar

new:
	mkdir -pv build/java

clean:
	rm -frv build

run: new build
	java -cp build/java $(ENTRY_POINT)

jar: new build
	jar cvfe $(JAR_PKG) $(ENTRY_POINT) -C build/java .


