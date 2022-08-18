package com.pms.cde.processPensionMicroservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import com.pms.cde.processPensionMicroservice.model.Bank;
import com.pms.cde.processPensionMicroservice.model.PensionerDetail;
import com.pms.cde.processPensionMicroservice.service.ProcessPensionServiceImpl;

@SpringBootTest(classes = ProcessPensionServiceImplTest.class)
public class ProcessPensionServiceImplTest {

	@InjectMocks
	ProcessPensionServiceImpl processPensionService;
	
	SimpleDateFormat parseDate=new SimpleDateFormat("dd-MM-yyyy");



	@Test
	public void testCheckDetailsForCorrectPensionerInputForSelfPensionType() throws ParseException {
		Bank bank = new Bank("HDFC", 33445566, "private");
		PensionerDetail details = new PensionerDetail("Ananya", parseDate.parse("01-01-1998"), "ANANY1001", 30000,
				12001, "self", bank);
		assertEquals(36001.0,processPensionService.getresult(details));
	}

	@Test
	public void testCheckDetailsForCorrectPensionerInputForFamilyPensionType() throws ParseException {
		Bank bank = new Bank("HDFC", 33445566, "private");
		PensionerDetail details = new PensionerDetail("Prem", parseDate.parse("02-02-1999"), "PREMD1234Q", 45000,
				2000, "family", bank);

		assertEquals(24500.0,processPensionService.getresult(details));
	}
	
	@Test
	public void testGetServiceChargePublicBank() {
		
		assertEquals(500.0,processPensionService.getServiceCharge("public"));
	}
	
	@Test
	public void testGetServiceChargePrivateBank() {
		
		assertEquals(550.0,processPensionService.getServiceCharge("private"));
	}

}
