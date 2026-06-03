package com.main.Favorite;
import org.springframework.data.repository.CrudRepository;

public interface FavoriteRepo extends CrudRepository<Favorite, Long> {
    
}
