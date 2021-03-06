package auction.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.*;
import nl.fontys.util.Money;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@NamedQueries ({
    @NamedQuery(name = "Item.count", query = "select count(i) from Item as i"),
    @NamedQuery(name = "Item.getAll", query = "select i from Item as i"),
    @NamedQuery(name = "Item.findByDescription", query = "select i from Item as i where i.description = :description")
})
public class Item implements Comparable {
    @Id 
    @GeneratedValue
    @XmlAttribute (required = true)
    private Long id;
    
    @ManyToOne
    @XmlElement
    private User seller;
    
    @Embedded
    @AttributeOverride (name = "description", column = @Column(name = "c_description"))
    @XmlElement
    private Category category;
    
    @XmlAttribute (required = true)
    private String description;
    
    @OneToOne (cascade = CascadeType.ALL)
    @XmlElement
    private Bid highest;
    
    public Item() { }

    public Item(User seller, Category category, String description) {
        this.seller = seller;
        this.category = category;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public User getSeller() {
        return seller;
    }

    public Category getCategory() {
        return category;
    }
    
    public String getDescription() {
        return description;
    }

    public Bid getHighestBid() {
        return highest;
    }

    public Bid newBid(User buyer, Money amount) {
        
        if (highest != null && highest.getAmount().compareTo(amount) >= 0) {
            return null;
        }
        highest = new Bid(buyer, amount);
        return highest;
    }

    @Override
    public int compareTo(Object arg0) {
        //TODO
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        //TODO
        return false;
    }

    @Override
    public int hashCode() {
        //TODO
        return 0;
    }
}
