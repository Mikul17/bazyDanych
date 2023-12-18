package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Address;
import com.mikul17.bazyDanych.Models.TokenType;
import com.mikul17.bazyDanych.Models.User;
import com.mikul17.bazyDanych.Models.VerificationToken;
import com.mikul17.bazyDanych.Repository.UserRepository;
import com.mikul17.bazyDanych.Request.ChangePasswordRequest;
import com.mikul17.bazyDanych.Request.MailRequest;
import com.mikul17.bazyDanych.Response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final MailService mailService;
    private final AddressService addressService;

    public void checkIfUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ServiceException("User with id: " + userId + " does not exist");
        }
    }
    public User getUserById(Long userId) {
        try{
            return userRepository.findById(userId).orElseThrow(()
                    -> new ServiceException("User with id: " + userId + " does not exist"));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }
    public UserResponse getUserInfoById(Long userId){
        try{
            User user = userRepository.findById(userId).orElseThrow(
                    ()-> new ServiceException("User not found"));
            return mapUserToUserResponse(user);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
    public Double getUserBalanceById(Long userId){
        try{
            User user = userRepository.findById(userId).orElseThrow(
                    ()-> new ServiceException("User not found"));
            return user.getBalance();
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
    public UserResponse changePhoneNumber(String phoneNumber, Long userId){
        try{
            User user = userRepository.findById(userId).orElseThrow(
                    ()-> new ServiceException("User not found"));
            user.setPhoneNumber(phoneNumber);
            userRepository.save(user);
            return mapUserToUserResponse(user);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
    public UserResponse changeAccountNumber(String acc, Long userId){
        try{
            User user = userRepository.findById(userId).orElseThrow(
                    ()-> new ServiceException("User not found"));
            user.setAccountNumber(acc);
            userRepository.save(user);
            return mapUserToUserResponse(user);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
    public String changeUserBannedStatus (Optional<String> id){
        try{
            Long userId = Long.parseLong(id.orElseThrow(()-> new Exception("User id is missing")));
            User user = getUserById(userId);
            user.setBanned(!user.getBanned());
            userRepository.save(user);
            return "User with id: "+userId+" got"+(user.getBanned()?" banned" : " unbanned");
        }catch (Exception e){
            throw new ServiceException("Error while banning user: "+e.getMessage());
        }
    }
    public void changePassword(ChangePasswordRequest request){
        try{
            User user = getUserById(request.getUserId());
            if(!passwordEncoder.matches(request.getOldPassword(),user.getPassword())){
                throw new ServiceException("Old password is incorrect");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    public Address changeAddress(Address request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found"));

        Address currentAddress = user.getAddress();
        boolean isSharedAddress = userRepository.countByAddress_Id(currentAddress.getId()) > 1;

        if (isSharedAddress) {
            Address existingAddress = addressService.findOrCreateAddress(request);
            user.setAddress(existingAddress);
        } else {
            addressService.updateAddressDetails(currentAddress, request);
        }

        userRepository.save(user);
        return user.getAddress();
    }
    private UserResponse mapUserToUserResponse(User user){
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .accountNumber(user.getAccountNumber())
                .balance(user.getBalance())
                .address(user.getAddress())
                .build();
    }

    public void resetPasswordProcedure (Optional<String> requestEmail) {
        try{
            String userEmail = requestEmail.orElseThrow(()-> new ServiceException("Missing required parameter in request: email"));
            User user = userRepository.findByEmail(userEmail).orElseThrow(
                    ()-> new ServiceException("User with given email doesn't exist"));
            String passwordToken = verificationTokenService.generateResetPasswordToken(user);
            String htmlContent = generateResetPasswordMail(passwordToken);
            MailRequest mail = MailRequest.builder()
                    .email(userEmail)
                    .subject("Forgot password")
                    .text(htmlContent)
                    .build();

            new Thread(() -> mailService.sendMail(mail)).start();

        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    private static String generateResetPasswordMail (String passwordToken) {
        String resetPasswordUrl = "http://localhost:8080/api/user/resetPassword?token="+ passwordToken;
        return String.format(
                """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <title>Password Notification</title>
                        </head>
                        <body>
                            <p>Please click the button below to reset your password.</p>
                            <p>If you are not the one that requested password reset, you can ignore this mail.</p>
                            <a href='%s'>Reset password</a>
                        </body>
                        </html>""", resetPasswordUrl
        );
    }

    public void resetPasswordForUser(Optional<String> passwordResetToken){
        try{
            String parsedToken = passwordResetToken.orElseThrow(
                    () -> new ServiceException("Missing required parameter in request: token"));
            VerificationToken token = verificationTokenService.findByToken(parsedToken, TokenType.PASSWORD_RESET);
            User user = token.getUser();
            String safePassword = generateSafePassword();
            user.setPassword(passwordEncoder.encode(safePassword));
            userRepository.save(user);
            String htmlContent = String.format(
                    """
                            <!DOCTYPE html>
                            <html>
                            <head>
                                <title>Password Notification</title>
                            </head>
                            <body>
                                <p>Your new password is: <strong>%s</strong></p>
                            </body>
                            </html>""", safePassword
            );
            MailRequest mail = MailRequest.builder()
                    .email(user.getEmail())
                    .subject("Reset password")
                    .text(htmlContent)
                    .build();
            new Thread(() -> mailService.sendMail(mail)).start();
            verificationTokenService.removeTokenById(token.getId());
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    private String generateSafePassword(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        List<String> specialCharacters = new ArrayList<>(Arrays.asList("!", "@", "#", "$", "%", "^", "&", "*", "/", ",", ".", "?"));
        //uppercase
        for(int i=0; i<3; i++){
            int ascii = 65+random.nextInt(26);
            sb.append((char)ascii);
        }
        //lowercase
        for(int i=0; i<3; i++){
            int ascii = 97+random.nextInt(26);
            sb.append((char)ascii);
        }
        //digits
        for(int i=0; i<3; i++){
            int ascii = 48+random.nextInt(10);
            sb.append((char)ascii);
        }
        //special characters
        for(int i=0;i<3;i++){
            int index = random.nextInt(specialCharacters.size());
            sb.append(specialCharacters.get(index));
        }

        String password = sb.toString();
        return shuffleString(password);
    }
    private static String shuffleString(String input) {
        List<Character> characters = new ArrayList<>();
        for (char c : input.toCharArray()) {
            characters.add(c);
        }

        Collections.shuffle(characters);

        StringBuilder shuffledString = new StringBuilder();
        for (char c : characters) {
            shuffledString.append(c);
        }

        return shuffledString.toString();
    }
}