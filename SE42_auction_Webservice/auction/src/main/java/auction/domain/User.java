package auction.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@NamedQueries({
    @NamedQuery(name = "User.getAll", query = "select u from User as u"),
    @NamedQuery(name = "User.count", query = "select count(u) from User as u"),
    @NamedQuery(name = "User.findByEmail", query = "select u from User as u where u.email = :email")
})
public class User {

    @Id
    @GeneratedValue
    @XmlAttribute (required = true)
    private Long Id;
    
    @Column(unique = true)
    @XmlAttribute (required = true)
    private String email;

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
}
