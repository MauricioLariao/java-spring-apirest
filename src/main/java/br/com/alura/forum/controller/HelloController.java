package br.com.alura.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class HelloController {
	
	@RequestMapping("/")
	@ResponseBody //pra nao achar que devolvera uma pagina
	public String hello() {
		return "Bom dia";
	}

}
