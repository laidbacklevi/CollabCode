package com.company.collabcode.model;

public class OutputContainer {
    private String sourceUserFirstName;
    private String output;

    public OutputContainer() {
    }

    public OutputContainer(String sourceUserFirstName, String output) {
        this.sourceUserFirstName = sourceUserFirstName;
        this.output = output;
    }

    public String getSourceUserFirstName() {
        return sourceUserFirstName;
    }

    public void setSourceUserFirstName(String sourceUserFirstName) {
        this.sourceUserFirstName = sourceUserFirstName;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}