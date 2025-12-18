package com.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.library.entity.LibraryUserPrincipal;
import com.library.entity.User;
import com.library.repository.UserRepository;



@Service
public class LibraryUserDetailsService implements UserDetailsService{
	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepo.findOneByUserName(username)
				  .orElseThrow(()->new UsernameNotFoundException("user not found !!"));
		
		
		return new LibraryUserPrincipal(user);
	}
}
