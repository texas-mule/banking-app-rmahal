package com.revature;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class test {

	@Test
	void btdccountCheck() {
		BankTableDao btd = new BankTableDao();
		assertEquals(btd.getAccount(1).getId(), btd.getAccount(1).getId(),"Testing objects");
	}

}
