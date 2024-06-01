package hellojpa;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter //불변객체로 만들기 - 값 변경 불가
public class Address {

    private String city;
    private String street;
    private String zipcode;

    public Address(){};
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }


}


