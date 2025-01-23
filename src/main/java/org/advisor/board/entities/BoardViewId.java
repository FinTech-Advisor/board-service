package org.advisor.board.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BoardViewId {
    private Long seq; // 필요한 데이터값에 seq 추가
    private int hash; // 필요한 데이터 값에 hash 추가
}

