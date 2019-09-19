package com.maxtrain.prs.requestLine;

import java.text.DecimalFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.maxtrain.prs.request.Request;
import com.maxtrain.prs.request.RequestRepository;
import com.maxtrain.prs.requestLine.RequestLine;
import com.maxtrain.prs.requestLine.RequestLineRepository;

@Controller
@RequestMapping("/api/requestLines")

public class RequestLineController {


	@Autowired
	private RequestLineRepository requestLineRepository;
	@Autowired
	private RequestRepository requestRepository;

	public void RecalculateRequestTotal(int requestId) {
		Optional<Request> request = requestRepository.findById(requestId);
		Iterable<RequestLine> requestLines = requestLineRepository.findByRequestId(requestId);
		double total = 0;
		for(RequestLine line : requestLines) {
			total += line.getProduct().getPrice() * line.getQuantity();
		}
		DecimalFormat df = new DecimalFormat("##########.##");
		total = Double.valueOf(df.format(total));
		request.get().setTotal(total);
		requestRepository.save(request.get());
	}
	
	@GetMapping()
	public @ResponseBody Iterable<RequestLine> GetAll() {
		return requestLineRepository.findAll();
	}
	@GetMapping("{id}")
	public @ResponseBody Optional<RequestLine> Get(@PathVariable Integer id) {
		try {
			Optional<RequestLine> requestLine = requestLineRepository.findById(id);
			if(!requestLine.isPresent()) return null;
			return requestLine;
		} catch (IllegalArgumentException ex) {
			throw ex;
		}
	}
	@PostMapping()
	public @ResponseBody RequestLine Insert(@RequestBody RequestLine requestLine) throws Exception {
		if(requestLine == null) {
			throw new Exception("RequestLine instance cannot be null.");
		}
		RequestLine rl = requestLineRepository.save(requestLine);
		RecalculateRequestTotal(rl.getRequest().getId());
		return rl;
	}
	@PutMapping("{id}")
	public @ResponseBody RequestLine Update(@PathVariable Integer id, @RequestBody RequestLine requestLine) throws Exception {
		if(requestLine == null) {
			throw new Exception("RequestLine instance cannot be null.");
		}
		if(id != requestLine.getId()) {
			throw new Exception("Id of requestLine instance does not match route parameter.");
		}
		RequestLine rl = requestLineRepository.save(requestLine);
		RecalculateRequestTotal(requestLine.getRequest().getId());
		return rl;
	}
	@DeleteMapping()
	public @ResponseBody void Delete(@RequestBody RequestLine requestLine) throws Exception {
		if(requestLine == null) {
			throw new Exception("RequestLine instance cannot be null.");
		}
		try {
			requestLineRepository.delete(requestLine);
		} catch (IllegalArgumentException ex) {
			throw ex;
		}
		RecalculateRequestTotal(requestLine.getRequest().getId());
	}
	@DeleteMapping("{id}")
	public @ResponseBody void Delete(@PathVariable Integer id) throws Exception {
		Optional<RequestLine> requestLine = requestLineRepository.findById(id);
		if(!requestLine.isPresent()) {
			throw new Exception("RequestLine not found.");
		}
		Delete(requestLine.get());
	}
}
