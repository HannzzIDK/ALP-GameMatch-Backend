package com.main.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.main.Model.Game;
public interface GameRepo extends JpaRepository <Game, Long> {

}
