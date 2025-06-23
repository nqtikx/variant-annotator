package bioinfo.variantannotator.model;

import java.time.LocalDateTime;

public record ErrorResponse(String error, String details, LocalDateTime timestamp) {
    public static ErrorResponse of(String error, String details) {
        return new ErrorResponse(error, details, LocalDateTime.now());
    }
}
