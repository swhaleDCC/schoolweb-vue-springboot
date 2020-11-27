package com.haibusiness.szweb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Menu{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String title;
	@UpdateTimestamp
	@Column(name = "update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;
	@JoinColumn(name = "update_user", referencedColumnName = "id")
	@OneToOne(cascade = CascadeType.DETACH)
	@JsonIgnore
	private User updateUser;
	@Basic(optional = false)
	private String name;
 	private String icon;
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "parent")
	@JsonIgnore
	private Menu parent;
	@OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY,mappedBy="parent")
	private List<Menu> items;
	@ManyToMany
	@JoinTable(name = "menu_Authority", joinColumns = {@JoinColumn(name = "menu_id",referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "authority_id",referencedColumnName = "id")})
	private Set<Authority> roles;
	@Override
	public String toString() {
		return name;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Menu menu = (Menu) o;
		return Objects.equals(name, menu.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
