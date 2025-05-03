//package com.cema_health_ke_v1.Backend.CEMA_health.exception;
//
//import lombok.Getter;
//import org.springframework.http.HttpStatus;
//@Getter
//public class CustomException {
//
//
//    public CustomException(String s) {
//    }
//
//    @Getter
//    public class BusinessException extends RuntimeException {
//        private final HttpStatus status;
//
//        public BusinessException(String message, HttpStatus status) {
//            super(message);
//            this.status = status;
//        }
//    }
//
//    // Specific exception types
//    public class ResourceNotFoundException extends BusinessException {
//        public ResourceNotFoundException(String resource) {
//            super(resource + " not found", HttpStatus.NOT_FOUND);
//        }
//    }
//
//    public class ConflictException extends BusinessException {
//        public ConflictException(String message) {
//            super(message, HttpStatus.CONFLICT);
//        }
//    }
//
//    public class UnauthorizedException extends BusinessException {
//        public UnauthorizedException(String message) {
//            super(message, HttpStatus.UNAUTHORIZED);
//        }
//    }
//
//    public class ValidationException extends BusinessException {
//        public ValidationException(String message) {
//            super(message, HttpStatus.BAD_REQUEST);
//        }
//    }
//}
//

package com.cema_health_ke_v1.Backend.CEMA_health.exception;

import jakarta.annotation.Generated;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException {
    // Base Business Exception (nested)
    public static class BusinessException extends RuntimeException {
        private final HttpStatus status;

        public BusinessException(String message, HttpStatus status) {
            super(message);
            this.status = status;
        }

        public Generated getStatus() {
            return null;
        }
    }

    // Specific Exception Types (nested)
    public static class ResourceNotFoundException extends BusinessException {
        public ResourceNotFoundException(String resource) {
            super(resource + " not found", HttpStatus.NOT_FOUND);
        }
    }

    public static class ConflictException extends BusinessException {
        public ConflictException(String message) {
            super(message, HttpStatus.CONFLICT);
        }
    }

    public static class UnauthorizedException extends BusinessException {
        public UnauthorizedException(String message) {
            super(message, HttpStatus.UNAUTHORIZED);
        }
    }

    public static class ValidationException extends BusinessException {
        public ValidationException(String message) {
            super(message, HttpStatus.BAD_REQUEST);
        }
    }

    // Add more exceptions as needed...
    public static class ForbiddenException extends BusinessException {
        public ForbiddenException(String message) {
            super(message, HttpStatus.FORBIDDEN);
        }
    }
}
