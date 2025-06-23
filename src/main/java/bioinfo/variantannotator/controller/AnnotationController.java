package bioinfo.variantannotator.controller;
import bioinfo.variantannotator.model.ErrorResponse;

import bioinfo.variantannotator.model.AnnotationRecord;
import bioinfo.variantannotator.service.AnnotationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/info")
public class AnnotationController {

    private final AnnotationService annotationService;
    private static final Logger log = LoggerFactory.getLogger(AnnotationController.class);

    public AnnotationController(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    @GetMapping
    public ResponseEntity<?> getAnnotation(
            @RequestParam String rac,
            @RequestParam int lap,
            @RequestParam int rap,
            @RequestParam String refkey
    ) {
        log.info("Получен запрос: rac={}, lap={}, rap={}, refkey={}", rac, lap, rap, refkey);
        try {
            AnnotationRecord record = annotationService.findVariant(rac, lap, rap, refkey);
            if (record != null) {
                return ResponseEntity.ok(record);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ErrorResponse.of(
                                "Annotation not found",
                                "No variant matching the given coordinates and allele"
                        ));
            }
        } catch (IOException e) {
            log.error("Ошибка при чтении файла: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse.of("I/O error", e.getMessage()));
        } catch (Exception e) {
            log.warn("Неверные параметры запроса: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.of("Invalid request", e.getMessage()));
        }
    }

    @GetMapping("/debug/all")
    public ResponseEntity<?> getAllAnnotations() {
        log.info("Получен запрос: /debug/all");

        try {
            List<AnnotationRecord> records = annotationService.findAllVariants();
            return ResponseEntity.ok(records);
        } catch (IOException e) {
            log.error("Ошибка при чтении аннотаций: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse.of("I/O error", e.getMessage()));
        }
    }

}
