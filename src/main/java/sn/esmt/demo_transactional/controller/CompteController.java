package sn.esmt.demo_transactional.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sn.esmt.demo_transactional.model.Compte;
import sn.esmt.demo_transactional.service.ICompteService;

import java.util.List;
import java.util.Optional;

@Controller
public class CompteController {

    private final ICompteService compteService;

    @Autowired
    public CompteController(ICompteService compteService) {
        this.compteService = compteService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("comptes", compteService.findAll());
        return "home";
    }

    @PostMapping("/transfert")
    public String transfert(@RequestParam Integer idSource, @RequestParam Integer idDestination, @RequestParam Double montant, Model model) {
        try {
            compteService.transferer(idSource, idDestination, montant);
            model.addAttribute("message", "Transfert effectué avec succès !");
        } catch (RuntimeException e) {
            model.addAttribute("error", "Échec du transfert : " + e.getMessage());
        }
        model.addAttribute("comptes", compteService.findAll());
        return "comptes";
    }

    @GetMapping("/comptes")
    public String afficherComptes(Model model) {
        model.addAttribute("comptes", compteService.findAll());
        return "comptes";
    }

    @GetMapping("/comptes/nouveau")
    public String afficherFormulaireCreation(Model model) {
        model.addAttribute("compte", new Compte());
        return "form";
    }

    @PostMapping("/comptes")
    public String enregistrerCompte(@ModelAttribute Compte compte) {
        compteService.save(compte);
        return "redirect:/comptes";
    }

    @GetMapping("/comptes/modifier/{id}")
    public String afficherFormulaireModification(@PathVariable("id") Integer id, Model model) {
        Optional<Compte> compte = compteService.findById(id);
        if (compte.isPresent()) {
            model.addAttribute("compte", compte.get());
            return "form";
        } else {
            return "redirect:/comptes";
        }
    }

    @GetMapping("/comptes/supprimer/{id}")
    public String supprimerCompte(@PathVariable("id") Integer id) {
        compteService.delete(id);
        return "redirect:/comptes";
    }


    @GetMapping("/search")
    public String search(@RequestParam String query, Model model) {
        List<Compte> comptes;

        // Détecte si c'est un numéro de carte (9 chiffres uniquement)
        if (query.matches("\\d{9}")) {
            // Recherche par numéro de carte
            comptes = compteService.rechercherParNumCarte(query);
        } else {
            // Recherche par nom de titulaire
            comptes = compteService.rechercherParTitulaire(query);
        }

        model.addAttribute("comptes", comptes);
        return "comptes";
    }

    @GetMapping("/solde-min")
    public String soldeMin(@RequestParam double val, Model model) {
        List<Compte> comptes = compteService.soldeSuperieur(val);
        model.addAttribute("comptes", comptes);
        return "comptes";
    }
}
