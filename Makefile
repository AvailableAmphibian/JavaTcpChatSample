# --- Variable useful to launch a JAR --- # 
# -- -- -- -- -- -- ----- -- -- -- -- -- #

JVM = java 
JVM_FLAGS = -cp build

JC = javac # The executable to use as a compiler for java.

# === javac flags ===
JC_FLAGS = -sourcepath src -cp ${CLASSPATH}:src
# === javac flags ===

# We could override the BUILD_FOLDER in the parameter.
ifndef BUILD_FOLDER
    override BUILD_FOLDER = build
endif

# --- End of the variable definition  --- # 
# -- -- -- -- -- -- ---- -- -- --  -- -- #

default: classes

classes: | $(BUILD_FOLDER)
	$(JC) $(JC_FLAGS) -d $(BUILD_FOLDER) src/**/*.java

$(BUILD_FOLDER):
	mkdir $(BUILD_FOLDER)


run_hello_serv:
	$(JVM) $(JVM_FLAGS) ex1.Server

run_one_time_client:
	$(JVM) $(JVM_FLAGS) ex1.Client

run_server:
	$(JVM) $(JVM_FLAGS) chat.Server

connect_client:
	$(JVM) $(JVM_FLAGS) chat.Client



clean:
	$(RM) -r $(BUILD_FOLDER)/* 
