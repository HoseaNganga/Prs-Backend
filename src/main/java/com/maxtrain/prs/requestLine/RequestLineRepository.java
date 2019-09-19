package com.maxtrain.prs.requestLine;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestLineRepository extends CrudRepository<RequestLine, Integer> {
	Iterable<RequestLine> findByRequestId(int id);
}
