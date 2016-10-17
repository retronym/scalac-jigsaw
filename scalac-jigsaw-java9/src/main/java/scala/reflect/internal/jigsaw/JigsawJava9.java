/*
 * Copyright (C) 2016. Lightbend Inc. <http://lightbend.com>
 */

package scala.reflect.internal.jigsaw;

import java.io.IOException;
import java.lang.invoke.VarHandle;
import java.lang.module.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public class JigsawJava9 implements JigsawAPI {

    public void helloModules() {
        ModuleDescriptor mod1 = new ModuleDescriptor.Builder("acme.mod1").requires("java.xml").build();
        ModuleDescriptor mod2 = new ModuleDescriptor.Builder("acme.mod2").requires("acme.mod1").build();
        ModuleFinder systemFinder = ModuleFinder.ofSystem();
        ModuleFinder customFinder = FixedModuleFinder.newModuleFinder(List.of(mod1, mod2));
        Configuration configuration = Configuration.empty().resolveRequires(systemFinder, customFinder, List.of("acme.mod2"));
        String resultString = configuration.modules().stream().map(Objects::toString).collect(Collectors.joining(", "));
        System.out.println(resultString);
    }
}

class FixedModuleFinder implements ModuleFinder {
    private List<ModuleReference> modules;

    public FixedModuleFinder(List<ModuleReference> modules) {
        this.modules = modules;
    }
    public static FixedModuleFinder newModuleFinder(List<ModuleDescriptor> modules) {
        List<ModuleReference> refs = modules.stream().map(x -> new ModuleReference(x, null, () -> NoopModuleReader.INSTANCE)).collect(Collectors.toList());
        return new FixedModuleFinder(refs);
    }

    @Override
    public Optional<ModuleReference> find(String name) {
        return modules.stream().filter(x -> x.descriptor().name().equals(name)).findFirst();
    }

    @Override
    public Set<ModuleReference> findAll() {
        return modules.stream().collect(Collectors.toSet());
    }
}

class NoopModuleReader implements ModuleReader {
    static final ModuleReader INSTANCE = new NoopModuleReader();

    @Override
    public Optional<URI> find(String name) throws IOException {
        return Optional.empty();
    }

    @Override
    public void close() throws IOException {
    }
}