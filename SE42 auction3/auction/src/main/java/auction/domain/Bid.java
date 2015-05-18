package auction.domain;

import javax.persistence.*;
import nl.fontys.util.FontysTime;
import nl.fontys.util.Money;

@Entity
public class Bid {
    @Id
    @GeneratedValue
    private Long id;
    
    @Embedded
    private FontysTime time;
    
    @ManyToOne
    private User buyer;
    
    @Embedded
    private Money amount;

    public Bid() { }
    
    public Bid(User buyer, Money amount) {
        this.buyer = buyer;
        this.amount = amount;
        this.time = new FontysTime();
    }

    public FontysTime getTime() {
        return time;
    }

    public User getBuyer() {
        return buyer;
    }

    public Money getAmount() {
        return amount;
    }
}
