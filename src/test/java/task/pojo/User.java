package task.pojo;

import lombok.*;

@Getter
@Setter
@ToString
public class User {
    public int id;
    public String name;
    public String username;
    public String email;
    public String phone;
    public String website;
    public Address address;
    public Company company;
}
