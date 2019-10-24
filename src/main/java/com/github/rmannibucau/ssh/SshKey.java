package com.github.rmannibucau.ssh;

import java.nio.file.Path;

public class SshKey {
    private final Path path;
    private final String password;

    public SshKey(final Path path, final String password) {
        this.path = path;
        this.password = password;
    }

    public Path getPath() {
        return path;
    }

    public String getPassword() {
        return password;
    }
}
