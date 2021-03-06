package org.soxmpp.server.xmpp.net;

import java.net.UnknownHostException;

import org.soxmpp.server.xmpp.UnauthenticatedException;
import org.soxmpp.server.xmpp.session.LocalSession;
import org.xmpp.packet.Packet;

public interface Connection {
	/**
	 * Verifies that the connection is still live. Typically this is done by
	 * sending a whitespace character between packets.
	 * 
	 * @return true if the socket remains valid, false otherwise.
	 */
	public boolean validate();

	/**
	 * Initializes the connection with it's owning session. Allows the
	 * connection class to configure itself with session related information
	 * (e.g. stream ID).
	 * 
	 * @param session
	 *            the session that owns this connection
	 */
	public void init(LocalSession session);

	/**
	 * Returns the raw IP address of this <code>InetAddress</code> object. The
	 * result is in network byte order: the highest order byte of the address is
	 * in <code>getAddress()[0]</code>.
	 * 
	 * @return the raw IP address of this object.
	 * @throws java.net.UnknownHostException
	 *             if IP address of host could not be determined.
	 */
	public byte[] getAddress() throws UnknownHostException;

	/**
	 * Returns the IP address string in textual presentation.
	 * 
	 * @return the raw IP address in a string format.
	 * @throws java.net.UnknownHostException
	 *             if IP address of host could not be determined.
	 */
	public String getHostAddress() throws UnknownHostException;

	/**
	 * Gets the host name for this IP address.
	 * 
	 * <p>
	 * If this InetAddress was created with a host name, this host name will be
	 * remembered and returned; otherwise, a reverse name lookup will be
	 * performed and the result will be returned based on the system configured
	 * name lookup service. If a lookup of the name service is required, call
	 * {@link java.net.InetAddress#getCanonicalHostName() getCanonicalHostName}.
	 * 
	 * <p>
	 * If there is a security manager, its <code>checkConnect</code> method is
	 * first called with the hostname and <code>-1</code> as its arguments to
	 * see if the operation is allowed. If the operation is not allowed, it will
	 * return the textual representation of the IP address.
	 * 
	 * @return the host name for this IP address, or if the operation is not
	 *         allowed by the security check, the textual representation of the
	 *         IP address.
	 * @throws java.net.UnknownHostException
	 *             if IP address of host could not be determined.
	 * 
	 * @see java.net.InetAddress#getCanonicalHostName
	 * @see SecurityManager#checkConnect
	 */
	public String getHostName() throws UnknownHostException;

	/**
	 * Close this session including associated socket connection. The order of
	 * events for closing the session is:
	 * <ul>
	 * <li>Set closing flag to prevent redundant shutdowns.
	 * <li>Call notifyEvent all listeners that the channel is shutting down.
	 * <li>Close the socket.
	 * </ul>
	 */
	public void close();

	/**
	 * Notification message indicating that the server is being shutdown.
	 * Implementors should send a stream error whose condition is
	 * system-shutdown before closing the connection.
	 */
	public void systemShutdown();

	/**
	 * Returns true if the connection/session is closed.
	 * 
	 * @return true if the connection is closed.
	 */
	public boolean isClosed();

	/**
	 * Returns true if this connection is secure.
	 * 
	 * @return true if the connection is secure (e.g. SSL/TLS)
	 */
	public boolean isSecure();

	/**
	 * Registers a listener for close event notification. Registrations after
	 * the Session is closed will be immediately notified <em>before</em> the
	 * registration call returns (within the context of the registration call).
	 * An optional handback object can be associated with the registration if
	 * the same listener is registered to listen for multiple connection
	 * closures.
	 * 
	 * @param listener
	 *            the listener to register for events.
	 *            the object to send in the event notification.
	 */
	public void registerCloseListener(ConnectionCloseListener listener);

	/**
	 * Removes a registered close event listener. Registered listeners must be
	 * able to receive close events up until the time this method returns. (i.e.
	 * it is possible to call unregister, receive a close event registration,
	 * and then have the unregister call return.)
	 * 
	 * @param listener
	 *            the listener to deregister for close events.
	 */
	public void removeCloseListener(ConnectionCloseListener listener);

	/**
	 * Delivers the packet to this connection without checking the recipient.
	 * The method essentially calls
	 * <code>socket.send(packet.getWriteBuffer())</code>.
	 * 
	 * @param packet
	 *            the packet to deliver.
	 * @throws org.jivesoftware.openfire.auth.UnauthorizedException
	 *             if a permission error was detected.
	 */
	public void deliver(Packet packet) throws UnauthenticatedException;

	/**
	 * Delivers raw text to this connection. This is a very low level way for
	 * sending XML stanzas to the client. This method should not be used unless
	 * you have very good reasons for not using
	 * {@link #deliver(org.xmpp.packet.Packet)}.
	 * <p>
	 * 
	 * This method avoids having to get the writer of this connection and mess
	 * directly with the writer. Therefore, this method ensures a correct
	 * delivery of the stanza even if other threads were sending data
	 * concurrently.
	 * 
	 * @param text
	 *            the XML stanzas represented kept in a String.
	 */
	public void deliverRawText(String text);

	/**
	 * Returns the major version of XMPP being used by this connection
	 * (major_version.minor_version. In most cases, the version should be "1.0".
	 * However, older clients using the "Jabber" protocol do not set a version.
	 * In that case, the version is "0.0".
	 * 
	 * @return the major XMPP version being used by this connection.
	 */
	public int getMajorXMPPVersion();

	/**
	 * Returns the minor version of XMPP being used by this connection
	 * (major_version.minor_version. In most cases, the version should be "1.0".
	 * However, older clients using the "Jabber" protocol do not set a version.
	 * In that case, the version is "0.0".
	 * 
	 * @return the minor XMPP version being used by this connection.
	 */
	public int getMinorXMPPVersion();

	/**
	 * Sets the XMPP version information. In most cases, the version should be
	 * "1.0". However, older clients using the "Jabber" protocol do not set a
	 * version. In that case, the version is "0.0".
	 * 
	 * @param majorVersion
	 *            the major version.
	 * @param minorVersion
	 *            the minor version.
	 */
	public void setXMPPVersion(int majorVersion, int minorVersion);

	/**
	 * Returns the language code that should be used for this connection (e.g.
	 * "en").
	 * 
	 * @return the language code for the connection.
	 */
	public String getLanguage();

	/**
	 * Sets the language code that should be used for this connection (e.g.
	 * "en").
	 * 
	 * @param language
	 *            the language code.
	 */
	public void setLanaguage(String language);

	/**
	 * Returns whether TLS is mandatory, optional or is disabled. When TLS is
	 * mandatory clients are required to secure their connections or otherwise
	 * their connections will be closed. On the other hand, when TLS is disabled
	 * clients are not allowed to secure their connections using TLS. Their
	 * connections will be closed if they try to secure the connection. in this
	 * last case.
	 * 
	 * @return whether TLS is mandatory, optional or is disabled.
	 */
	TLSPolicy getTlsPolicy();

	/**
	 * Sets whether TLS is mandatory, optional or is disabled. When TLS is
	 * mandatory clients are required to secure their connections or otherwise
	 * their connections will be closed. On the other hand, when TLS is disabled
	 * clients are not allowed to secure their connections using TLS. Their
	 * connections will be closed if they try to secure the connection. in this
	 * last case.
	 * 
	 * @param tlsPolicy
	 *            whether TLS is mandatory, optional or is disabled.
	 */
	void setTlsPolicy(TLSPolicy tlsPolicy);


	/**
	 * Secures the plain connection by negotiating TLS with the other peer. In a
	 * server-2-server connection the server requesting the TLS negotiation will
	 * be the client and the other server will be the server during the TLS
	 * negotiation. Therefore, the server requesting the TLS negotiation must
	 * pass <code>true</code> in the <tt>clientMode</tt> parameter and the
	 * server receiving the TLS request must pass <code>false</code> in the
	 * <tt>clientMode</tt> parameter. Both servers should specify the xmpp
	 * domain of the other server in the <tt>remoteServer</tt> parameter.
	 * <p>
	 * 
	 * In the case of client-2-server the XMPP server must pass
	 * <code>false</code> in the <tt>clientMode</tt> parameter since it will
	 * behave as the server in the TLS negotiation. The <tt>remoteServer</tt>
	 * parameter will always be <tt>null</tt>.
	 * 
	 * @param authentication
	 *            policy to use for authenticating the remote peer.
	 * @throws Exception
	 *             if an error occured while securing the connection.
	 */
	void startTLS( ClientAuth authentication) throws Exception;

	/**
	 * Enumeration of possible TLS policies required to interact with the
	 * server.
	 */
	enum TLSPolicy {

		/**
		 * TLS is required to interact with the server. Entities that do not
		 * secure their connections using TLS will get a stream error and their
		 * connections will be closed.
		 */
		required,

		/**
		 * TLS is optional to interact with the server. Entities may or may not
		 * secure their connections using TLS.
		 */
		optional,

		/**
		 * TLS is not available. Entities that request a TLS negotiation will
		 * get a stream error and their connections will be closed.
		 */
		disabled
	}

	/**
	 * Enumeration that specifies if clients should be authenticated (and how)
	 * while negotiating TLS.
	 */
	enum ClientAuth {

		/**
		 * No authentication will be performed on the client. Client credentials
		 * will not be verified while negotiating TLS.
		 */
		disabled,

		/**
		 * Clients will try to be authenticated. Unlike {@link #needed}, if the
		 * client chooses not to provide authentication information about
		 * itself, the TLS negotiations will stop and the connection will be
		 * dropped. This option is only useful for engines in the server mode.
		 */
		wanted,

		/**
		 * Clients need to be authenticated. Unlike {@link #wanted}, if the
		 * client chooses not to provide authentication information about
		 * itself, the TLS negotiations will continue. This option is only
		 * useful for engines in the server mode.
		 */
		needed
	}
}
