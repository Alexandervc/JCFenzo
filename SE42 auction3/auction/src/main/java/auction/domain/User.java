package auction.domain;

import java.util.Iterator;
import java.util.Set;
import java.util.List;
import javax.persistence.*;

@Entity
@NamedQueries({
    @NamedQuery(name = "User.getAll", query = "select u from User as u"),
    @NamedQuery(name = "User.count", query = "select count(u) from User as u"),
    @NamedQuery(name = "User.findByEmail", query = "select u from User as u where u.email = :email")
})
public class User {

    @Id
    @GeneratedValue
    private Long Id;
    @Column(unique = true)
    private String email;
    
    @OneToMany(mappedBy = "seller")
    private Set<Item> offeredItems;

    public User() {
    }

    public User(String email) {
        this.email = email;
    }
    
    public Long getId() {
        return this.Id;
    }

    public String getEmail() {
        return email;
    }
    
    public Iterator<Item> getOfferedItems() {
        return offeredItems.iterator();
    }
    
    public void addItem(Item item) {
        offeredItems.add(item);
    }
    
    public int numberOfOfferedItems() {
        return offeredItems.size();
    }
}
