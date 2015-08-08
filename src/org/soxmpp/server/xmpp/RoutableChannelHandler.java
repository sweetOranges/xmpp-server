package org.soxmpp.server.xmpp;

import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;

/**
 * 
 * 
 * @author Matt Tucker
 */
public interface RoutableChannelHandler extends ChannelHandler<Packet> {

	/**
	 * Returns the XMPP address. The address is used by services like the core
	 * server packet router to determine if a packet should be sent to the
	 * handler. Handlers that are working on behalf of the server should use the
	 * generic server hostname address (e.g. server.com).
	 * 
	 * @return the XMPP address.
	 */
	public JID getAddress();
}