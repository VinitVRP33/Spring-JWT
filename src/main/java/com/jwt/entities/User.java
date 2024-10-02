package com.jwt.entities;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	
	@Column(nullable = false,unique = true)
	private String userName;
	
	@Column(nullable = false)
	private String userPassword;
	
	@Column(nullable = false)
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	name = "userAuthorities",
	joinColumns = @JoinColumn(name="userId"),
	inverseJoinColumns=@JoinColumn(name="userAuthorityId")
	)
	private Set<Authority> authorities;
	
}
