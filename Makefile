# This Makefile is for building the java sources
#
# Based on http://stackoverflow.com/questions/12684771/how-can-i-make-makefile-for-java-with-external-jar-file, Freya Ren, July 2014

# Set your entry point of your java app:
ENTRY_POINT = burstiDAtor/BurstiDAtor

BUILD_DIR = build/java${JTARGET}

SOURCE_FILES = $(wildcard java/burstiDAtor/*.java)

ifdef JTARGET
  TARGET_FLAGS=-source $(JTARGET) -target $(JTARGET)
  JAR_INFIX=_java${JTARGET}
else
  TARGET_FLAGS=
  JAR_INFIX=
endif

# Set the file name of your jar package:
JAR_PKG = build/java${JTARGET}/BurstiDAtor${JAR_INFIX}.jar


JAVAC = javac
JFLAGS = -encoding UTF-8 $(TARGET_FLAGS)

vpath %.class build/java${JTARGET}
vpath %.java java

# show help message by default
Default:
	@echo "Choose one of the following to build the java sources"
	@echo "make new: new project."
	@echo "make build: build project."
	@echo "make clean: clear classes generated."
	@echo "make rebuild: rebuild project."
	@echo "make run: run your app."
	@echo "make jar: package your project into a executable jar."
	@echo "make pdf: build documentation."
	@echo ""
	@echo "Use  make JTARGET=6 jar  to build for an older JRE version"


build$(JTARGET): ${BUILD_DIR} $(SOURCE_FILES:.java=.class)

# pattern rule
%.class: %.java
	$(JAVAC) -cp java -d build/java${JTARGET} $(JFLAGS) $<

rebuild${JTARGET}: clean build${JTARGET}

.PHONY: new clean run jar

new$(JTARGET): ${BUILD_DIR}

${BUILD_DIR}:
	mkdir -pv build/java${JTARGET}

clean:
	rm -frv build

run: build$(JTARGET)
	java -cp build/java$(JTARGET) $(ENTRY_POINT)

jar: $(JAR_PKG)
$(JAR_PKG): build$(JTARGET)
	@echo "jtarget ${JTARGET}"
	jar cvfe $(JAR_PKG) $(ENTRY_POINT) -C build/java$(JTARGET) .


pdf:
	cd doc && make latexpdf
