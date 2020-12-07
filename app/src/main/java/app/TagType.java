package app;

import java.io.Serializable;

/**
 * @author Karun Kanda
 * @author Yulin Ni
 *
 */

/**
 * TagType class defines the type for a tag.
 */
public class TagType implements Serializable {
	
	static final long serialVersionUID = -7005170714542958199L;
	
	/**
	 * Stores the type of the tag
	 */
	String name; 
	/**
	 * boolean multiplicity determines the multiplicity of a particular tag type that is allowed per photo. 
	 * false = only one allowed per photo
	 * true = multiple allowed per photo
	 */
	public boolean multiplicity;

	/**
	 * Creates a new TagType.
	 * @param name
	 * @param multiplicity
	 */
	public TagType(String name, boolean multiplicity) {
		this.name = name;
		this.multiplicity = multiplicity; 
	}
	
	/**
	 * Returns the tag type.
	 */
	public String toString() {
		return name; 
	}
}
