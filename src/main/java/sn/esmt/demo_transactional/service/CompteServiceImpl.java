package sn.esmt.demo_transactional.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.esmt.demo_transactional.model.Compte;
import sn.esmt.demo_transactional.repository.CompteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CompteServiceImpl implements ICompteService {

    private final CompteRepository compteRepository;

    @Autowired
    public CompteServiceImpl(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Compte> findAll() {
        return compteRepository.findAll();
    }

    @Override
    @Transactional
    public Compte save(Compte compte) {
        return compteRepository.save(compte);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Optional<Compte> findById(Integer id) {
        return compteRepository.findById(id);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        compteRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void transferer(Integer idSource, Integer idDestination, double montant) {
        Optional<Compte> sourceCompteOpt = compteRepository.findById(idSource);
        Optional<Compte> destinationCompteOpt = compteRepository.findById(idDestination);

        if (!sourceCompteOpt.isPresent() || !destinationCompteOpt.isPresent()) {
            throw new IllegalArgumentException("L'un des comptes spécifiés est introuvable.");
        }

        Compte sourceCompte = sourceCompteOpt.get();
        Compte destinationCompte = destinationCompteOpt.get();

        if (sourceCompte.getSolde() < montant) {
            throw new IllegalArgumentException("Le montant à transférer est supérieur au solde du compte source.");
        }

        sourceCompte.setSolde(sourceCompte.getSolde() - montant);
        destinationCompte.setSolde(destinationCompte.getSolde() + montant);

        compteRepository.save(sourceCompte);
        compteRepository.save(destinationCompte);
    }

    @Override
    public List<Compte> rechercherParTitulaire(String nom) {
        return compteRepository.findByTitulaireContainingIgnoreCase(nom);
    }

    @Override
    public List<Compte> soldeSuperieur(double valeur) {
        return compteRepository.findBySoldeGreaterThan(valeur);
    }

    @Override
    public List<Compte> rechercherParNumCarte(String numCarte) {
        Optional<Compte> compte = compteRepository.findByNumCarte(numCarte);
        return compte.map(List::of).orElse(List.of());
    }
}
