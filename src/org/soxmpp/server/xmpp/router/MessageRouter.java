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
package org.soxmpp.server.xmpp.router;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soxmpp.server.xmpp.session.ClientSession;
import org.soxmpp.server.xmpp.session.Session;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

/**
 * This class is to route Message packets to their corresponding handler.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class MessageRouter extends Router {

	private final Log log = LogFactory.getLog(getClass());

	/**
	 * Constucts a packet router.
	 */
	public MessageRouter() {
	}

	/**
	 * Routes the Message packet.
	 * 
	 * @param packet
	 *            the packet to route
	 */
	public void route(Packet packet) {
		Message message = (packet instanceof Message) ? (Message) packet : null;
		if (message == null) {
			throw new NullPointerException();
		}
		ClientSession session = sessionManager.getSession(message
				.getFrom());
		if (session != null
				&& session.getStatus() == Session.STATUS_AUTHENTICATED) {
			log.info("正在路由");
			Message reply = new Message();
			reply.setID(message.getID());
			reply.setTo(session.getAddress());
			reply.setFrom(message.getTo());
			reply.setType(message.getType());
			reply.setThread(message.getThread());
			reply.setBody(message.getBody());
			session.process(reply);
		}
	}

}
