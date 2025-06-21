package com.maxtrain.prs.requestLine;

import java.text.DecimalFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.maxtrain.prs.request.Request;
import com.maxtrain.prs.request.RequestRepository;
import com.maxtrain.prs.product.Product;
import com.maxtrain.prs.product.ProductRepository;

import com.maxtrain.prs.requestLine.RequestLine;
import com.maxtrain.prs.requestLine.RequestLineRepository;

@Controller
@RequestMapping("/api/requestLines")

public class RequestLineController {


	@Autowired
	private RequestLineRepository requestLineRepository;
	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private ProductRepository productRepository;

	public void RecalculateRequestTotal(int requestId) {
		Optional<Request> requestOpt = requestRepository.findById(requestId);
		if (!requestOpt.isPresent()) {
			System.out.println("Request not found with id: " + requestId);
			return;
		}

		Request request = requestOpt.get();
		Iterable<RequestLine> requestLines = requestLineRepository.findByRequestId(requestId);
		double total = 0;
		int lineCount = 0;
		for (RequestLine line : requestLines) {
			double lineTotal = line.getProduct().getPrice() * line.getQuantity();
			total += lineTotal;
			lineCount++;
			System.out.println("Line " + line.getId() + ": " + line.getQuantity() + " x $" + line.getProduct().getPrice() + " = $" + lineTotal);
		}

		total = Math.round(total * 100.0) / 100.0;
		request.setTotal(total);
		requestRepository.save(request);

		System.out.println("Updated request " + requestId + " with " + lineCount + " lines, total: $" + total);
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
	@Transactional
	public @ResponseBody RequestLine Insert(@RequestBody RequestLine requestLine) throws Exception {
		if (requestLine == null) {
			throw new Exception("RequestLine instance cannot be null.");
		}

		// Load full Product and Request from DB
		Product product = productRepository.findById(requestLine.getProduct().getId())
				.orElseThrow(() -> new Exception("Product not found."));
		Request request = requestRepository.findById(requestLine.getRequest().getId())
				.orElseThrow(() -> new Exception("Request not found."));

		requestLine.setProduct(product);
		requestLine.setRequest(request);

		RequestLine rl = requestLineRepository.save(requestLine);
		RecalculateRequestTotal(rl.getRequest().getId());

		return rl;
	}


	@PutMapping("{id}")
	@Transactional
	public @ResponseBody RequestLine Update(@PathVariable Integer id, @RequestBody RequestLine requestLine) throws Exception {
		if (requestLine == null) {
			throw new Exception("RequestLine instance cannot be null.");
		}
		if (id != requestLine.getId()) {
			throw new Exception("Id of requestLine instance does not match route parameter.");
		}

		// Load full Product and Request from DB
		Product product = productRepository.findById(requestLine.getProduct().getId())
				.orElseThrow(() -> new Exception("Product not found."));
		Request request = requestRepository.findById(requestLine.getRequest().getId())
				.orElseThrow(() -> new Exception("Request not found."));

		requestLine.setProduct(product);
		requestLine.setRequest(request);

		RequestLine rl = requestLineRepository.save(requestLine);
		RecalculateRequestTotal(rl.getRequest().getId());

		return rl;
	}

	@DeleteMapping()
	@Transactional
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
