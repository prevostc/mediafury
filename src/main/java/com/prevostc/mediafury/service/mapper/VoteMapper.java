package com.prevostc.mediafury.service.mapper;

import com.prevostc.mediafury.domain.*;
import com.prevostc.mediafury.service.dto.VoteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Vote and its DTO VoteDTO.
 */
@Mapper(componentModel = "spring", uses = {MovieMapper.class})
public interface VoteMapper extends EntityMapper<VoteDTO, Vote> {

    @Mapping(source = "winner.id", target = "winnerId")
    @Mapping(source = "winner.title", target = "winnerTitle")
    @Mapping(source = "loser.id", target = "loserId")
    @Mapping(source = "loser.title", target = "loserTitle")
    VoteDTO toDto(Vote vote);

    @Mapping(source = "winnerId", target = "winner")
    @Mapping(source = "loserId", target = "loser")
    Vote toEntity(VoteDTO voteDTO);

    default Vote fromId(Long id) {
        if (id == null) {
            return null;
        }
        Vote vote = new Vote();
        vote.setId(id);
        return vote;
    }
}
