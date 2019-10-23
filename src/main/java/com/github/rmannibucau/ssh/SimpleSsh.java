/**
 * Copyright (C) 2006-2019 Talend Inc. - www.talend.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
