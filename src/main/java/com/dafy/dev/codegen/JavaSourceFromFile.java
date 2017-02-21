package com.dafy.dev.codegen;

/**
 * Created by chunxiaoli on 12/30/16.
 */

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

/**
 * A file object used to represent source coming from a string.
 */
public class JavaSourceFromFile extends SimpleJavaFileObject {
    /**
     * The source code of this "file".
     */
    final String code;

    /**
     * Constructs a new JavaSourceFromString.
     * @param name the name of the compilation unit represented by this file object
     * @param code the source code for the compilation unit represented by this file object
     */
    JavaSourceFromFile(String name, String code) {
        super(URI.create(name),
                Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}
