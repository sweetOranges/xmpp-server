package org.soxmpp.server.xmpp.cache;


public interface CacheFactory {
	public String get(String key);

	public void set(String key, String val);

	public boolean delete(String key);
}
