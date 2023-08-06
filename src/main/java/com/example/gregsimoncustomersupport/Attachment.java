package com.example.gregsimoncustomersupport;

public class Attachment {
    public static String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }

    private static String name;

    private static byte[] contents;
}
