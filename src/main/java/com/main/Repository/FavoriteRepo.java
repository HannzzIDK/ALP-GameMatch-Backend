package com.main.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.main.Model.Favorite;

public interface FavoriteRepo extends JpaRepository<Favorite, Long> {
    
}
