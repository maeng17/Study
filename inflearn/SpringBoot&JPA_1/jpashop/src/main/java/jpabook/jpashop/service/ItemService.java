package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional // overide해서 저장
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    //준영속 엔티티 수정 방법 - 1) 변경 감지 기능 사용
    @Transactional //commit
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId); //영속 상태
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }


    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }
}

/*
변경 감지 기능: 원하는 속성만 선택 변경
병합: 모든 속성이 변경. 병합시 값이 없으면 `null` 로 업데이트 (병합은 모든 필드를 교체한다.)
 -> 실무에서는 변경 감지 사용 권장
 */