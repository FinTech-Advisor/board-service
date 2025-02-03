package org.advisor.board.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.advisor.global.paging.CommonSearch;
import org.advisor.member.contants.Authority;

import java.util.List;

@Data
public class BoardConfigSearch extends CommonSearch {

    @NotBlank
    private List<String> bid;

}
