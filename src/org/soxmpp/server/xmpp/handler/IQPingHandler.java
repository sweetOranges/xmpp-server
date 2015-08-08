package org.soxmpp.server.xmpp.handler;

import java.util.Collections;
import java.util.Iterator;

import org.xmpp.packet.IQ;
import org.xmpp.packet.IQ.Type;

public class IQPingHandler extends IQHandler{

	public static final String NAMESPACE = "urn:xmpp:ping";
	

	/**
	 * Constructs a new handler that will process XMPP Ping request.
	 */
	public IQPingHandler() {
	}

	/*
	 * @see
	 * org.jivesoftware.openfire.handler.IQHandler#handleIQ(org.xmpp.packet.IQ)
	 */
	@Override
	public IQ handleIQ(IQ packet) {
		if (packet.getType().equals(Type.get)) {
			return IQ.createResultIQ(packet);
		}
		return null;
	}

	/*
	 * @see org.jivesoftware.openfire.disco.ServerFeaturesProvider#getFeatures()
	 */
	public Iterator<String> getFeatures() {
		return Collections.singleton(NAMESPACE).iterator();
	}

	@Override
	public String getNamespace() {
		// TODO Auto-generated method stub
		return NAMESPACE;
	}
}
