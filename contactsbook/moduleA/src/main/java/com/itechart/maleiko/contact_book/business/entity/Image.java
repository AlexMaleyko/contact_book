package com.itechart.maleiko.contact_book.business.entity;

/**
 * Created by Alexey on 11.08.2017.
 */
public class Image {
    String name;
    String length;
    byte[] byteRepresentation;

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

    public void setByteRepresentation(byte[] byteRepresentation) {
        this.byteRepresentation = byteRepresentation;
    }
}
