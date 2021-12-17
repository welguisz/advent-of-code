package com.dwelguisz.year2021.helper.day16;

public class Packet {
    public Integer version;
    public Integer packetId;

    public Packet(Integer version, Integer packetId) {
        this.version = version;
        this.packetId = packetId;
    }

    public Integer versionSum() {
        return version;
    }

    public Long getValue() {
        throw new NullPointerException("Bad place");
    }

    @Override
    public String toString() {
        return "";
    }

}
