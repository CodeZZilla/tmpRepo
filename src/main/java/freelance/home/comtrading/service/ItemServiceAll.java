package freelance.home.comtrading.service;

import freelance.home.comtrading.domain.item.Item;
import freelance.home.comtrading.repository.ItemRepositoryAll;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceAll {
    private final ItemRepositoryAll itemRepositoryAll;

    public List<Item> findAll(){
        return itemRepositoryAll.findAll();
    }
}
