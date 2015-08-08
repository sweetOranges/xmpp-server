package org.soxmpp.server.xmpp.router;

import org.soxmpp.server.xmpp.session.SessionManager;
import org.xmpp.packet.Packet;

public abstract class Router {
	
	protected SessionManager sessionManager;
	
	public Router() {
	}
	
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	/**
	 * this is a base message dispatcher
	 * @param packet packet
	 */
	public abstract void route(Packet packet);
}
