package com.dwelguisz.year2021.helper.day16;

import java.util.List;

public class OperatorPacket extends Packet{
    public List<Packet> subPackets;

    public OperatorPacket(Integer version, Integer packetId, List<Packet> subPackets) {
        super(version, packetId);
        this.subPackets = subPackets;
    }

    @Override
    public Integer versionSum() {
        Integer sum = version;
        for (Packet subPacket : subPackets) {
            sum += subPacket.versionSum();
        }
        return sum;
    }

    public Long getValue() {
        switch (packetId) {
            case 0: return getSum();
            case 1: return getProduct();
            case 2: return getMaximum();
            case 3: return getMinimum();
            case 5: return getGreaterThan();
            case 6: return getLessThan();
            case 7: return getEquals();
            default: return 0L;
        }
    }

    public Long getSum() {
        Long sum = 0L;
        for (Packet subPacket : subPackets) {
            sum += subPacket.getValue();
        }
        return sum;
    }

    public Long getProduct() {
        Long product = 1L;
        for (Packet subPacket : subPackets) {
            product *= subPacket.getValue();
        }
        return product;
    }

    public Long getMinimum() {
        return subPackets.stream().map(packet -> packet.getValue()).min(Long::compareTo).get();
    }

    public Long getMaximum() {
        return subPackets.stream().map(packet -> packet.getValue()).max(Long::compareTo).get();
    }

    public Long getGreaterThan() {
        if (subPackets.get(0).getValue() > subPackets.get(1).getValue()) {
            return 1L;
        } else {
            return 0L;
        }
    }

    public Long getLessThan() {
        if (subPackets.get(0).getValue() < subPackets.get(1).getValue()) {
            return 1L;
        } else {
            return 0L;
        }

    }

    public Long getEquals() {
        if (subPackets.get(0).getValue() == subPackets.get(1).getValue()) {
            return 1L;
        } else {
            return 0L;
        }

    }
}
