package br.com.alura.forum.config.validacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//sera um interceptador dos erros ocorridos
@RestControllerAdvice
public class ErroDeValidacaoHandler {

	@Autowired
	private MessageSource messageSource; //para pegar a mesnagem de erro
	
	//pra avisar que esse metodo controlora excecoes, informa as excecoes a serem lancadas
	//esse argumento no handler informa que em qualquer lugar que der erro caira nesse metodo
	@ResponseStatus(code = HttpStatus.BAD_REQUEST) //pra sempre devolver o 400
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<ErroDeFormularioDTO> handle(MethodArgumentNotValidException exception) {
		
		List<ErroDeFormularioDTO> dto = new ArrayList<>();
		List<FieldError> fieldErros = exception.getBindingResult().getFieldErrors(); //pega todos os erros
		
		fieldErros.forEach(e -> {
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			ErroDeFormularioDTO erro = new ErroDeFormularioDTO(e.getField(), mensagem);
			dto.add(erro);
		});
		
		return dto;
	}
}
