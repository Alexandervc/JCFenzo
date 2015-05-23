package auction.domain;

import java.util.Objects;
import javax.persistence.*;
import nl.fontys.util.Money;

@Entity
@Inheritance (strategy = InheritanceType.JOINED)
@NamedQueries ({
    @NamedQuery(name = "Item.count", query = "select count(i) from Item as i"),
    @NamedQuery(name = "Item.getAll", query = "select i from Item as i"),
    @NamedQuery(name = "Item.findByDescription", query = "select i from Item as i where i.description = :description")
})
public class Item implements Comparable {
    @Id 
    @GeneratedValue
    private Long id;
    
    @ManyToOne
    private User seller;
    
    @Embedded
    @AttributeOverride (name = "description", column = @Column(name = "c_description"))
    private Category category;
    private String description;
    
    @OneToOne (mappedBy = "bidItem", cascade = CascadeType.ALL)
    private Bid highest;
    
    public Item() { }

    public Item(User seller, Category category, String description) {
        this.seller = seller;
        this.category = category;
        this.description = description;     
        this.seller.addItem(this);
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
        highest.setItem(this);
        return highest;
    }

    @Override
    public int compareTo(Object arg0) {
        return Long.compare(this.id, ((Item)arg0).getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this.id != null && this.id.equals(((Item)o).getId())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }
}
