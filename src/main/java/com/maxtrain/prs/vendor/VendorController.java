package com.maxtrain.prs.vendor;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.maxtrain.prs.vendor.Vendor;

@Controller
@RequestMapping("/api/vendors")
public class VendorController {
	
	@Autowired
	private VendorRepository vendorRepository;

	@GetMapping()
	public @ResponseBody Iterable<Vendor> GetAll() {
		return vendorRepository.findAll();
	}
	@GetMapping("{id}")
	public @ResponseBody Optional<Vendor> Get(@PathVariable Integer id) {
		try {
			Optional<Vendor> vendor = vendorRepository.findById(id);
			if(!vendor.isPresent()) return null;
			return vendor;
		} catch (IllegalArgumentException ex) {
			throw ex;
		}
	}
	@PostMapping()
	public @ResponseBody Vendor Insert(@RequestBody Vendor vendor) throws Exception {
		if(vendor == null) {
			throw new Exception("Vendor instance cannot be null.");
		}
		return vendorRepository.save(vendor);
	}
	@PutMapping("{id}")
	public @ResponseBody Vendor Update(@PathVariable Integer id, @RequestBody Vendor vendor) throws Exception {
		if(vendor == null) {
			throw new Exception("Vendor instance cannot be null.");
		}
		if(id != vendor.getId()) {
			throw new Exception("Id of vendor instance does not match route parameter.");
		}
		return vendorRepository.save(vendor);
	}
	@DeleteMapping()
	public @ResponseBody void Delete(@RequestBody Vendor vendor) throws Exception {
		if(vendor == null) {
			throw new Exception("Vendor instance cannot be null.");
		}
		try {
			vendorRepository.delete(vendor);
		} catch (IllegalArgumentException ex) {
			throw ex;
		}
	}
	@DeleteMapping("{id}")
	public @ResponseBody void Delete(@PathVariable Integer id) throws Exception {
		Optional<Vendor> vendor = vendorRepository.findById(id);
		if(!vendor.isPresent()) {
			throw new Exception("Vendor not found.");
		}
		Delete(vendor.get());
	}
}
