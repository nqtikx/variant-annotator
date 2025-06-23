package bioinfo.variantannotator;

import bioinfo.variantannotator.service.AnnotationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class VariantAnnotatorApplication {
    private static final Logger log = LoggerFactory.getLogger(AnnotationService.class);


    public static void main(String[] args) {
        // и внутри метода
        log.info("==> findAllVariants вызван");
        System.out.println(">>> Приложение запущено");
        SpringApplication.run(VariantAnnotatorApplication.class, args);
    }


}

