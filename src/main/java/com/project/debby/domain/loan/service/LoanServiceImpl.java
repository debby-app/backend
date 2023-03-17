package com.project.debby.domain.loan.service;

import com.project.debby.domain.integrations.minio.client.exception.CannotRemoveObjectMinioException;
import com.project.debby.domain.integrations.minio.service.MinioService;
import com.project.debby.domain.loan.dto.request.LoanChangeTermsDTO;
import com.project.debby.domain.loan.dto.request.LoanPaidPartConfirmationDTO;
import com.project.debby.domain.loan.dto.request.LoanRegisterDTO;
import com.project.debby.domain.loan.dto.response.LoanDTO;
import com.project.debby.domain.loan.dto.response.LoanStateDTO;
import com.project.debby.domain.loan.model.Loan;
import com.project.debby.domain.loan.model.LoanState;
import com.project.debby.domain.loan.model.LoanStatus;
import com.project.debby.domain.loan.model.repository.LoanRepository;
import com.project.debby.domain.loan.model.repository.LoanStateRepository;
import com.project.debby.domain.loan.service.factory.LoanFactory;
import com.project.debby.domain.loan.service.factory.LoanStateFactory;
import com.project.debby.domain.user.model.NotificationType;
import com.project.debby.domain.user.model.User;
import com.project.debby.domain.user.service.NotificationService;
import com.project.debby.domain.user.service.UserService;
import com.project.debby.util.exceptions.NotEnoughPermissionsException;
import com.project.debby.util.exceptions.RequestedEntityNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class LoanServiceImpl implements LoanService {

    private final UserService userService;
    private final LoanRepository loanRepository;
    private final LoanFactory loanFactory;
    private final LoanStateFactory loanStateFactory;
    private final LoanStateRepository loanStateRepository;
    private final NotificationService notificationService;

    private final MinioService minioService;

    @Override
    public Loan createLoan(LoanRegisterDTO registerDTO, String userID) throws RequestedEntityNotFound {
        log.debug("--creating loan | user extId {}", userID);
        User owner = userService.getUser(userID);
        Loan loan = loanFactory.create(registerDTO, owner);
        for (String v : registerDTO.getUsers()) {
            loan.getStates().add(createLoanState(loan, v));
        }
        loan = loanRepository.saveAndFlush(loan);
        loan.getStates().forEach((state ->
                notificationService.notify(state.getBorrower(), NotificationType.NEW_LOAN, state)));
        return loan;
    }

    private LoanState createLoanState(Loan loan, String borrowerID) throws RequestedEntityNotFound {
        User borrower = userService.getUser(borrowerID);
        return loanStateFactory.create(loan, borrower);
    }

    @Override
    public void acceptLoan(Long stateID, String userID) throws RequestedEntityNotFound, NotEnoughPermissionsException {
        log.debug("--accepting loan | user extId {} state {}", userID, stateID);
        LoanState state = getLoanState(stateID, userID);
        if (state.getBorrower().getUserDetails().getUsername().equals(userID)){
            state.setStatus(LoanStatus.ACTIVE);
            notificationService.notify(state.getLoan().getOwner(), NotificationType.LOAN_ACCEPTED, state);
        } else throw new NotEnoughPermissionsException(userID + " attempted to accept loan " + stateID + " that not related to him.");
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void declineLoan(Long stateID, String userID) throws RequestedEntityNotFound, NotEnoughPermissionsException {
        log.debug("--declining loan | user extId {} state {}", userID, stateID);
        LoanState state = getLoanState(stateID, userID);
        if (state.getBorrower().getUserDetails().getUsername().equals(userID)){
            state.setStatus(LoanStatus.DECLINED);
            notificationService.notify(state.getLoan().getOwner(), NotificationType.LOAN_DECLINED, state);
        } else throw new NotEnoughPermissionsException(userID + " attempted to decline loan " + stateID +
                " that not related to him.");
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void requestTermsChange(Long stateId, LoanChangeTermsDTO changeTermsDTO, String userID) throws RequestedEntityNotFound,
            NotEnoughPermissionsException {
        log.debug("--requesting change of terms | user extId {} state {}", userID, stateId);
        LoanState state = getLoanState(stateId, userID);
        if (state.getBorrower().getUserDetails().getUsername().equals(userID)){
            state.setRequestedMaturityDate(changeTermsDTO.getNewMaturityDate());
            state.setStatus(LoanStatus.CHANGED);
            notificationService.notify(state.getLoan().getOwner(), NotificationType.TERMS_CHANGE_REQUEST, state);
        } else throw new NotEnoughPermissionsException(userID + " attempted to request change of terms of loan " +
                stateId + " that not related to him.");
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void acceptChangeTerms(Long stateID, String userID) throws RequestedEntityNotFound,
            NotEnoughPermissionsException {
        log.debug("--accepting change of terms | user extId {} state {}", userID, stateID);
        LoanState state = getLoanState(stateID, userID);
        if (state.getLoan().getOwner().getUserDetails().getUsername().equals(userID)){
            state.setUpdatedMaturityDate(state.getRequestedMaturityDate());
            state.setStatus(LoanStatus.ACTIVE);
            notificationService.notify(state.getBorrower(), NotificationType.TERMS_CHANGE_ACCEPTED, state);
        } else throw new NotEnoughPermissionsException(userID + " attempted to accept change of terms of loan " +
                stateID + " that not related to him.");
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void declineChangeTerms(Long stateID, String userID) throws RequestedEntityNotFound,
            NotEnoughPermissionsException {
        log.debug("--declining change of terms | user extId {} state {}", userID, stateID);
        LoanState state = getLoanState(stateID, userID);
        if (state.getLoan().getOwner().getUserDetails().getUsername().equals(userID)){
            state.setRequestedMaturityDate(state.getUpdatedMaturityDate());
            state.setStatus(LoanStatus.ACTIVE);
            notificationService.notify(state.getBorrower(), NotificationType.TERMS_CHANGE_DECLINED, state);
        } else throw new NotEnoughPermissionsException(userID + " attempted to decline change of terms of loan "
                + stateID + " that not related to him.");
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void requestPaidPartConfirmation(Long stateId, LoanPaidPartConfirmationDTO confirmationDTO, String userID)
            throws RequestedEntityNotFound, NotEnoughPermissionsException {
        log.debug("--requesting paid paid confirmation | user extId {} state {}", userID, stateId);
        LoanState state = getLoanState(stateId, userID);
        if (state.getBorrower().getUserDetails().getUsername().equals(userID)){
            state.setPaidPartOnConfirmation(confirmationDTO.getPaidPart());
            state.setStatus(LoanStatus.PENDING_PAID_PART_ACCEPTANCE);
            notificationService.notify(state.getLoan().getOwner(), NotificationType.PAID_PART_REQUEST, state);
        } else throw new NotEnoughPermissionsException(userID + " attempted to request paid part confirmation of loan "
                + stateId + " that not related to him.");
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void acceptPaidPart(Long stateID, String userID) throws RequestedEntityNotFound,
            NotEnoughPermissionsException {
        log.debug("--accepting paid paid | user extId {} state {}", userID, stateID);
        LoanState state = getLoanState(stateID, userID);
        if (state.getLoan().getOwner().getUserDetails().getUsername().equals(userID)){
            state.setPaidPart(state.getPaidPartOnConfirmation());
            notificationService.notify(state.getBorrower(), NotificationType.PAID_PART_ACCEPTED, state);
            if (state.getPaidPart().compareTo(
                    state.getLoan().getPrice()
                            .divideToIntegralValue(BigDecimal.valueOf(
                                    state.getLoan().getStates().size()))) > -1){
                state.setStatus(LoanStatus.ARCHIVED);
                notificationService.notify(state.getBorrower(), NotificationType.CLOSE_ACCEPTED, state);
            } else state.setStatus(LoanStatus.ACTIVE);
        } else throw new NotEnoughPermissionsException(userID + " attempted to accept paid part confirmation of loan "
                + stateID + " that not related to him.");
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void declinePaidPart(Long stateID, String userID) throws RequestedEntityNotFound,
            NotEnoughPermissionsException {
        log.debug("--declining paid paid | user extId {} state {}", userID, stateID);
        LoanState state = getLoanState(stateID, userID);
        if (state.getLoan().getOwner().getUserDetails().getUsername().equals(userID)){
            state.setPaidPartOnConfirmation(BigDecimal.ZERO);
            state.setStatus(LoanStatus.ACTIVE);
            notificationService.notify(state.getBorrower(), NotificationType.PAID_PART_DECLINED, state);
        } else throw new NotEnoughPermissionsException(userID + " attempted to decline paid part confirmation of loan "
                + stateID + " that not related to him.");
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void requestClose(Long stateID, String userID) throws RequestedEntityNotFound,
            NotEnoughPermissionsException {
        log.debug("--requesting close of loan | user extId {} state {}", userID, stateID);
        LoanState state = getLoanState(stateID, userID);
        if (state.getBorrower().getUserDetails().getUsername().equals(userID)){
            state.setStatus(LoanStatus.PENDING_CLOSE);
            notificationService.notify(state.getLoan().getOwner(), NotificationType.CLOSE_REQUEST, state);
        } else throw new NotEnoughPermissionsException(userID + " attempted to request close of loan "
                + stateID + " that not related to him.");
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void acceptClose(Long stateID, String userID) throws RequestedEntityNotFound,
            NotEnoughPermissionsException {
        log.debug("--accepting close of loan | user extId {} state {}", userID, stateID);
        LoanState state = getLoanState(stateID, userID);
        if (state.getLoan().getOwner().getUserDetails().getUsername().equals(userID)){
            state.setStatus(LoanStatus.ARCHIVED);
            notificationService.notify(state.getBorrower(), NotificationType.CLOSE_ACCEPTED, state);
        } else throw new NotEnoughPermissionsException(userID + " attempted to accept close of loan "
                + stateID + " that not related to him.");
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void declineClose(Long stateID, String userID) throws RequestedEntityNotFound,
            NotEnoughPermissionsException {
        log.debug("--declining close of loan | user extId {} state {}", userID, stateID);
        LoanState state = getLoanState(stateID, userID);
        if (state.getLoan().getOwner().getUserDetails().getUsername().equals(userID)){
            state.setStatus(LoanStatus.ACTIVE);
            notificationService.notify(state.getBorrower(), NotificationType.CLOSE_DECLINED, state);
        } else throw new NotEnoughPermissionsException(userID + " attempted to decline close of loan "
                + stateID + " that not related to him.");
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public Loan getLoan(Long id, String userID) throws RequestedEntityNotFound {
        Loan loan = loanRepository.findById(id).orElseThrow(
                () -> new RequestedEntityNotFound("Loan with id " + id + " not found.")
        );
        if (loan.getOwner().getUserDetails().getUsername().equals(userID)){
            return loan;
        }
        else throw new RequestedEntityNotFound("Loan with id " + id + " not related to user " + userID);
    }

    @Override
    public List<Loan> getAllOwnedLoans(String userID, boolean archived) {
        if(archived) return loanRepository.getAllLoans(userID);
        else return loanRepository.getAllActiveLoans(userID);
    }

    @Override
    public List<LoanState> getAllDebts(String userID, boolean archived) {
        if (archived) return loanStateRepository.getAllDebts(userID);
        else return loanStateRepository.getAllActiveDebts(userID);
    }

    @Override
    public String uploadImage(String userId, MultipartFile file, Long stateId) throws RequestedEntityNotFound,
            NotEnoughPermissionsException, CannotRemoveObjectMinioException {
        log.debug("--uploading image to loan | user extId {} state {}", userId, stateId);
        User user = userService.getUser(userId);
        LoanState state = getLoanState(stateId, userId);
        if(state.getBorrower().getId().equals(user.getId())){
            if(state.getFile() != null){
                minioService.removeImage(state);
            }
            state.setFile(minioService.saveImage(state, file));
            loanStateRepository.saveAndFlush(state);
            return minioService.getImageURL(state);
        }else throw new NotEnoughPermissionsException(userId + " attempted to upload image for loan "
                + stateId + " that not related to him.");
    }

    @Override
    public LoanDTO convertToDTO(Loan loan) {
        var loanDTO = LoanDTO.create(loan);
        var statesDTO = loan.getStates().stream().map(v -> {
            var dto = LoanStateDTO.create(v);
            dto.setFile(minioService.getImageURL(v));
            return dto;
        }).collect(Collectors.toList());
        loanDTO.setStates(statesDTO);
        return loanDTO;
    }

    public LoanState getLoanState(Long id, String userID) throws RequestedEntityNotFound {
        LoanState loanState = loanStateRepository.findById(id).orElseThrow(
                () -> new RequestedEntityNotFound("Loan state with id " + id + "not found")
        );
        if (loanState.getBorrower().getUserDetails().getUsername().equals(userID)
        || loanState.getLoan().getOwner().getUserDetails().getUsername().equals(userID)){
            return loanState;
        }
        else throw new RequestedEntityNotFound("Loan state with id " + id + "not related to user " + userID);
    }
}
