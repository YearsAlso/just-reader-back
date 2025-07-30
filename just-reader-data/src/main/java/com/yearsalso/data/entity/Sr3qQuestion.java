package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "sr3q_question")
@TableName("sr3q_question")
@Schema(description = "sr3q-提问")
public class Sr3qQuestion extends BaseEntity implements Serializable {
}
