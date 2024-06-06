package me.coley.recaf.decompile.fernflower;

import me.coley.recaf.workspace.JavaResource;
import org.jetbrains.java.decompiler.main.extern.IContextSource;
import org.jetbrains.java.decompiler.main.extern.IResultSaver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JavaResourceContextSource implements IContextSource {

    private final JavaResource javaResource;

    public JavaResourceContextSource(JavaResource javaResource) {
        this.javaResource = javaResource;
    }

    @Override
    public String getName() {
        return "JavaResource " + javaResource;
    }

    @Override
    public Entries getEntries() {
        final List<Entry> classes = javaResource.getClasses().keySet().stream().map(Entry::parse).collect(Collectors.toList());
        final List<String> directories = new ArrayList<>();
        final List<Entry> others = javaResource.getFiles().keySet().stream().map(Entry::parse).collect(Collectors.toList());
        final List<IContextSource> jarChildren = new ArrayList<>();

        return new Entries(classes, directories, others, jarChildren);
    }

    @Override
    public InputStream getInputStream(String resource) {
        byte[] bytes = javaResource.getFiles().get(resource);
        if (bytes == null && resource.endsWith(CLASS_SUFFIX)) {
            bytes = javaResource.getClasses().get(resource.substring(0, resource.indexOf(CLASS_SUFFIX)));
        }
        if (bytes == null) return null;
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public boolean hasClass(String className) {
        return javaResource.getClasses().containsKey(className);
    }

    @Override
    public byte[] getClassBytes(String className) {
        return javaResource.getClasses().get(className);
    }

    @Override
    public IOutputSink createOutputSink(IResultSaver saver) {
        return new IOutputSink() {
            @Override
            public void begin() {
            }

            @Override
            public void acceptClass(String qualifiedName, String fileName, String content, int[] mapping) {
            }

            @Override
            public void acceptDirectory(String directory) {
            }

            @Override
            public void acceptOther(String path) {
            }

            @Override
            public void close() {
            }
        };
    }
}
