package auction.domain;

import javax.persistence.*;
import nl.fontys.util.Money;

@Entity
@NamedQueries ({
    @NamedQuery(name = "Item.count", query = "select count(i) from Item as i"),
    @NamedQuery(name = "Item.getAll", query = "select i from Item as i"),
    @NamedQuery(name = "Item.findByDescription", query = "select i from Item as i where i.description = :description")
})
public class Item implements Comparable {
    @Id 
    @GeneratedValue
    private Long id;
    
    @ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private User seller;
    
    @Embedded
    private Category category;
    private String description;
    
    @OneToOne (cascade = CascadeType.ALL)
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
