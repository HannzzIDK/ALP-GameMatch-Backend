package com.main.service;

import com.main.Model.AnswerWeight;
import com.main.Model.Game;
import com.main.Model.GameTierEnum;
import com.main.Repository.AnswerWeightRepo;
import com.main.Repository.GameRepo;
import com.main.dto.GameResponseDTO;
import com.main.dto.QuizSubmitRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchmakerService {

    private static final int TOP_N = 10;

    private final AnswerWeightRepo answerWeightRepo;
    private final GameRepo gameRepo;

    public List<GameResponseDTO> generateRecommendation(QuizSubmitRequestDTO dto) {
        List<Integer> optionIds = dto.getAnswers() == null
                ? List.of()
                : dto.getAnswers().stream()
                        .map(QuizSubmitRequestDTO.AnswerDTO::getAnswerOptionId)
                        .collect(Collectors.toList());

        List<AnswerWeight> weights = optionIds.isEmpty()
                ? List.of()
                : answerWeightRepo.findByAnswerOptionIds(optionIds);

        Map<Integer, Double> genreScoreMap = new HashMap<>();
        for (AnswerWeight aw : weights) {
            if (aw.getGenre() != null && aw.getWeightScore() != null) {
                genreScoreMap.merge(aw.getGenre().getGenreId(), aw.getWeightScore(), Double::sum);
            }
        }

        Float maxPrice = dto.getMaxBudget() != null ? dto.getMaxBudget().floatValue() : null;
        GameTierEnum tier = parseTier(dto.getTargetTier());

        List<Game> candidates = gameRepo.findGamesWithFilters(maxPrice, tier);

        return candidates.stream()
                .map(game -> {
                    double score = 0.0;
                    if (game.getGameGenres() != null) {
                        for (var gameGenre : game.getGameGenres()) {
                            if (gameGenre.getGenre() != null) {
                                score += genreScoreMap.getOrDefault(gameGenre.getGenre().getGenreId(), 0.0);
                            }
                        }
                    }
                    return new GameResponseDTO(game, score);
                })
                .sorted(Comparator.comparingDouble(GameResponseDTO::getMatchScore).reversed())
                .limit(TOP_N)
                .collect(Collectors.toList());
    }

    private GameTierEnum parseTier(String targetTier) {
        if (targetTier == null || targetTier.isBlank()) {
            return null;
        }
        try {
            return GameTierEnum.valueOf(targetTier.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
