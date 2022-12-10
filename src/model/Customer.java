package model;

import java.util.regex.Pattern;

public class Customer {
    private String firstName;
    private String lastName;
    private String email;
    public final Pattern pattern = Pattern.compile("^(.+)@(.+).(.+)$");

    public Customer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        if(pattern.matcher(email).matches()) {
            this.email = email;
        }
        else {
            throw new IllegalArgumentException("Please enter a valid email address");
        }
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "FirstName = " + firstName +
                ", LastName= " + lastName +
                ", email= " + email;
    }
}
