package org.bp.payment;


import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class PizzaIdentifierService {
	
	public String getPizzaIdentifier() {
		return UUID.randomUUID().toString();
	}
	

}
