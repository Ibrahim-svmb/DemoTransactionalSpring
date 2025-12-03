package sn.esmt.demo_transactional.service;

import sn.esmt.demo_transactional.model.Compte;
import java.util.List;
import java.util.Optional;

public interface ICompteService {
    List<Compte> findAll();
    Compte save(Compte compte);
    Optional<Compte> findById(Integer id);
    void delete(Integer id);
    void transferer(Integer idSource, Integer idDestination, double montant);
    List<Compte> rechercherParTitulaire(String nom);
    List<Compte> soldeSuperieur(double valeur);
    List<Compte> rechercherParNumCarte(String numCarte);
}
