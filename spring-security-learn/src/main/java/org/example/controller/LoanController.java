package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Loan;
import org.example.repository.LoanRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/myLoans")
public class LoanController {

    private final LoanRepository loanRepository;

    @GetMapping
    @PostAuthorize("hasRole('USER')")
    public List<Loan> getLoanDetails(@RequestParam Integer id) {
        List<Loan> loans = loanRepository.findByCustomerIdOrderByStartDtDesc(id);
        return loans;
    }

//    @PostMapping
//    @PreFilter("filterObject.title != 'Test'")
     // @PostFilter("filterObject.title != 'Test'")
//    public List<Loan> getLoanDetails(@RequestBody List<Loan> loans) {
//
//    }
}
