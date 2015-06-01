
package auction.web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for item complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="item">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="seller" type="{http://web.auction/}user" minOccurs="0"/>
 *         &lt;element name="category" type="{http://web.auction/}category" minOccurs="0"/>
 *         &lt;element name="highest" type="{http://web.auction/}bid" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="description" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "item", propOrder = {
    "seller",
    "category",
    "highest"
})
public class Item {

    protected User seller;
    protected Category category;
    protected Bid highest;
    @XmlAttribute(name = "id", required = true)
    protected long id;
    @XmlAttribute(name = "description", required = true)
    protected String description;

    /**
     * Gets the value of the seller property.
     * 
     * @return
     *     possible object is
     *     {@link User }
     *     
     */
    public User getSeller() {
        return seller;
    }

    /**
     * Sets the value of the seller property.
     * 
     * @param value
     *     allowed object is
     *     {@link User }
     *     
     */
    public void setSeller(User value) {
        this.seller = value;
    }

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link Category }
     *     
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link Category }
     *     
     */
    public void setCategory(Category value) {
        this.category = value;
    }

    /**
     * Gets the value of the highest property.
     * 
     * @return
     *     possible object is
     *     {@link Bid }
     *     
     */
    public Bid getHighest() {
        return highest;
    }

    /**
     * Sets the value of the highest property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bid }
     *     
     */
    public void setHighest(Bid value) {
        this.highest = value;
    }

    /**
     * Gets the value of the id property.
     * 
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(long value) {
        this.id = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

}
