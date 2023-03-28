package com.home.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class LocalObjectInputStream extends ObjectInputStream {
    protected LocalObjectInputStream() throws IOException, SecurityException {
        super();
    }

    public LocalObjectInputStream(InputStream arg0) throws IOException {
        super(arg0);
    }

    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException{
        String name = desc.getName();
        if (name.startsWith("com.example.testapp.javaTest.tool")) {
            name = name.replace("com.example.testapp.javaTest.tool", "com.home.server");
        }
        return Class.forName(name);
    }
}
