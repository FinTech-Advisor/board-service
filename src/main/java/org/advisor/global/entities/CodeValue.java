package org.advisor.global.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash(timeToLive = 300) // 5분간 값을 유지, 5분 지난 후 데이터 삭제
public class CodeValue implements Serializable {
    @Id
    private String code;
    private Object value;
}
