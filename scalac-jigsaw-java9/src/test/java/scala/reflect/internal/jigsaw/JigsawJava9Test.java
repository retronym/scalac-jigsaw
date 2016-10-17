/*
 * Copyright (C) 2016. Lightbend Inc. <http://lightbend.com>
 */

package scala.reflect.internal.jigsaw;

import org.junit.Test;

import static org.junit.Assert.*;

public class JigsawJava9Test {
    @Test
    public void helloModulesTest() {
        JigsawJava9 jig = new JigsawJava9();
        jig.helloModules();
    }
}
