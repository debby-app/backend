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
        log.info("Started: create loan | user extID {}", externalId);
        Loan loan = loanService.createLoan(registerDTO, externalId);
        log.info("Complete: create loan | user extID {} loanId {}", externalId, loan.getId());
        return ResponseEntity.ok(loanService.convertToDTO(loan));
    }

    @SneakyThrows
    @PostMapping("/{stateId}/confirm")
    public ResponseEntity<Void> acceptLoan(@PathVariable Long stateId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        log.info("Started: accept loan | user extID {} stateId {}", externalId, stateId);
        loanService.acceptLoan(stateId, externalId);
        log.info("Complete: accept loan | user extID {} stateId {}", externalId, stateId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/decline")
    public ResponseEntity<Void> declineLoan(@PathVariable Long stateId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        log.info("Started: decline loan | user extID {} stateId {}", externalId, stateId);
        loanService.declineLoan(stateId, externalId);
        log.info("Complete: decline loan | user extID {} stateId {}", externalId, stateId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/terms/request")
    public ResponseEntity<Void> requestTermsChange(@PathVariable Long stateId, @RequestBody LoanChangeTermsDTO termsDTO, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        log.info("Started: request terms change | user extID {} stateId {}", externalId, stateId);
        loanService.requestTermsChange(stateId, termsDTO, externalId);
        log.info("Complete: request terms change | user extID {} stateId {}", externalId, stateId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/terms/accept")
    public ResponseEntity<Void> acceptTermsChange(@PathVariable Long stateId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        log.info("Started: accept terms change | user extID {} stateId {}", externalId, stateId);
        loanService.acceptChangeTerms(stateId, externalId);
        log.info("Complete: accept terms change | user extID {} stateId {}", externalId, stateId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/terms/decline")
    public ResponseEntity<Void> declineTermsChange(@PathVariable Long stateId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        log.info("Started: decline terms change | user extID {} stateId {}", externalId, stateId);
        loanService.declineChangeTerms(stateId, externalId);
        log.info("Complete: decline terms change | user extID {} stateId {}", externalId, stateId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/part/request")
    public ResponseEntity<Void> requestPaidPartConfirmation(@PathVariable Long stateId,
                                                            @RequestBody LoanPaidPartConfirmationDTO
                                                                        paidPartConfirmationDTO,
                                                            HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        log.info("Started: request paid part confirmation | user extID {} stateId {}", externalId, stateId);
        loanService.requestPaidPartConfirmation(stateId, paidPartConfirmationDTO, externalId);
        log.info("Complete: request paid part confirmation | user extID {} stateId {}", externalId, stateId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/part/accept")
    public ResponseEntity<Void> acceptPaidPart(@PathVariable Long stateId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        log.info("Started: accept paid part | user extID {} stateId {}", externalId, stateId);
        loanService.acceptPaidPart(stateId, externalId);
        log.info("Complete: accept paid part | user extID {} stateId {}", externalId, stateId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/part/decline")
    public ResponseEntity<Void> declinePaidPart(@PathVariable Long stateId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        log.info("Started: decline paid part | user extID {} stateId {}", externalId, stateId);
        loanService.declinePaidPart(stateId, externalId);
        log.info("Complete: decline paid part | user extID {} stateId {}", externalId, stateId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/close/request")
    public ResponseEntity<Void> requestClose(@PathVariable Long stateId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        log.info("Started: request close loan | user extID {} stateId {}", externalId, stateId);
        loanService.requestClose(stateId, externalId);
        log.info("Complete: request close loan | user extID {} stateId {}", externalId, stateId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/close/accept")
    public ResponseEntity<Void> acceptClose(@PathVariable Long stateId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        log.info("Started: accept close loan | user extID {} stateId {}", externalId, stateId);
        loanService.acceptClose(stateId, externalId);
        log.info("Complete: accept close loan | user extID {} stateId {}", externalId, stateId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{stateId}/close/decline")
    public ResponseEntity<Void> declineClose(@PathVariable Long stateId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        log.info("Started: decline close loan | user extID {} stateId {}", externalId, stateId);
        loanService.declineClose(stateId, externalId);
        log.info("Complete: decline close loan | user extID {} stateId {}", externalId, stateId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @GetMapping("/{loanId}")
    public ResponseEntity<LoanDTO> getLoan(@PathVariable Long loanId, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        log.info("Started: get loan | user extID {} stateId {}", externalId, loanId);
        Loan loan = loanService.getLoan(loanId, externalId);
        log.info("Complete: get loan | user extID {} stateId {}", externalId, loanId);
        return ResponseEntity.ok(loanService.convertToDTO(loan));
    }

    @SneakyThrows
    @GetMapping("/")
    public ResponseEntity<List<LoanDTO>> getAllLoans(@RequestParam(defaultValue = "false") boolean archived, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        log.info("Started: get all loans | user extID {}", externalId);
        List<Loan> loans = loanService.getAllOwnedLoans(externalId, archived);
        log.info("Complete: get all loans | user extID {}", externalId);
        return ResponseEntity.ok(loans.stream().map(loanService::convertToDTO).collect(Collectors.toList()));
    }

    @SneakyThrows
    @GetMapping("/debts")
    public ResponseEntity<List<LoanDTO>> getAllDebts(@RequestParam(defaultValue = "false") boolean archived, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        log.info("Started: get all states | user extID {}", externalId);
        List<LoanState> states = loanService.getAllDebts(externalId, archived);
        log.info("Complete: get all states | user extID {}", externalId);
        return ResponseEntity.ok(states.stream().map((v) -> loanService.convertToDTO((v.getLoan()))).collect(Collectors.toList()));
    }

    @PostMapping("/states/{id}/image")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws NotEnoughPermissionsException, RequestedEntityNotFound, CannotRemoveObjectMinioException {
        String externalId = ExternalIdExtractor.getExternalID(request);
        log.info("Started: upload image fro state | user extID {} state {}", externalId, id);
        String url = loanService.uploadImage(externalId, file, id);
        log.info("Complete: upload image fro state | user extID {} state {}", externalId, id);
        return ResponseEntity.ok(url);

    }
}
