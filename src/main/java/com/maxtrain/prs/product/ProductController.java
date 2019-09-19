package com.maxtrain.prs.product;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.maxtrain.prs.product.Product;
import com.maxtrain.prs.product.ProductRepository;

@Controller
@RequestMapping("/api/products")
public class ProductController {
	@Autowired
	private ProductRepository productRepository;

	@GetMapping()
	public @ResponseBody Iterable<Product> GetAll() {
		return productRepository.findAll();
	}
	@GetMapping("{id}")
	public @ResponseBody Optional<Product> Get(@PathVariable Integer id) {
		try {
			Optional<Product> product = productRepository.findById(id);
			if(!product.isPresent()) return null;
			return product;
		} catch (IllegalArgumentException ex) {
			throw ex;
		}
	}
	@PostMapping()
	public @ResponseBody Product Insert(@RequestBody Product product) throws Exception {
		if(product == null) {
			throw new Exception("Product instance cannot be null.");
		}
		return productRepository.save(product);
	}
	@PutMapping("{id}")
	public @ResponseBody Product Update(@PathVariable Integer id, @RequestBody Product product) throws Exception {
		if(product == null) {
			throw new Exception("Product instance cannot be null.");
		}
		if(id != product.getId()) {
			throw new Exception("Id of product instance does not match route parameter.");
		}
		return productRepository.save(product);
	}
	@DeleteMapping()
	public @ResponseBody void Delete(@RequestBody Product product) throws Exception {
		if(product == null) {
			throw new Exception("Product instance cannot be null.");
		}
		try {
			productRepository.delete(product);
		} catch (IllegalArgumentException ex) {
			throw ex;
		}
	}
	@DeleteMapping("{id}")
	public @ResponseBody void Delete(@PathVariable Integer id) throws Exception {
		Optional<Product> product = productRepository.findById(id);
		if(!product.isPresent()) {
			throw new Exception("Product not found.");
		}
		Delete(product.get());
	}

}
