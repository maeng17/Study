package hellojpa;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Embeddable
@Getter
@Setter
public class Period {
    private LocalDate startDate;
    private LocalDate endDate;

    public Period() {}
    public Period(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

/*
임베디드 타입
 - 새로운 값 타입을 직접 정의할 수 있음
 - JPA는 임베디드 타입(embedded type)이라 함
 - 주로 기본 값 타입을 모아서 만들어서 복합 값 타입이라고도 함 int, String과 같은 값 타입

 - @Embeddable: 값 타입을 정의하는 곳에 표시
 - @Embedded: 값 타입을 사용하는 곳에 표시
 - 기본 생성자 필수
 */