package freelance.home.comtrading.repository;

import freelance.home.comtrading.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepositoryAll extends JpaRepository<Item, Long> {
}
