package org.soxmpp.server.xmpp.session;

import java.util.Collection;

import org.soxmpp.server.xmpp.net.Connection;
import org.xmpp.packet.JID;

public interface SessionManager {
	
	 /**
     * Creates a new ClientSession and returns it.
     *  
     * @param conn the connection
     * @return a newly created session
     */
	public ClientSession createClientSession(Connection conn);
	 /**
     * Adds a new session that has been authenticated. 
     *  
     * @param session the session
     */
    public void addSession(ClientSession session);
    /**
     * Returns the session associated with the username.
     * 
     * @param username the username of the client address
     * @return the session associated with the username
     */
    public ClientSession getSession(String username);
    /**
     * Returns the session associated with the JID.
     * 
     * @param from the client address
     * @return the session associated with the JID
     */
    public ClientSession getSession(JID from);
    /**
     * Returns a list that contains all authenticated client sessions.
     * 
     * @return a list that contains all client sessions
     */
    public Collection<ClientSession> getSessions();
    /**
     * Removes a client session.
     * 
     * @param session the session to be removed
     * @return true if the session was successfully removed 
     */
    public boolean removeSession(ClientSession session);
    /**
     * Closes the all sessions. 
     */
    public void closeAllSessions() ;
}
