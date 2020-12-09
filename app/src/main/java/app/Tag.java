package app;

import java.io.Serializable;

/**
 * @author Karun Kanda
 * @author Yulin Ni
 */

/**
 * The Tag class is where the tagName and tagValue will be stored for any photo.
 */
public class Tag implements Serializable {
	
	/**
	 * String tagName stores the name of the tag (e.g: location).
	 */
	String tagName;
	
	/**
	 * String tagValue stores the value of the tag (e.g: New Brunswick).
	 */
	String tagValue;

	/**
	 * Creates a new instance of Tag and initializes it.
	 * @param tagName
	 * @param tagValue
	 */
	public Tag(String tagName, String tagValue) {
		this.tagName = tagName;
		this.tagValue = tagValue;
	}
	
	/**
	 * getTagName gets the name of the tag.
	 * @return String
	 */
	public String getTagName() {
		return tagName;
	}
	
	public String getTagNameString() {
		return tagName;
	}
	
	/**
	 * getTagValue gets the value of the tag.
	 * @return String
	 */
	public String getTagValue() {
		return tagValue;
	}
	
	/**
	 * Returns the tag name and tag value.
	 */
	public String toString() {
		return tagName + ":" + tagValue;
	}
}
