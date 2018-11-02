package com.baosong.supplyme.service.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baosong.supplyme.domain.AttachmentFile;
import com.baosong.supplyme.service.dto.AttachmentFileDTO;

import org.springframework.stereotype.Service;

/**
 * Mapper for the entity User and its DTO called UserDTO.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class AttachmentFileMapper {

    public AttachmentFileDTO fromEntityToDTO(AttachmentFile af) {
        return new AttachmentFileDTO(af);
    }

    public List<AttachmentFileDTO> fromEntitiesToDTOs(List<AttachmentFile> afs) {
        return afs.stream()
            .filter(Objects::nonNull)
            .map(this::fromEntityToDTO)
            .collect(Collectors.toList());
    }

    public AttachmentFile fromDTOToEntity(AttachmentFileDTO afDTO) {
        if (afDTO == null) {
            return null;
        } else {
            AttachmentFile af = new AttachmentFile();
            af.setId(afDTO.getId());
            af.setName(afDTO.getName());
            af.setType(afDTO.getType());
            af.setSize(afDTO.getSize());
            return af;
        }
    }

    public List<AttachmentFile> fromDTOsToEntities(List<AttachmentFileDTO> afDTOs) {
        return afDTOs.stream()
            .filter(Objects::nonNull)
            .map(this::fromDTOToEntity)
            .collect(Collectors.toList());
    }

    public AttachmentFile fromId(Long id) {
        if (id == null) {
            return null;
        }
        AttachmentFile af = new AttachmentFile();
        af.setId(id);
        return af;
    }
}
