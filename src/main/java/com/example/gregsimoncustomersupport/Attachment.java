package com.example.gregsimoncustomersupport;

public class Attachment {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }

    private String name;

    private byte[] contents;
}
