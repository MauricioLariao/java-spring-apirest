package br.com.alura.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.alura.forum.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

	//filtrando a partir de um relacionamento ai pode usar o _ para separar
	List<Topico> findByCurso_Nome(String nomeCurso);

	//se quiser adotar outro padrao de nome para os metodos
	//tem que usar jpql
	//@Query("SELECT t FROM TOPICO t WHERE t.curso.nome = :nomeCurso")
	//List<Topico> carregarPorNomeDpCurso(@Param("nomeCurso") String nomeCurso);
}
