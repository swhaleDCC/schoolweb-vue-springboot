package com.haibusiness.szweb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 *
 * @author Jinyu
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "scientific")
public class Scientific extends BaseEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)

    private Long id;
    @Lob
    private String content;
    private String type;
}
