package entity;

import javax.persistence.*;

@Entity
@Table(name = "user_access")
public class UserAccess {
    private int id;
    private User user;
    private String password;
    private String token;
    private int passwordHash;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "_user")
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "token")
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    @Basic
    @Column(name = "_hash")
    public int getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(int passwordHash) {
        this.passwordHash = passwordHash;
    }
}
