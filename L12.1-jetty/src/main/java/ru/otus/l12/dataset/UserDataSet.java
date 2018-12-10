package ru.otus.l12.dataset;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class UserDataSet extends DataSet {

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @OneToOne(cascade = CascadeType.ALL)
    private AddressDataSet address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PhoneDataSet> phones;

    public UserDataSet(){ }

    public UserDataSet(String name, int age){
        this.name = name;
        this.age = age;
    }

    public UserDataSet(String name, int age, AddressDataSet address, List<PhoneDataSet> phones){
        this.name = name;
        this.age = age;

        this.address = address;

        this.phones = phones;
        for (PhoneDataSet p : phones){
            p.setUser(this);
        }
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void addPhone(PhoneDataSet phone){
        phones.add(phone);
        phone.setUser(this);
    }

    public void removePhone(PhoneDataSet phone){
        phones.remove(phone);
    }

    public AddressDataSet getAddress(){
        return address;
    }

    public void addAddress(AddressDataSet address){
        this.address = address;
        // address.setUser(this);
    }

    @Override
    public String toString() {
        return "UserDataSet {" +
                "id = '" + getId() + '\'' +
                ", name = '" + name + '\'' +
                ", address = " + address +
                ", phone = " + phones +
                '}';
    }
}
