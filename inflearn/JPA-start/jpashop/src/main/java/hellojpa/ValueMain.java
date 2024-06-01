package hellojpa;

public class ValueMain {

    public static void main(String[] args) {
        int a = 10;
        int b = 10;

        System.out.println("a == b " + (a == b));


        Address address1 = new Address("city", "street", "10000");
        Address address2 = new Address("city", "street", "10000");

        System.out.println("address1 == address2 " + (address1 == address2)); // 동일성 비교 : 인스턴스 참조값 비교 / 인스턴스 자체가 다르기 때문에 false
        System.out.println("address1.equals(address2) " + (address1.equals(address2))); //동등성 비교 : 인스턴스의 값을 비교
    }
}
