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
import org.soxmpp.server.xmpp.handler.PresenceUpdateHandler;
import org.soxmpp.server.xmpp.session.ClientSession;
import org.soxmpp.server.xmpp.session.Session;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketError;
import org.xmpp.packet.Presence;

/** 
 * This class is to route Presence packets to their corresponding handler.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class PresenceRouter extends Router{

    private final Log log = LogFactory.getLog(getClass());

    private PresenceUpdateHandler presenceUpdateHandler;

    /**
     * Constucts a packet router.
     */
    public PresenceRouter() {
    }
    
	public void setPresenceUpdateHandler(PresenceUpdateHandler presenceUpdateHandler) {
		this.presenceUpdateHandler = presenceUpdateHandler;
	}

	/**
     * Routes the Presence packet.
     * 
     * @param packet the packet to route
     */
    public void route(Packet packet) {
    	
    	Presence presence=(packet instanceof Presence)?(Presence)packet:null;
    	
        if (packet == null) {
            throw new NullPointerException();
        }
        ClientSession session = sessionManager.getSession(presence.getFrom());

        if (session == null || session.getStatus() != Session.STATUS_CONNECTED) {
            handle(presence);
        } else {
        	presence.setTo(session.getAddress());
        	presence.setFrom((JID) null);
        	presence.setError(PacketError.Condition.not_authorized);
            session.process(presence);
        }
    }

    private void handle(Presence packet) {
        try {
            Presence.Type type = packet.getType();
            // Presence updates (null == 'available')
            if (type == null || Presence.Type.unavailable == type) {
                presenceUpdateHandler.process(packet);
            } else {
                log.warn("Unknown presence type");
            }

        } catch (Exception e) {
            log.error("Could not route packet", e);
            Session session = sessionManager.getSession(packet.getFrom());
            if (session != null) {
                session.close();
            }
        }
    }

}
