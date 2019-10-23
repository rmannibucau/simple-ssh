/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 */
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
