package com.pms.cde.processPensionMicroservice.service;

import java.io.IOException;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.cde.processPensionMicroservice.client.AuthorizationClient;
import com.pms.cde.processPensionMicroservice.client.PensionDisbursementClient;
import com.pms.cde.processPensionMicroservice.client.PensionerDetailClient;
import com.pms.cde.processPensionMicroservice.controller.processPensionController;
import com.pms.cde.processPensionMicroservice.exception.AuthorizationException;
import com.pms.cde.processPensionMicroservice.exception.PensionerDetailsException;
import com.pms.cde.processPensionMicroservice.exception.PensionerNotFoundException;
import com.pms.cde.processPensionMicroservice.model.PensionDetail;
import com.pms.cde.processPensionMicroservice.model.PensionerDetail;
import com.pms.cde.processPensionMicroservice.model.PensionerInput;
import com.pms.cde.processPensionMicroservice.model.ProcessInput;
import com.pms.cde.processPensionMicroservice.model.ProcessPensionInput;
import com.pms.cde.processPensionMicroservice.model.ProcessPensionResponse;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ProcessPensionServiceImpl implements ProcessPensionService {
	private static final Logger log = LoggerFactory.getLogger(ProcessPensionServiceImpl.class);

	@Autowired
	private PensionerDetailClient pensionerDetailClient;
	@Autowired
	private PensionDisbursementClient pensionDisbursementClient;
	@Autowired
	private AuthorizationClient authorizationClient;

	@Autowired
	private ProcessPensionService processPensionService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	private static final Logger Log = LoggerFactory.getLogger(processPensionController.class);
	
	public double getresult(PensionerDetail pensionerDetail) {
		double pensionAmount = 0.0;

		if (pensionerDetail.getPensionType().equalsIgnoreCase("self"))
			pensionAmount = (pensionerDetail.getSalary() * Constants.SELF_SERVICE_CHARGE
					+ pensionerDetail.getAllowance());
		else if (pensionerDetail.getPensionType().equalsIgnoreCase("family"))
			pensionAmount = (pensionerDetail.getSalary() * Constants.FAMILY_SERVICE_CHARGE
					+ pensionerDetail.getAllowance());
		log.info("" + pensionAmount);
		return pensionAmount;

	}

	public double getServiceCharge(String bankType) {
		if (bankType.equalsIgnoreCase("public")) {
			return 500.0;
		} else if (bankType.equalsIgnoreCase("private")) {
			return 550.0;
		} else {
			return -1;
		}
	}

	@Override
	public ProcessPensionResponse getcode(String header, @Valid ProcessInput processInput)
			throws AuthorizationException, PensionerNotFoundException, IOException {
		if (authorizationClient.authorizeRequest(header)) {
			//getting details on basis of aadhaar number from pensioner detail microservice
			PensionerDetail pensionerDetail = pensionerDetailClient.getPensionerDetailByAadhaar(header,
					processInput.getAadharNumber());
			if (pensionerDetail.getName() == null) {
				throw new PensionerNotFoundException("Pensioner with given aadhaar number not found");
			}
			//calculating service charge,aadhar,pension amt in process pension
			double serviceCharge = processPensionService.getServiceCharge(pensionerDetail.getBank().getBankType());
			ProcessPensionInput processPensionInput = new ProcessPensionInput(processInput.getAadharNumber(),
					processInput.getPensionAmount(), serviceCharge);
             //validation of process detail and process pension in disburse getcode and code function
			ProcessPensionResponse responseCode = null;
			for (int i = 1; i <= 3; i++) {
				responseCode = pensionDisbursementClient.getcode(header, processPensionInput);
				if (responseCode.getPensionStatusCode() == 10) {
					Log.info("End ProcessPension");
					return responseCode;
				}
				Log.info("retrying");
			}

			Log.info("End ProcessPension");
			return responseCode;

		} else {
			throw new AuthorizationException("User not authorized");
		}

	}

	@Override
	public PensionDetail getPensionDetails(String header, @Valid PensionerInput pensionerInput)
			throws PensionerNotFoundException, PensionerDetailsException, AuthorizationException {
		if (authorizationClient.authorizeRequest(header)) {
			PensionerDetail pensionerDetail = pensionerDetailClient.getPensionerDetailByAadhaar(header,
					pensionerInput.getAadharNumber());
			Log.info(pensionerDetail.getName());
			if (pensionerDetail.getName() == null) {
				throw new PensionerNotFoundException("Pensioner with given aadhar not found");
			}
			PensionerDetail receivedPensionerDetail = modelMapper.map(pensionerInput, PensionerDetail.class);
			if (pensionerDetail.compareTo(receivedPensionerDetail) < 0) {
				throw new PensionerDetailsException("Incorrect Pensioner Details.");
			}

			double pensionAmount = processPensionService.getresult(pensionerDetail);
			Log.info("" + pensionAmount);
			PensionDetail pensionDetail = modelMapper.map(pensionerDetail, PensionDetail.class);
			pensionDetail.setPensionAmount(pensionAmount);
			Log.info("" + pensionDetail.getPensionAmount());
			return pensionDetail;

		} else {
			throw new AuthorizationException("User not authorized");
		}
	}
}
