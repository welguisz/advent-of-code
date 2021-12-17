package com.dwelguisz.year2021.helper.day16;

public class NumberPacket extends Packet{
    public Long value;
    public NumberPacket(Integer version, Long value) {
        super(version, 4);
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
