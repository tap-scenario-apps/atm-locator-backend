package com.example.tanzu.atm;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.tanzu.atm.repository.ATMRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class SpringBaseTest 
{
	@Autowired
	protected ATMRepository atmRepo;
}
