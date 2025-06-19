package com.maxtrain.prs.request;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.maxtrain.prs.request.Request;
import com.maxtrain.prs.request.RequestRepository;
import com.maxtrain.prs.requestLine.RequestLine;
import com.maxtrain.prs.requestLine.RequestLineRepository;
import com.maxtrain.prs.user.User;
import com.maxtrain.prs.user.UserRepository;

@Controller
@RequestMapping("/api/requests")
public class RequestController {

	@Autowired
	private RequestRepository requestRepository;
	@Autowired
	private RequestLineRepository requestLineRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping()
	public @ResponseBody Iterable<Request> GetAll() {
		return requestRepository.findAll();
	}

	@GetMapping("{id}")
	public @ResponseBody Optional<Request> Get(@PathVariable Integer id) {
		try {
			Optional<Request> request = requestRepository.findById(id);
			if (!request.isPresent()) return null;
			return request;
		} catch (IllegalArgumentException ex) {
			throw ex;
		}
	}

	@PostMapping()
	public @ResponseBody Request Insert(@RequestBody Request request) throws Exception {
		if (request == null) {
			throw new Exception("Request instance cannot be null.");
		}

		request.setStatus("NEW");
		request.setTotal(0.0);
		request.setSubmittedDate(LocalDate.now());
		request.setRequestNumber("REQ-" + System.currentTimeMillis());

		return requestRepository.save(request);
	}

	@PutMapping("{id}")
	public @ResponseBody Request Update(@PathVariable Integer id, @RequestBody Request request) throws Exception {
		if (request == null) {
			throw new Exception("Request instance cannot be null.");
		}
		if (id != request.getId()) {
			throw new Exception("Id of request instance does not match route parameter.");
		}
		return requestRepository.save(request);
	}

	@DeleteMapping()
	public @ResponseBody void Delete(@RequestBody Request request) throws Exception {
		if (request == null) {
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
		if (!request.isPresent()) {
			throw new Exception("Request not found.");
		}
		Delete(request.get());
	}

	@PutMapping("submit-review/{id}")
	public @ResponseBody Request submitForReview(@PathVariable Integer id) throws Exception {
		Optional<Request> reqOpt = requestRepository.findById(id);
		if (!reqOpt.isPresent()) {
			throw new Exception("Request not found");
		}

		Request request = reqOpt.get();

		// Only allow if status is NEW
		if (!request.getStatus().equalsIgnoreCase("NEW")) {
			throw new Exception("Only NEW requests can be submitted for review");
		}

		// Auto-approve if total <= 50
		if (request.getTotal() <= 50) {
			request.setStatus("APPROVED");
		} else {
			request.setStatus("REVIEW");
		}

		return requestRepository.save(request);
	}

	@PutMapping("approve/{id}")
	public @ResponseBody Request approve(
			@PathVariable Integer id,
			@RequestParam Integer reviewerId) throws Exception {

		Request request = requestRepository.findById(id)
				.orElseThrow(() -> new Exception("Request not found"));

		User reviewer = userRepository.findById(reviewerId)
				.orElseThrow(() -> new Exception("Reviewer not found"));

		if (!reviewer.getIsReviewer()) {
			throw new Exception("User is not authorized to approve requests");
		}
		if (request.getUser().getId() == (reviewerId)) {
			throw new Exception("Reviewers cannot approve their own requests");
		}
		if (!"REVIEW".equalsIgnoreCase(request.getStatus())) {
			throw new Exception("Only REVIEW requests can be approved");
		}

		request.setStatus("APPROVED");
		return requestRepository.save(request);
	}

	@PutMapping("reject/{id}")
	public @ResponseBody Request reject(
			@PathVariable Integer id,
			@RequestParam Integer reviewerId,
			@RequestParam String rejectionReason) throws Exception {

		Request request = requestRepository.findById(id)
				.orElseThrow(() -> new Exception("Request not found"));

		User reviewer = userRepository.findById(reviewerId)
				.orElseThrow(() -> new Exception("Reviewer not found"));

		if (!reviewer.getIsReviewer()) {
			throw new Exception("User is not authorized to reject requests");
		}
		if (request.getUser().getId() == (reviewerId)) {
			throw new Exception("Reviewers cannot reject their own requests");
		}
		if (!"REVIEW".equalsIgnoreCase(request.getStatus())) {
			throw new Exception("Only REVIEW requests can be rejected");
		}
		if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
			throw new Exception("Rejection reason must be provided");
		}

		request.setStatus("REJECTED");
		request.setRejectionReason(rejectionReason);
		return requestRepository.save(request);
	}

	@GetMapping("review-queue")
	public @ResponseBody Iterable<Request> getRequestsForReview(@RequestParam Integer reviewerId) throws Exception {
		User reviewer = userRepository.findById(reviewerId)
				.orElseThrow(() -> new Exception("Reviewer not found"));

		if (!reviewer.getIsReviewer()) {
			throw new Exception("User is not authorized to view review queue");
		}

		Iterable<Request> allRequests = requestRepository.findAll();
		List<Request> result = new ArrayList<>();
		for (Request r : allRequests) {
			if ("REVIEW".equalsIgnoreCase(r.getStatus()) && r.getUser().getId() != reviewerId) {
				result.add(r);
			}
		}
		return result;
	}

}
