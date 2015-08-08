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
package org.soxmpp.server.xmpp.net;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.dom4j.io.XMPPPacketReader;
import org.jivesoftware.openfire.net.MXParser;
import org.jivesoftware.openfire.nio.XMLLightweightParser;
import org.soxmpp.server.xmpp.XmppServer;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/** 
 * This class is to create new sessions, destroy sessions and deliver
 * received XML stanzas to the StanzaHandler.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public abstract class ConnectionHandler implements IoHandler {

    private static final Log log = LogFactory.getLog(ConnectionHandler.class);
    
    /**
     * The utf-8 charset for decoding and encoding Jabber packet streams.
     */
    static final String CHARSET = "UTF-8";

    public static final String XML_PARSER = "XML_PARSER";

    protected static final String CONNECTION = "CONNECTION";

    protected static final String STANZA_HANDLER = "STANZA_HANDLER";

    protected String serverName;

    private static final ThreadLocal<XMPPPacketReader> PARSER_CACHE = new ThreadLocal<XMPPPacketReader>()
            {
               @Override
               protected XMPPPacketReader initialValue()
               {
                  final XMPPPacketReader parser = new XMPPPacketReader();
                  parser.setXPPFactory( factory );
                  return parser;
               }
            };

    private static XmlPullParserFactory factory = null;

    static {
        try {
            factory = XmlPullParserFactory.newInstance(
                    MXParser.class.getName(), null);
            factory.setNamespaceAware(true);
        } catch (XmlPullParserException e) {
            log.error("Error creating a parser factory", e);
        }
    }

    /**
     * Constructor. Set the server name from server instance. 
     */
    protected ConnectionHandler() {
        serverName = XmppServer.getInstance().getServerName();
    }

    /**
     * Invoked from an I/O processor thread when a new connection has been created.
     */
    public void sessionCreated(IoSession session) throws Exception {
    		log.info(session.getRemoteAddress()+"创建session");
    }

    /**
     * Invoked when a connection has been opened.
     */
    public void sessionOpened(IoSession session) throws Exception {
        log.info(session.getRemoteAddress()+"打开session");
        // Create a new XML parser
        XMLLightweightParser parser = new XMLLightweightParser(CHARSET);
        session.setAttribute(XML_PARSER, parser);
        // Create a new connection
        final NIOConnection connection = createNIOConnection(session);
        session.setAttribute(CONNECTION, connection);
        session.setAttribute(STANZA_HANDLER, createStanzaHandler(connection));
    }

    /**
     * Invoked when a connection is closed.
     */
    public void sessionClosed(IoSession session) throws Exception {
    	log.info(session.getRemoteAddress()+"断开session");
        Connection connection = (Connection) session.getAttribute(CONNECTION);
        connection.close();
    }

    /**
     * Invoked with the related IdleStatus when a connection becomes idle.
     */
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        log.debug("sessionIdle()...");
        Connection connection = (Connection) session.getAttribute(CONNECTION);
        if (log.isDebugEnabled()) {
            log.debug("Closing connection that has been idle: " + connection);
        }
        connection.close();
    }

    /**
     * Invoked when any exception is thrown.
     */
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        log.debug("exceptionCaught()...");
        log.error(cause);
    }

    /**
     * Invoked when a message is received.
     */
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        log.info("RCVD: " + message);

        // Get the stanza handler
        StanzaHandler handler = (StanzaHandler) session
                .getAttribute(STANZA_HANDLER);

        // Get the XMPP packet parser
        final XMPPPacketReader parser = PARSER_CACHE.get();

        // The stanza handler processes the message
        try {
            handler.process((String) message, parser);
        } catch (Exception e) {
            log.error(
                    "Closing connection due to error while processing message: "
                            + message, e);
            Connection connection = (Connection) session
                    .getAttribute(CONNECTION);
            connection.close();
        }
    }

    /**
     * Invoked when a message written by IoSession.write(Object) is sent out.
     */
    public void messageSent(IoSession session, Object message) throws Exception {
        log.debug("messageSent()...");
    }
    
    abstract NIOConnection createNIOConnection(IoSession session);

    abstract StanzaHandler createStanzaHandler(NIOConnection connection);

    /**
     * Returns the max number of seconds a connection can be idle (both ways) before
     * being closed.<p>
     *
     * @return the max number of seconds a connection can be idle.
     */
    abstract int getMaxIdleTime();


}