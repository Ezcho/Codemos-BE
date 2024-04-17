package com.tools.codemos.service;

import com.tools.codemos.dto.RankingDTO;
import com.tools.codemos.model.RankingEntity;
import com.tools.codemos.repository.RankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RankingService {

    @Autowired
    private RankingRepository rankingRepository;
    public Page<RankingDTO> getRanking(int pageNo) {
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("score").descending());
        Page<RankingEntity> rankingPage = rankingRepository.findAll(pageRequest);
        List<RankingDTO> dtos = rankingPage.getContent().stream()
                .map(entity -> new RankingDTO(
                        entity.getId(),
                        entity.getScore(),
                        entity.getUser().getEmail(),
                        entity.getUser().getNickname(),
                        entity.getTime(),
                        entity.getCode().getId()))
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageRequest, rankingPage.getTotalElements());
    }
}
