package br.edu.ifpb.pweb2.vox.repository;


import org.springframework.stereotype.Repository;

import br.edu.ifpb.pweb2.vox.entity.Assunto;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AssuntoRepository extends JpaRepository<Assunto, Long> {
    
}
