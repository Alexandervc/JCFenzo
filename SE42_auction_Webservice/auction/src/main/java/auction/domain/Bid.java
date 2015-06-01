package auction.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import nl.fontys.util.FontysTime;
import nl.fontys.util.Money;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Bid {
    @Id
    @GeneratedValue
    @XmlAttribute (required = true)
    private Long id;
    
    @Embedded
    @XmlElement
    private FontysTime time;
    
    @ManyToOne
    @XmlElement
    private User buyer;
    
    @Embedded
    @XmlElement
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
