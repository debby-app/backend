package com.project.debby.domain.loan.service;

import com.project.debby.domain.integrations.minio.client.exception.CannotRemoveObjectMinioException;
import com.project.debby.domain.loan.dto.request.LoanChangeTermsDTO;
import com.project.debby.domain.loan.dto.request.LoanPaidPartConfirmationDTO;
import com.project.debby.domain.loan.dto.request.LoanRegisterDTO;
import com.project.debby.domain.loan.dto.response.LoanDTO;
import com.project.debby.domain.loan.model.Loan;
import com.project.debby.domain.loan.model.LoanState;
import com.project.debby.util.exceptions.NotEnoughPermissionsException;
import com.project.debby.util.exceptions.RequestedEntityNotFound;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LoanService {
    Loan createLoan(LoanRegisterDTO registerDTO, String userID) throws RequestedEntityNotFound;
    void acceptLoan(Long stateID, String userID) throws RequestedEntityNotFound, NotEnoughPermissionsException;
    void declineLoan(Long stateID, String userID) throws RequestedEntityNotFound, NotEnoughPermissionsException;
    void requestTermsChange(Long stateId, LoanChangeTermsDTO changeTermsDTO, String userID) throws RequestedEntityNotFound, NotEnoughPermissionsException;
    void acceptChangeTerms(Long stateID, String userID) throws RequestedEntityNotFound, NotEnoughPermissionsException;
    void declineChangeTerms(Long stateID, String userID) throws RequestedEntityNotFound, NotEnoughPermissionsException;
    void requestPaidPartConfirmation(Long stateId, LoanPaidPartConfirmationDTO confirmationDTO, String userID) throws RequestedEntityNotFound, NotEnoughPermissionsException;
    void acceptPaidPart(Long stateID, String userID) throws RequestedEntityNotFound, NotEnoughPermissionsException;
    void declinePaidPart(Long stateID, String userID) throws RequestedEntityNotFound, NotEnoughPermissionsException;
    void requestClose(Long stateID, String userID) throws RequestedEntityNotFound, NotEnoughPermissionsException;
    void acceptClose(Long stateID, String userID) throws RequestedEntityNotFound, NotEnoughPermissionsException;
    void declineClose(Long stateID, String userID) throws RequestedEntityNotFound, NotEnoughPermissionsException;
    Loan getLoan(Long id, String userID) throws RequestedEntityNotFound;
    List<Loan> getAllLoans(String userID);
    List<LoanState> getStatusesOfAllLoans(String userID);

    String uploadImage(String userId, MultipartFile file, Long stateId) throws RequestedEntityNotFound, NotEnoughPermissionsException, CannotRemoveObjectMinioException;
    LoanDTO convertToDTO(Loan loan);
}
