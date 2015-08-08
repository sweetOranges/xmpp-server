package org.soxmpp.server.xmpp;

import org.xmpp.packet.Packet;

/**
 * Interface to handle packets delivered by Channels.
 *
 * @author Matt Tucker
 */
public interface ChannelHandler<T extends Packet> {

    /**
     * Process an XMPP packet.
     *
     * @param packet a packet to process.
     * @throws UnauthorizedException if not allowed to process the packet.
     * @throws PacketException thrown if the packet is malformed (results in the sender's
     *      session being shutdown).
     */
    public abstract void process(T packet) throws UnauthorizedException, PacketException;
}