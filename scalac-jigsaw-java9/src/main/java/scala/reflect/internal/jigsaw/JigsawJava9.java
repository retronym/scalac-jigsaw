/*
 * Copyright (C) 2016. Lightbend Inc. <http://lightbend.com>
 */

package scala.reflect.internal.jigsaw;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.VarHandle;
import java.lang.module.*;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JigsawJava9 implements JigsawAPI {

    public void helloModules() {
        ModuleDescriptor mod0 = ModuleDescriptor.newModule("acme.mod0").exports("acme.mod0").requires("java.xml").build();
        ModuleDescriptor mod1 = ModuleDescriptor.newModule("acme.mod1").exports("acme.mod1").requires(Set.of(ModuleDescriptor.Requires.Modifier.TRANSITIVE), "acme.mod0").build();
        ModuleDescriptor mod2 = ModuleDescriptor.newModule("acme.mod2").exports("acme.mod2").requires("acme.mod1").build();
        ModuleFinder systemFinder = ModuleFinder.ofSystem();
        ModuleFinder customFinder = FixedModuleFinder.newModuleFinder(List.of(mod0, mod1, mod2));
        Configuration configuration = Configuration.empty().resolve(systemFinder, customFinder, List.of("acme.mod2"));
        String resultString = configuration.modules().stream().map(Objects::toString).collect(Collectors.joining(", "));
        System.out.println(resultString);
        ResolvedModule root = configuration.findModule(mod2.name()).get();
        List<ModuleDescriptor.Exports> exportedPackagesOfReads = root.reads().stream().flatMap(x -> x.reference().descriptor().exports().stream()).collect(Collectors.toList());
        Set<String> rootPackages = root.reference().descriptor().packages();
        System.out.println(exportedPackagesOfReads);
        System.out.println(rootPackages);
    }
    public static void main(String... args) {
        new JigsawJava9().helloModules();
    }
}

class FixedModuleFinder implements ModuleFinder {
    private List<ModuleReference> modules;

    public FixedModuleFinder(List<ModuleReference> modules) {
        this.modules = modules;
    }
    public static FixedModuleFinder newModuleFinder(List<ModuleDescriptor> modules) {
        List<ModuleReference> refs = modules.stream().map(x -> new FixedModuleReference(x, null)).collect(Collectors.toList());
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

final class FixedModuleReference extends ModuleReference {
    public FixedModuleReference(ModuleDescriptor descriptor, URI location) {
        super(descriptor, location);
    }

    @Override
    public ModuleReader open() throws IOException {
        return NoopModuleReader.INSTANCE;
    }

}

class NoopModuleReader implements ModuleReader {
    static final ModuleReader INSTANCE = new NoopModuleReader();

    @Override
    public Optional<URI> find(String name) throws IOException {
        return Optional.empty();
    }

    @Override
    public Optional<InputStream> open(String name) throws IOException {
        return Optional.empty();
    }

    @Override
    public Optional<ByteBuffer> read(String name) throws IOException {
        return Optional.empty();
    }

    @Override
    public void release(ByteBuffer bb) {
    }

    @Override
    public Stream<String> list() throws IOException {
        return Stream.empty();
    }

    @Override
    public void close() throws IOException {
    }
}