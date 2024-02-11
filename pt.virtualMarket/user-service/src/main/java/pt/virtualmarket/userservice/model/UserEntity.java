package pt.virtualmarket.userservice.model;

public class UserEntity {

    private String firstName;
    private String lastName;
    private String email;
    private int nif;
    private int password;

    public UserEntity(String firstName, String lastName, String email, int nif, int password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.nif = nif;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getNif() {
        return nif;
    }

    public int getPassword() {
        return password;
    }
}
