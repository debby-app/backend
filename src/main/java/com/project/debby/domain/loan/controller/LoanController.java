package com.project.debby.domain.loan.controller;

import com.project.debby.domain.integrations.minio.client.exception.CannotRemoveObjectMinioException;
import com.project.debby.domain.loan.dto.request.LoanChangeTermsDTO;
import com.project.debby.domain.loan.dto.request.LoanPaidPartConfirmationDTO;
import com.project.debby.domain.loan.dto.request.LoanRegisterDTO;
import com.project.debby.domain.loan.dto.response.LoanDTO;
import com.project.debby.domain.loan.model.Loan;
import com.project.debby.domain.loan.model.LoanState;
import com.project.debby.domain.loan.service.LoanService;
import com.project.debby.util.exceptions.NotEnoughPermissionsException;
import com.project.debby.util.exceptions.RequestedEntityNotFound;
import com.project.debby.util.service.request.ExternalIdExtractor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/loan")
public class LoanController {

    private final LoanService loanService;

    @SneakyThrows
    @PostMapping
    public ResponseEntity<LoanDTO> createLoan(@RequestBody LoanRegisterDTO registerDTO, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        Loan loan = loanService.createLoan(registerDTO, externalId);
        return ResponseEntity.ok(loanService.convertToDTO(loan));
    }

    @SneakyThrows
    @PostMapping("/confirm")
    public ResponseEntity<Void> acceptLoan(@RequestParam Long id, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.acceptLoan(id, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/decline")
    public ResponseEntity<Void> declineLoan(@RequestParam Long id, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.declineLoan(id, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/terms/request")
    public ResponseEntity<Void> requestTermsChange(@RequestBody LoanChangeTermsDTO termsDTO, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.requestTermsChange(termsDTO, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/terms/accept")
    public ResponseEntity<Void> acceptTermsChange(@RequestParam Long id, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.acceptChangeTerms(id, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/terms/decline")
    public ResponseEntity<Void> declineTermsChange(@RequestParam Long id, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.declineChangeTerms(id, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/part/request")
    public ResponseEntity<Void> requestPaidPartConfirmation(@RequestBody LoanPaidPartConfirmationDTO
                                                                        paidPartConfirmationDTO,
                                                            HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.requestPaidPartConfirmation(paidPartConfirmationDTO, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/part/accept")
    public ResponseEntity<Void> acceptPaidPart(@RequestParam Long id, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.acceptPaidPart(id, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/part/decline")
    public ResponseEntity<Void> declinePaidPart(@RequestParam Long id, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.declinePaidPart(id, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/close/request")
    public ResponseEntity<Void> requestClose(@RequestParam Long id, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.requestClose(id, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/close/accept")
    public ResponseEntity<Void> acceptClose(@RequestParam Long id, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.acceptClose(id, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/close/decline")
    public ResponseEntity<Void> declineClose(@RequestParam Long id, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.declineClose(id, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @GetMapping
    public ResponseEntity<LoanDTO> getLoan(@RequestParam Long id, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        Loan loan = loanService.getLoan(id, externalId);
        return ResponseEntity.ok(loanService.convertToDTO(loan));
    }

    @SneakyThrows
    @GetMapping("/all")
    public ResponseEntity<List<LoanDTO>> getAllLoans(HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        List<Loan> loans = loanService.getAllLoans(externalId);
        return ResponseEntity.ok(loans.stream().map(loanService::convertToDTO).collect(Collectors.toList()));
    }

    @SneakyThrows
    @GetMapping("/states")
    public ResponseEntity<List<LoanDTO>> getStatesOfAllLoans(HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        List<LoanState> states = loanService.getStatusesOfAllLoans(externalId);
        return ResponseEntity.ok(states.stream().map((v) -> loanService.convertToDTO((v.getLoan()))).collect(Collectors.toList()));
    }

    @PostMapping("/states/{id}")
    public ResponseEntity<String> uploadImage(@PathVariable("id") Long stateId, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws NotEnoughPermissionsException, RequestedEntityNotFound, CannotRemoveObjectMinioException {
        String externalId = ExternalIdExtractor.getExternalID(request);
        return ResponseEntity.ok(loanService.uploadImage(externalId, file, stateId));

    }
}
