package pt.ulisboa.tecnico.softeng.bank.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.bank.services.local.BankInterface;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData.CopyDepth;

@Controller
@RequestMapping(value = "/banks/{code}/operations")
public class OperationController {
	private static Logger logger = LoggerFactory.getLogger(ClientController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String showOperations(Model model, @PathVariable String code) {
		logger.info("showOperations from code:{}", code);
		
		BankData bankData = BankInterface.getBankDataByCode(code, CopyDepth.OPERATIONS);
		if (bankData == null) {
			model.addAttribute("error", "Error: it does not exist a bank with the code " + code);
			model.addAttribute("bank", new BankData());
			model.addAttribute("banks", BankInterface.getBanks());
			return "banks";
		}
		else{
			model.addAttribute("bank", bankData);
			return "operations";
		}
	}
}