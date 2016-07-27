package com.peekaboo.controller.confirmation;

import com.peekaboo.controller.sign.ErrorResponse;
import com.peekaboo.controller.sign.ErrorType;
import com.peekaboo.controller.sign.SignResponse;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import com.peekaboo.model.service.UserService;
import com.peekaboo.model.service.VerificationTokenService;
import com.peekaboo.security.jwt.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@SuppressWarnings("unchecked")
public class ConfirmationController {

    private final Logger logger = LogManager.getLogger(ConfirmationController.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationTokenService tokenService;

    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public ResponseEntity confirm(@Valid @RequestBody ConfirmationRequestEntity requestEntity, Errors errors) {
        logger.debug("Got CONFIRMATION request");
        if (errors.hasErrors()) {
            logErrors(errors);
            return new ResponseEntity(
                    transformErrors(errors.getAllErrors()),
                    HttpStatus.BAD_REQUEST
            );
        }
        VerificationToken key = tokenService.findByValue(requestEntity.getKey());
        if (key == null || !key.getUser().getId().equals(requestEntity.getId())) {
            return new ResponseEntity(
                    new ErrorResponse(ErrorType.INVALID_CONFIRM_TOKEN, "User entered invalid verification token"),
                    HttpStatus.BAD_REQUEST);
        } else {
            User user = userService.get(requestEntity.getId());
            user.setEnabled(true);
            tokenService.deleteByValue(requestEntity.getKey());
            userService.update(user);
            SignResponse response = new SignResponse();
            response.setId(user.getId())
                    .setUsername(user.getUsername())
                    .setRole(user.getRoles())
                    .setEnabled(user.isEnabled());
            return new ResponseEntity(
                    new ConfirmationResponse(jwtUtil.generateToken(response)),
                    HttpStatus.OK
            );
        }
    }

    private void logErrors(Errors errors) {
        logger.info("User has entered invalid data.");
        for(ObjectError error : errors.getAllErrors()) {
            logger.debug(error.toString());
        }
    }

    private List<ErrorResponse> transformErrors(List<ObjectError> errors) {
        List<ErrorResponse> errorResponses = new ArrayList<>();
        for(ObjectError error : errors) {
            errorResponses.add(
                    new ErrorResponse(
                                ErrorType.AUTHENTICATION_ERROR,
                                error.getDefaultMessage()
                        )
            );
        }
        return errorResponses;
    }


}
