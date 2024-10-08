package com.pms.cde.processPensionMicroservice.client;

import java.io.IOException;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.pms.cde.processPensionMicroservice.exception.PensionerNotFoundException;
import com.pms.cde.processPensionMicroservice.model.ProcessPensionInput;
import com.pms.cde.processPensionMicroservice.model.ProcessPensionResponse;

@FeignClient(name = "pensioner-disbursement-service", url = "http://3.110.66.245:7003/disburse")
public interface PensionDisbursementClient {
	@PostMapping("/disbursePension")
	public ProcessPensionResponse getcode(@RequestHeader("Authorization") String header,@RequestBody ProcessPensionInput processPensionInput)
			throws IOException, PensionerNotFoundException;
}
