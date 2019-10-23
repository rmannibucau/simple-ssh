#! /bin/sh

export GRAALVM_HOME="${GRAALVM_HOME:-$JAVA_HOME}"

export classpath="target/classes:$HOME/.m2/repository/com/jcraft/jsch/0.1.55/jsch-0.1.55.jar"

native-image \
    -classpath "$classpath" \
    --no-fallback \
    --static \
    -H:+ReportExceptionStackTraces \
    -H:+TraceClassInitialization \
    --enable-all-security-services \
    '--initialize-at-build-time=com.jcraft.jsch.JSch,com.jcraft.jsch.JSch$1' \
    com.github.rmannibucau.ssh.SimpleSsh \
    simple-ssh
