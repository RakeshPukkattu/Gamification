package com.zisbv.gamification.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zisbv.gamification.entities.Role;
import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.repositories.RoleName;

public class UserPrincipal implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private String emailId;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	private Map<String, Object> attributes;

	public UserPrincipal(Long id, String name, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.name = name;
		this.emailId = email;
		this.password = password;
		this.authorities = authorities;
	}

	public static UserPrincipal build(UserData user) {
		List<GrantedAuthority> authorities = convertSetToList(getAuthority(user));

		return new UserPrincipal(user.getId(), user.getUserName(), user.getEmail(), user.getPassword(),
				authorities);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return emailId;
	}

	@Override
	public String getUsername() {
		return emailId;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		UserPrincipal user = (UserPrincipal) o;
		return Objects.equals(id, user.id);
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	private static Set<SimpleGrantedAuthority> getAuthority(UserData user) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();

		Set<Role> roleSet = new HashSet<>();
		String roles = user.getRoles();

		if (roles != null) {
			String[] splitted = roles.split(",");
			for (String s : splitted) {
				if (s.equalsIgnoreCase("SUPERADMIN")) {
					Role data = new Role();
					data.setId(1);
					data.setName(RoleName.SUPERADMIN.toString());
					roleSet.add(data);
				}
				if (s.equalsIgnoreCase("ADMIN")) {
					Role data = new Role();
					data.setId(1);
					data.setName(RoleName.ADMIN.toString());
					roleSet.add(data);
				}
				if (s.equalsIgnoreCase("LEARNER")) {
					Role data = new Role();
					data.setId(1);
					data.setName(RoleName.LEARNER.toString());
					roleSet.add(data);
				}
			}
		}
		roleSet.forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
		});
		return authorities;
	}

	// Generic function to convert set to list
	@SuppressWarnings("unchecked")
	public static <T> List<GrantedAuthority> convertSetToList(Set<T> set) {
		// create an empty list
		List<T> list = new ArrayList<>();

		// push each element in the set into the list
		for (T t : set)
			list.add(t);

		// return the list
		List<GrantedAuthority> returnList = new ArrayList<GrantedAuthority>();
		returnList = (List<GrantedAuthority>) list;

		return returnList;
	}

}
