package com.maxtrain.prs.request;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.maxtrain.prs.request.Request;
import com.maxtrain.prs.request.RequestRepository;
import com.maxtrain.prs.requestLine.RequestLine;
import com.maxtrain.prs.requestLine.RequestLineRepository;

@Controller
@RequestMapping("/api/requests")
public class RequestController {

	@Autowired
	private RequestRepository requestRepository;
	@Autowired
	private RequestLineRepository requestLineRepository;

	@GetMapping()
	public @ResponseBody Iterable<Request> GetAll() {
		return requestRepository.findAll();
	}
	@GetMapping("{id}")
	public @ResponseBody Optional<Request> Get(@PathVariable Integer id) {
		try {
			Optional<Request> request = requestRepository.findById(id);
			if(!request.isPresent()) return null;
			return request;
		} catch (IllegalArgumentException ex) {
			throw ex;
		}
	}
	@PostMapping()
	public @ResponseBody Request Insert(@RequestBody Request request) throws Exception {
		if(request == null) {
			throw new Exception("Request instance cannot be null.");
		}
		return requestRepository.save(request);
	}
	@PutMapping("{id}")
	public @ResponseBody Request Update(@PathVariable Integer id, @RequestBody Request request) throws Exception {
		if(request == null) {
			throw new Exception("Request instance cannot be null.");
		}
		if(id != request.getId()) {
			throw new Exception("Id of request instance does not match route parameter.");
		}
		return requestRepository.save(request);
	}
	@DeleteMapping()
	public @ResponseBody void Delete(@RequestBody Request request) throws Exception {
		if(request == null) {
			throw new Exception("Request instance cannot be null.");
		}
		try {
			requestRepository.delete(request);
		} catch (IllegalArgumentException ex) {
			throw ex;
		}
	}
	@DeleteMapping("{id}")
	public @ResponseBody void Delete(@PathVariable Integer id) throws Exception {
		Optional<Request> request = requestRepository.findById(id);
		if(!request.isPresent()) {
			throw new Exception("Request not found.");
		}
		Delete(request.get());
	}
}
