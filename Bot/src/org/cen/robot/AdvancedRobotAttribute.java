package org.cen.robot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Abstract base class of a robot attribute object based on a properties set.
 * 
 * @author Emmanuel ZURMELY
 */
public abstract class AdvancedRobotAttribute implements IRobotAttribute {
	private Map<String, Object> properties = new HashMap<String, Object>();

	/**
	 * Clears the properties set.
	 */
	public void clear() {
		properties.clear();
	}

	/**
	 * Determines whether the properties set contains the specified key.
	 * 
	 * @param key
	 *            the key to check
	 * @return true if the properties set contains the key, false otherwise
	 */
	public boolean containsKey(Object key) {
		return properties.containsKey(key);
	}

	/**
	 * Determines whether the properties set contains the specified value.
	 * 
	 * @param value
	 *            the value to check
	 * @return true if the properties set contains the value, false otherwise
	 */
	public boolean containsValue(Object value) {
		return properties.containsValue(value);
	}

	/**
	 * Returns the entry set of the properties set.
	 * 
	 * @return the entry set of the properties set
	 */
	public Set<Entry<String, Object>> entrySet() {
		return properties.entrySet();
	}

	@Override
	public boolean equals(Object o) {
		return properties.equals(o);
	}

	/**
	 * Returns the value of the given property.
	 * 
	 * @param key
	 *            the property key
	 * @return the value of the property or null if the property is not defined
	 */
	public Object get(Object key) {
		return properties.get(key);
	}

	/**
	 * Returns a map of the properties.
	 * 
	 * @return a map of the properties
	 */
	public Map<? extends Object, ? extends Object> getProperties() {
		return properties;
	}

	/**
	 * Returns the value of the given string property.
	 * 
	 * @param key
	 *            the property key
	 * @return the value of the property or null if the property is not defined
	 */
	public String getProperty(String key) {
		return (String) get(key);
	}

	@Override
	public int hashCode() {
		return properties.hashCode();
	}

	/**
	 * Determines whether the properties set is empty.
	 * 
	 * @return true if the properties set is empty, false otherwise
	 */
	public boolean isEmpty() {
		return properties.isEmpty();
	}

	/**
	 * Returns the key set of the properties set.
	 * 
	 * @return the key set of the properties set
	 */
	public Set<String> keySet() {
		return properties.keySet();
	}

	/**
	 * Puts a property in the properties set.
	 * 
	 * @param key
	 *            the property key
	 * @param value
	 *            the property value
	 * @return the old property value
	 */
	public Object put(String key, Object value) {
		return properties.put(key, value);
	}

	/**
	 * Puts all properties defined by the given map into the properties set.
	 * 
	 * @param m
	 *            a map of properties
	 */
	public void putAll(Map<? extends String, ? extends Object> m) {
		properties.putAll(m);
	}

	/**
	 * Remove the given property from the properties set.
	 * 
	 * @param key
	 *            the property key
	 * @return the value of the removed property
	 */
	public Object remove(Object key) {
		return properties.remove(key);
	}

	/**
	 * Sets the value of a string property.
	 * 
	 * @param key
	 *            the property key
	 * @param value
	 *            the property value
	 */
	public void setProperty(String key, String value) {
		put(key, value);
	}

	/**
	 * Returns the size of the properties set.
	 * 
	 * @return the size of the properties set
	 */
	public int size() {
		return properties.size();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + properties.toString() + "]";
	}

	/**
	 * Returns the values of the properties set.
	 * 
	 * @return the values of the properties set
	 */
	public Collection<Object> values() {
		return properties.values();
	}
}
