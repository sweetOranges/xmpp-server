/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.soxmpp.server.xmpp.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soxmpp.server.xmpp.UnauthorizedException;
import org.soxmpp.server.xmpp.router.PacketDeliverer;
import org.soxmpp.server.xmpp.session.SessionManager;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;

/**
 * This is an abstract class to handle routed IQ packets.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public abstract class PresenceHandler {

	protected final Log log = LogFactory.getLog(PresenceHandler.class);

	protected SessionManager sessionManager;

	/**
	 * Constructor.
	 */
	public PresenceHandler() {

	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	/**
	 * Processes the received IQ packet.
	 * 
	 * @param packet
	 *            the packet
	 */
	public void process(Packet packet) {
		Presence presence = (Presence) packet;
		try {
			Presence reply = handleIQ(presence);
			if (reply != null) {
				PacketDeliverer.deliver(reply);
			}
		} catch (UnauthorizedException e) {
			if (presence != null) {
				//#TODO
			}
		}
	}

	/**
	 * Handles the received Presence packet.
	 * 
	 * @param packet
	 *            the packet
	 * @return the response to send back
	 * @throws UnauthorizedException
	 *             if the user is not authorized
	 */
	public abstract Presence handleIQ(Presence packet) throws UnauthorizedException;

	/**
	 * Returns the namespace of the handler.
	 * 
	 * @return the namespace
	 */
	public abstract String getNamespace();

}
