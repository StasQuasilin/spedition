package ua.svasilina.spedition.entity;

public class User {
    private int id;
    private Person person;
    private Role role;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }
    public void setPerson(Person person) {
        this.person = person;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
