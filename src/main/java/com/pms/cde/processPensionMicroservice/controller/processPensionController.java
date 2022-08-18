package com.pms.cde.processPensionMicroservice.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.cde.processPensionMicroservice.exception.AuthorizationException;
import com.pms.cde.processPensionMicroservice.exception.PensionerDetailsException;
import com.pms.cde.processPensionMicroservice.exception.PensionerNotFoundException;
import com.pms.cde.processPensionMicroservice.model.PensionDetail;
import com.pms.cde.processPensionMicroservice.model.PensionerInput;
import com.pms.cde.processPensionMicroservice.model.ProcessInput;
import com.pms.cde.processPensionMicroservice.model.ProcessPensionResponse;
import com.pms.cde.processPensionMicroservice.service.ProcessPensionServiceImpl;

import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/process")
@CrossOrigin
public class processPensionController {
	private static final Logger Log = LoggerFactory.getLogger(processPensionController.class);

	@Autowired
	ProcessPensionServiceImpl processPensionServiceImpl;

	/*
	 * POST: localhost:7004/process/pensionDetail
	 * 
	 * { "name" : "Amit", "dateOfBirth" : "27-02-1996", "pan" : "RCASD1111T",
	 * "aadharNumber" : 401256780903, "pensionType" : "self" }
	 */

	@PostMapping("/PensionDetail")
	public PensionDetail getPensionDetails(@RequestHeader("Authorization") String header,
			@Valid @RequestBody PensionerInput pensionerInput)
			throws PensionerNotFoundException, PensionerDetailsException, AuthorizationException, RetryableException {

		Log.info("start getPensionDetails");

		Log.debug("" + pensionerInput);
		return processPensionServiceImpl.getPensionDetails(header, pensionerInput);
	}

	/*
	 * POST: localhost:7004/process/ProcessPension
	 * 
	 * { "aadharNumber" : 401256780903, "pensionAmount": 85000.0, "serviceCharge":
	 * 550 }
	 */

	@PostMapping("/ProcessPension")
	public ProcessPensionResponse getcode(@RequestHeader("Authorization") String header,
			@Valid @RequestBody ProcessInput processInput)
			throws AuthorizationException, IOException, PensionerNotFoundException {
		Log.info("start processPension");
		return processPensionServiceImpl.getcode(header, processInput);

	}

}
