package com.haibusiness.szweb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@MappedSuperclass
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer hit=0;
    @Basic(optional = false)
    private String title;
    @UpdateTimestamp
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @JoinColumn(name = "update_user", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.DETACH)
    @JsonIgnore
    private User updateUser;
}
