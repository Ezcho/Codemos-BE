package com.tools.codemos.service;

import com.tools.codemos.dto.CodeDTO;
import com.tools.codemos.model.CodeEntity;
import com.tools.codemos.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CodeService {

    @Autowired
    private CodeRepository codeRepository;

    public List<CodeDTO> findCodesByUserId(Long userId) {
        List<CodeEntity> codes = codeRepository.findByUserId(userId);
        return codes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private CodeDTO convertToDTO(CodeEntity codeEntity) {
        return new CodeDTO(codeEntity.getId(), codeEntity.getLeaderBoard().getScore(), codeEntity.getCode());
    }
}
