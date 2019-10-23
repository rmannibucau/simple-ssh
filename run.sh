./simple-ssh \
    -Djava.library.path="${GRAALVM_HOME:-$JAVA_HOME}/jre/lib/amd64" \
    -Djavax.net.ssl.trustStore="${GRAALVM_HOME:-$JAVA_HOME}/jre/lib/security/cacerts" \
    "$USER@localhost:22" "$SSH_PASSWORD"
