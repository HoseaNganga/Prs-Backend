package com.maxtrain.prs.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping()
	public @ResponseBody Iterable<User> GetAll() {
		return userRepository.findAll();
	}
	@GetMapping("{id}")
	public @ResponseBody Optional<User> Get(@PathVariable Integer id) {
		try {
			Optional<User> user = userRepository.findById(id);
			if(!user.isPresent()) {
				return null;
			}
			return user;
		} catch (IllegalArgumentException ex) {
			throw ex;
		}
	}
	@PostMapping()
	public @ResponseBody User Insert(@RequestBody User user) throws Exception {
		if(user == null) {
			throw new Exception("User instance cannot be null.");
		}
		return userRepository.save(user);
	}
	@PutMapping("{id}")
	public @ResponseBody User Update(@PathVariable Integer id, @RequestBody User user) throws Exception {
		if(user == null) {
			throw new Exception("User instance cannot be null.");
		}
		if(id != user.getId()) {
			throw new Exception("Id of user instance does not match route parameter.");
		}
		return userRepository.save(user);
	}
	@DeleteMapping()
	public @ResponseBody void Delete(@RequestBody User user) throws Exception {
		if(user == null) {
			throw new Exception("User instance cannot be null.");
		}
		try {
			userRepository.delete(user);
		} catch (IllegalArgumentException ex) {
			throw ex;
		}
	}
	@DeleteMapping("{id}")
	public @ResponseBody void Delete(@PathVariable Integer id) throws Exception {
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent()) {
			throw new Exception("User not found.");
		}
		Delete(user.get());
	}
}
