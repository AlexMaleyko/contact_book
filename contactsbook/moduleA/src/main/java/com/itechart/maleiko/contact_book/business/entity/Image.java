package com.itechart.maleiko.contact_book.business.entity;

public class Image {
    private String name;
    private String length;
    private byte[] byteRepresentation;

    public Image(String name, String length, byte[] byteRepresentation) {
        this.name = name;
        this.length = length;
        this.byteRepresentation = byteRepresentation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public byte[] getByteRepresentation() {
        return byteRepresentation;
    }

}
