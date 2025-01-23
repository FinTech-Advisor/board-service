package org.advisor.board.controllers;

import lombok.Data;
import org.advisor.global.paging.CommonSearch;

import java.util.List;

@Data
public class BoardConfigSearch extends CommonSearch {
    private List<String> bid;
}
