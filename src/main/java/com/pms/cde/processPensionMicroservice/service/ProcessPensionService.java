package com.pms.cde.processPensionMicroservice.service;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.pms.cde.processPensionMicroservice.exception.AuthorizationException;
import com.pms.cde.processPensionMicroservice.exception.PensionerDetailsException;
import com.pms.cde.processPensionMicroservice.exception.PensionerNotFoundException;
import com.pms.cde.processPensionMicroservice.model.PensionDetail;
import com.pms.cde.processPensionMicroservice.model.PensionerDetail;
import com.pms.cde.processPensionMicroservice.model.PensionerInput;
import com.pms.cde.processPensionMicroservice.model.ProcessInput;
import com.pms.cde.processPensionMicroservice.model.ProcessPensionResponse;

public interface ProcessPensionService {
	
	public double getresult(PensionerDetail pensionerDetail);
	public double getServiceCharge(String bankType);
	public PensionDetail getPensionDetails(@RequestHeader("Authorization") String header,
			@Valid @RequestBody PensionerInput pensionerInput) throws PensionerNotFoundException, PensionerDetailsException, AuthorizationException;
	public ProcessPensionResponse getcode(@RequestHeader("Authorization") String header,
			@Valid @RequestBody ProcessInput processInput) throws AuthorizationException, IOException, PensionerNotFoundException;
}
