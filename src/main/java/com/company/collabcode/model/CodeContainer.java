package com.company.collabcode.model;

public class CodeContainer {
    private String code;
    private String stdin;
    private String language;

    public CodeContainer() {}

    public CodeContainer(String code, String stdin, String language) {
        this.code = code;
        this.stdin = stdin;
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStdin() {
        return stdin;
    }

    public void setStdin(String stdin) {
        this.stdin = stdin;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
