package org.n1vnhil.llm.lowcode.dev.platform.core.parser;

public interface CodeParser<T> {

    T parse(String content);

}
