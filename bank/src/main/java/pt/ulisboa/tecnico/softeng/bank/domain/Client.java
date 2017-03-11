package pt.ulisboa.tecnico.softeng.bank.domain;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Client {
	private static int counter = 0;

	private final String name;
	private final String ID;

	public Client(Bank bank, String name) {
		
		checkBank(bank);
		checkName(name);
		
		this.ID = Integer.toString(++Client.counter);
		this.name = name;

		bank.addClient(this);
	}

	public String getName() {
		return this.name;
	}

	public String getID() {
		return this.ID;
	}
	
	private void checkBank(Bank bank) {
		if (bank == null || bank.getName().trim().length() == 0 || bank.getCode().trim().length() == 0)
			throw new BankException();		
	}
	
	private void checkName(String name) {
		if (name == null || name == "" || name.trim().length() == 0)
			throw new BankException();		
	}	

}
