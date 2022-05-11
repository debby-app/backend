package com.project.debby.domain.loan.service;

import com.project.debby.domain.loan.dto.request.LoanChangeTermsDTO;
import com.project.debby.domain.loan.dto.request.LoanPaidPartConfirmationDTO;
import com.project.debby.domain.loan.dto.request.LoanRegisterDTO;
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

import java.math.BigDecimal;
import java.util.List;

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

    @Override
    public Loan createLoan(LoanRegisterDTO registerDTO, String userID) throws RequestedEntityNotFound {
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
        LoanState state = getLoanState(stateID, userID);
        if (state.getBorrower().getUserDetails().getUsername().equals(userID)){
            state.setStatus(LoanStatus.ACTIVE);
            notificationService.notify(state.getLoan().getOwner(), NotificationType.LOAN_ACCEPTED, state);
        } else throw new NotEnoughPermissionsException();
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void declineLoan(Long stateID, String userID) throws RequestedEntityNotFound, NotEnoughPermissionsException {
        LoanState state = getLoanState(stateID, userID);
        if (state.getBorrower().getUserDetails().getUsername().equals(userID)){
            state.setStatus(LoanStatus.DECLINED);
            notificationService.notify(state.getLoan().getOwner(), NotificationType.LOAN_DECLINED, state);
        } else throw new NotEnoughPermissionsException();
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void requestTermsChange(LoanChangeTermsDTO changeTermsDTO, String userID) throws RequestedEntityNotFound,
            NotEnoughPermissionsException {
        LoanState state = getLoanState(changeTermsDTO.getStateID(), userID);
        if (state.getBorrower().getUserDetails().getUsername().equals(userID)){
            state.setRequestedMaturityDate(changeTermsDTO.getNewMaturityDate());
            state.setStatus(LoanStatus.CHANGED);
            notificationService.notify(state.getLoan().getOwner(), NotificationType.TERMS_CHANGE_REQUEST, state);
        } else throw new NotEnoughPermissionsException();
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void acceptChangeTerms(Long stateID, String userID) throws RequestedEntityNotFound,
            NotEnoughPermissionsException {
        LoanState state = getLoanState(stateID, userID);
        if (state.getLoan().getOwner().getUserDetails().getUsername().equals(userID)){
            state.setUpdatedMaturityDate(state.getRequestedMaturityDate());
            state.setStatus(LoanStatus.ACTIVE);
            notificationService.notify(state.getBorrower(), NotificationType.TERMS_CHANGE_ACCEPTED, state);
        } else throw new NotEnoughPermissionsException();
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void declineChangeTerms(Long stateID, String userID) throws RequestedEntityNotFound,
            NotEnoughPermissionsException {
        LoanState state = getLoanState(stateID, userID);
        if (state.getLoan().getOwner().getUserDetails().getUsername().equals(userID)){
            state.setRequestedMaturityDate(state.getUpdatedMaturityDate());
            state.setStatus(LoanStatus.ACTIVE);
            notificationService.notify(state.getBorrower(), NotificationType.TERMS_CHANGE_DECLINED, state);
        } else throw new NotEnoughPermissionsException();
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void requestPaidPartConfirmation(LoanPaidPartConfirmationDTO confirmationDTO, String userID)
            throws RequestedEntityNotFound, NotEnoughPermissionsException {
        LoanState state = getLoanState(confirmationDTO.getStateID(), userID);
        if (state.getBorrower().getUserDetails().getUsername().equals(userID)){
            state.setPaidPartOnConfirmation(confirmationDTO.getPaidPart());
            state.setStatus(LoanStatus.PENDING_PAID_PART_ACCEPTANCE);
            notificationService.notify(state.getLoan().getOwner(), NotificationType.PAID_PART_REQUEST, state);
        } else throw new NotEnoughPermissionsException();
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void acceptPaidPart(Long stateID, String userID) throws RequestedEntityNotFound,
            NotEnoughPermissionsException {
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
        } else throw new NotEnoughPermissionsException();
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void declinePaidPart(Long stateID, String userID) throws RequestedEntityNotFound,
            NotEnoughPermissionsException {
        LoanState state = getLoanState(stateID, userID);
        if (state.getLoan().getOwner().getUserDetails().getUsername().equals(userID)){
            state.setPaidPartOnConfirmation(BigDecimal.ZERO);
            state.setStatus(LoanStatus.ACTIVE);
            notificationService.notify(state.getBorrower(), NotificationType.PAID_PART_DECLINED, state);
        } else throw new NotEnoughPermissionsException();
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void requestClose(Long stateID, String userID) throws RequestedEntityNotFound,
            NotEnoughPermissionsException {
        LoanState state = getLoanState(stateID, userID);
        if (state.getBorrower().getUserDetails().getUsername().equals(userID)){
            state.setStatus(LoanStatus.PENDING_CLOSE);
            notificationService.notify(state.getLoan().getOwner(), NotificationType.CLOSE_REQUEST, state);
        } else throw new NotEnoughPermissionsException();
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void acceptClose(Long stateID, String userID) throws RequestedEntityNotFound,
            NotEnoughPermissionsException {
        LoanState state = getLoanState(stateID, userID);
        if (state.getLoan().getOwner().getUserDetails().getUsername().equals(userID)){
            state.setStatus(LoanStatus.ARCHIVED);
            notificationService.notify(state.getBorrower(), NotificationType.CLOSE_ACCEPTED, state);
        } else throw new NotEnoughPermissionsException();
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public void declineClose(Long stateID, String userID) throws RequestedEntityNotFound,
            NotEnoughPermissionsException {
        LoanState state = getLoanState(stateID, userID);
        if (state.getLoan().getOwner().getUserDetails().getUsername().equals(userID)){
            state.setStatus(LoanStatus.ACTIVE);
            notificationService.notify(state.getBorrower(), NotificationType.CLOSE_DECLINED, state);
        } else throw new NotEnoughPermissionsException();
        loanStateRepository.saveAndFlush(state);
    }

    @Override
    public Loan getLoan(Long id, String userID) throws RequestedEntityNotFound {
        Loan loan = loanRepository.findById(id).orElseThrow(RequestedEntityNotFound::new);
        if (loan.getOwner().getUserDetails().getUsername().equals(userID)){
            return loan;
        }
        else throw new RequestedEntityNotFound();
    }

    @Override
    public List<Loan> getAllLoans(String userID) {
        return loanRepository.getAllByOwner_UserDetails_Credentials_ExternalId(userID);
    }

    @Override
    public List<LoanState> getStatusesOfAllLoans(String userID) {
        return loanStateRepository.getAllByBorrower_UserDetails_Credentials_ExternalId(userID);
    }

    public LoanState getLoanState(Long id, String userID) throws RequestedEntityNotFound {
        LoanState loanState = loanStateRepository.findById(id).orElseThrow(RequestedEntityNotFound::new);
        if (loanState.getBorrower().getUserDetails().getUsername().equals(userID)
        || loanState.getLoan().getOwner().getUserDetails().getUsername().equals(userID)){
            return loanState;
        }
        else throw new RequestedEntityNotFound();
    }
}
