package entity;

import org.json.simple.JSONObject;

import javax.persistence.*;

import static constants.Keys.ID;
import static constants.Keys.PERSON;

@Entity
@Table(name = "users")
public class User extends JsonAble{
    private int id;
    private User supervisor;
    private Person person;
    private Role role;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "supervisor")
    public User getSupervisor() {
        return supervisor;
    }
    public void setSupervisor(User supervisor) {
        this.supervisor = supervisor;
    }

    @OneToOne
    @JoinColumn(name = "person")
    public Person getPerson() {
        return person;
    }
    public void setPerson(Person person) {
        this.person = person;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "role")
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = getJsonObject();
        json.put(ID, id);
        json.put(PERSON, person.toJson());
        return json;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return getClass() == obj.getClass() && hashCode() == obj.hashCode();
    }
}
