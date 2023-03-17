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
@RequestMapping("/loans")
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
    @PostMapping("/{stateId}/confirm")
    public ResponseEntity<Void> acceptLoan(@PathVariable Long stateId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.acceptLoan(stateId, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/decline")
    public ResponseEntity<Void> declineLoan(@PathVariable Long stateId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.declineLoan(stateId, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/terms/request")
    public ResponseEntity<Void> requestTermsChange(@PathVariable Long stateId, @RequestBody LoanChangeTermsDTO termsDTO, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.requestTermsChange(stateId, termsDTO, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/terms/accept")
    public ResponseEntity<Void> acceptTermsChange(@PathVariable Long stateId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.acceptChangeTerms(stateId, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/terms/decline")
    public ResponseEntity<Void> declineTermsChange(@PathVariable Long stateId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.declineChangeTerms(stateId, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/part/request")
    public ResponseEntity<Void> requestPaidPartConfirmation(@PathVariable Long stateId,
                                                            @RequestBody LoanPaidPartConfirmationDTO
                                                                        paidPartConfirmationDTO,
                                                            HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.requestPaidPartConfirmation(stateId, paidPartConfirmationDTO, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/part/accept")
    public ResponseEntity<Void> acceptPaidPart(@PathVariable Long stateId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.acceptPaidPart(stateId, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/part/decline")
    public ResponseEntity<Void> declinePaidPart(@PathVariable Long stateId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.declinePaidPart(stateId, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/close/request")
    public ResponseEntity<Void> requestClose(@PathVariable Long stateId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.requestClose(stateId, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/close/accept")
    public ResponseEntity<Void> acceptClose(@PathVariable Long stateId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.acceptClose(stateId, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/close/decline")
    public ResponseEntity<Void> declineClose(@PathVariable Long stateId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        loanService.declineClose(stateId, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @GetMapping("/{loanId}")
    public ResponseEntity<LoanDTO> getLoan(@PathVariable Long loanId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        Loan loan = loanService.getLoan(loanId, externalId);
        return ResponseEntity.ok(loanService.convertToDTO(loan));
    }

    @SneakyThrows
    @GetMapping("/")
    public ResponseEntity<List<LoanDTO>> getAllLoans(HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        List<Loan> loans = loanService.getAllLoans(externalId);
        return ResponseEntity.ok(loans.stream().map(loanService::convertToDTO).collect(Collectors.toList()));
    }

    @SneakyThrows
    @GetMapping("/states")
    public ResponseEntity<List<LoanDTO>> getAllStates(HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        List<LoanState> states = loanService.getStatusesOfAllLoans(externalId);
        return ResponseEntity.ok(states.stream().map((v) -> loanService.convertToDTO((v.getLoan()))).collect(Collectors.toList()));
    }

    @PostMapping("/states/{id}/image")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws NotEnoughPermissionsException, RequestedEntityNotFound, CannotRemoveObjectMinioException {
        String externalId = ExternalIdExtractor.getExternalID(request);
        return ResponseEntity.ok(loanService.uploadImage(externalId, file, id));

    }
}
