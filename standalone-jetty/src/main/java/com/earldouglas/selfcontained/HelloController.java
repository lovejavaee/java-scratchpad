package com.earldouglas.selfcontained;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

	@RequestMapping("/hello")
	public void hello(Model model) {
		model.addAttribute("greeting", "Hello world!");
	}
}
