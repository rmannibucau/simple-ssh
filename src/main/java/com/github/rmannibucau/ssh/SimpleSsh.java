package com.github.rmannibucau.ssh;

import java.nio.file.Paths;

public class SimpleSsh {
    public static void main(final String[] args) {
        try (final Ssh ssh = new Ssh(new SshKey(
                Paths.get(System.getProperty("user.home")).resolve(".ssh/id_rsa"),
                args[1]), args[0])) {
            ssh
                .scp(
                    Paths.get("/tmp/test-ssh/from/component-runtime-junit-1.1.14.jar"),
                    "/tmp/test-ssh/to/component-runtime-junit-1.1.14.jar",
                    d -> System.out.printf("Upload progress %2.2f%%\r", d))
                .exec(String.join(" ",
                    "java", "-cp",
                    "/tmp/test-ssh/to/component-runtime-junit-1.1.14.jar",
                    "org.talend.sdk.component.maven.MavenDecrypter",
                    "github"))
                .exec(String.join(" ", "rm", "/tmp/test-ssh/to/component-runtime-junit-1.1.14.jar"));
        }
    }

    private SimpleSsh() {
        // no-op
    }
}
