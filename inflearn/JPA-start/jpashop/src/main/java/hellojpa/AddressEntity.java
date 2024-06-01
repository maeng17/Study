package hellojpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

//값타입 컬렉션 대안
@Entity
@Getter
@Setter
@Table(name = "ADDRESS")
public class AddressEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Address address;

    public AddressEntity() {}
    public AddressEntity(Address address) {
        this.address = address;
    }
    public  AddressEntity(String city, String street, String zip) {
        this.address = new Address(city, street, zip);
    }
}
