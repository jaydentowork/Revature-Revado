package com.revature.revado.constants;

import com.revature.revado.dtos.ErrorResponse;

public class ErrorMessages {

    private ErrorMessages(){
    }

    public static final String LOGIN_FAILED = "Invalid username or password.";
    public static final String TAKEN_USERNAME = "The Username is already taken";

    public static final String UNAUTHORIZED_ACCESS = "You do not have permission to access this resource.";

    public static final String TODO_NOT_FOUND = "The requested Todo could not be found.";
    public static final String TODO_OWNERSHIP_REQUIRED = "You do not own this Todo item.";

    public static final String SUBTASK_NOT_FOUND = "The requested Subtask could not be found.";
    public static final String SUBTASK_OWNERSHIP_REQUIRED = "Unauthorized: You don't own this Subtask item.";
}
