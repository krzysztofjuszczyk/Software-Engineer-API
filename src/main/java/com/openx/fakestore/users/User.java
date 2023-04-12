package com.openx.fakestore.users;

public class User {
    private int id;
    private String email;
    private String username;
    private String password;
    private Name name;

    private Address address;
    private String phone;

    public double getDistance(User otherUser) {
        double lat1 = Double.parseDouble(this.getAddress().getGeolocation().getLat());
        double lon1 = Double.parseDouble(this.getAddress().getGeolocation().getLng());
        double lat2 = Double.parseDouble(otherUser.getAddress().getGeolocation().getLat());
        double lon2 = Double.parseDouble(otherUser.getAddress().getGeolocation().getLng());

        // Calculate distance using Haversine formula
        int earthRadius = 6371; // kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        return distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return (name.firstname + " " + name.lastname);
    }

    public void setName(Name name) {
        this.name = name;
    }

    public static class Name {
        private String firstname;
        private String lastname;

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }
    }
}

