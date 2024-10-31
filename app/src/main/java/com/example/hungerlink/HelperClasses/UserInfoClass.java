//package com.example.hungerlink;
//
//public class UserInfoClass {
//    String name,email, phoneNo, address;
//
//    public UserInfoClass() {}
//
//    public UserInfoClass(String name, String email, String phoneNo, String address) {
//        this.name = name;
//        this.phoneNo = phoneNo;
//        this.address = address;
//        this.email = email;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPhoneNo() {
//        return phoneNo;
//    }
//
//    public void setPhoneNo(String phoneNo) {
//        this.phoneNo = phoneNo;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setBlood(String address) {
//        this.address = address;
//    }
//
//}

package com.example.hungerlink.HelperClasses;

// Class to represent user information
public class UserInfoClass {
    private String name;      // User's name
    private String email;     // User's email
    private String phoneNo;   // User's phone number
    private String address;    // User's address

    // Default constructor for Firebase or other purposes
    public UserInfoClass() {}

    // Parameterized constructor to initialize user information
    public UserInfoClass(String name, String email, String phoneNo, String address) {
        this.name = name;      // Set the user's name
        this.phoneNo = phoneNo; // Set the user's phone number
        this.address = address;  // Set the user's address
        this.email = email;     // Set the user's email
    }

    // Getter method for name
    public String getName() {
        return name; // Return the user's name
    }

    // Setter method for name
    public void setName(String name) {
        this.name = name; // Update the user's name
    }

    // Getter method for email
    public String getEmail() {
        return email; // Return the user's email
    }

    // Setter method for email
    public void setEmail(String email) {
        this.email = email; // Update the user's email
    }

    // Getter method for phone number
    public String getPhoneNo() {
        return phoneNo; // Return the user's phone number
    }

    // Setter method for phone number
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo; // Update the user's phone number
    }

    // Getter method for address
    public String getAddress() {
        return address; // Return the user's address
    }

    // Setter method for address
    public void setAddress(String address) {
        this.address = address; // Update the user's address
    }
}
