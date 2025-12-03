package sn.esmt.demo_transactional.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sn.esmt.demo_transactional.model.Compte;

import java.util.List;
import java.util.Optional;

public interface CompteRepository extends JpaRepository<Compte, Integer> {
    // Recherche par titulaire
    List<Compte> findByTitulaireContainingIgnoreCase(String titulaire);


    // Solde supérieur à une valeur
    List<Compte> findBySoldeGreaterThan(Double solde);

    // Recherche par numéro de carte exact
    Optional<Compte> findByNumCarte(String numCarte);
}
