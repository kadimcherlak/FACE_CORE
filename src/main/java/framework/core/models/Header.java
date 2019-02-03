package framework.core.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	"messageType",
})
public class Header {

	@XmlElement(name = "Message_Type", required = true)
	protected String messageType;

	/**
	 * Gets the value of the messageType property.
	 *
	 * @return possible object is
	 * {@link String }
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * Sets the value of the messageType property.
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setMessageType(String value) {
		this.messageType = value;
	}
}
