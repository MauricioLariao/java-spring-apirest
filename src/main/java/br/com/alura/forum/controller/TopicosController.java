package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDTO;
import br.com.alura.forum.controller.dto.TopicoDTO;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController //usando restcontroller ao inves de controller indica que todo metodo retorna um rest
@RequestMapping("/topicos") //colocando na classe, todos os metodos comecam com /topicos. Podemos usar somente o post, get..nos metodos
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	@GetMapping
	public List<TopicoDTO> lista(String nomeCurso){
		
		if (nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDTO.converter(topicos);
		}else {
			List<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso);
			return TopicoDTO.converter(topicos);
		}
		
	}
	
	//o parametro @requestbody indica que os parametros vem pelo corpo da requisicao
	//retornar o codigo 201 que foi cadastrado com suesso usa o responseentity e passa a uribuilder para o spring injetar
	//usa o @valid para informar o spring que vai utilizar o bean validation
	@PostMapping
	@Transactional //para comitar a transacao
	public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm form,
			UriComponentsBuilder uriBuilder) {
		
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		                     //caminho da uri e o id dinamico que é subistiuido pelo getid, assim ja passa o enereco do servidor dinamicamente
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDTO(topico));
		
	}
	
	@GetMapping("/{id}") //@path indica que é parametro da url
	public ResponseEntity<DetalhesDoTopicoDTO> detalhar(@PathVariable Long id) {
		
		Optional<Topico> topico = topicoRepository.findById(id);
		//se ecnotrou o topico com o id retorna, pra isso usa o optional
		if (topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesDoTopicoDTO(topico.get())); //topico.get pra pegar o topico da classe optional
		}
		return ResponseEntity.notFound().build();
		
	}
	
	@PutMapping("/{id}")
	@Transactional //para comitar a transacao
	public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id, 
			@RequestBody @Valid AtualizacaoTopicoForm form){
		
		Optional<Topico> optional = topicoRepository.findById(id);
		//se ecnotrou o topico com o id retorna, pra isso usa o optional
		if (optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			//topicoRepository.save(topico); nem precisa, pois a jpa ja entende que foi alterada a entidade pelo id e ja atualiza no final do metodo
			return ResponseEntity.ok(new TopicoDTO(topico));
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional //para comitar a transacao
	//passa ? porque nao precisa retornar nada 
	public ResponseEntity<?> atualizar(@PathVariable Long id){
		Optional<Topico> optional = topicoRepository.findById(id);
		//se ecnotrou o topico com o id retorna, pra isso usa o optional
		if (optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
		
	}
}
