
package auction.web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for money complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="money">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="serialVersionUID" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="euro" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="currency" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="cents" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "money")
public class Money {

    @XmlAttribute(name = "serialVersionUID", required = true)
    protected long serialVersionUID;
    @XmlAttribute(name = "euro", required = true)
    protected String euro;
    @XmlAttribute(name = "currency", required = true)
    protected String currency;
    @XmlAttribute(name = "cents", required = true)
    protected long cents;

    /**
     * Gets the value of the serialVersionUID property.
     * 
     */
    public long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * Sets the value of the serialVersionUID property.
     * 
     */
    public void setSerialVersionUID(long value) {
        this.serialVersionUID = value;
    }

    /**
     * Gets the value of the euro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEuro() {
        return euro;
    }

    /**
     * Sets the value of the euro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEuro(String value) {
        this.euro = value;
    }

    /**
     * Gets the value of the currency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

    /**
     * Gets the value of the cents property.
     * 
     */
    public long getCents() {
        return cents;
    }

    /**
     * Sets the value of the cents property.
     * 
     */
    public void setCents(long value) {
        this.cents = value;
    }

}
