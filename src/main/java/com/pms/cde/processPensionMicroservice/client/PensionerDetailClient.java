package com.pms.cde.processPensionMicroservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.pms.cde.processPensionMicroservice.model.PensionerDetail;

@FeignClient(name = "pensioner-details-service", url = "http://43.205.123.147:7002/details")
public interface PensionerDetailClient {
	@GetMapping("/pensionerDetailByAadhaar/{aadhaarNumber}")
	public PensionerDetail getPensionerDetailByAadhaar(@RequestHeader(value = "Authorization",required = true) String header,@PathVariable long aadhaarNumber);
}
