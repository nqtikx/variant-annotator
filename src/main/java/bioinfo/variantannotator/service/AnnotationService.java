package bioinfo.variantannotator.service;

import bioinfo.variantannotator.model.AnnotationRecord;
import htsjdk.tribble.readers.TabixReader;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.file.Paths;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class AnnotationService {

    @Value("${annotation.file.path}")
    private String annotationFilePath;
    private static final Logger log = LoggerFactory.getLogger(AnnotationService.class);
    private TabixReader reader;

    @PostConstruct
    private void init() throws IOException {
        log.info("Инициализация TabixReader: {}", annotationFilePath);

        File file = new File(annotationFilePath);
        File tbi = new File(annotationFilePath + ".tbi");

        if (!file.exists()) {
            log.error("Файл .gz не найден: {}", file.getAbsolutePath());
            throw new FileNotFoundException("Файл .gz не найден: " + file.getAbsolutePath());
        }
        if (!tbi.exists()) {
            log.error("Файл .tbi не найден: {}", tbi.getAbsolutePath());
            throw new FileNotFoundException("Файл .tbi не найден: " + tbi.getAbsolutePath());
        }

        String uriPath = file.toURI().toString();
        log.info("Подготовленный URI для TabixReader: {}", uriPath);
        reader = new TabixReader(uriPath);
    }


    public AnnotationRecord findVariant(String rac, int lap, int rap, String refkey) throws IOException {
        log.info("Запрос поиска варианта: rac={}, lap={}, rap={}, refkey={}", rac, lap, rap, refkey);

        String region = rac + ":" + lap + "-" + rap;
        TabixReader.Iterator iter = reader.query(region);
        if (iter == null) {
            log.warn("Регион не найден в индексе: {}", region);
            return null;
        }

        String line;
        while ((line = iter.next()) != null) {
            try {
                AnnotationRecord record = AnnotationRecord.fromTsvLine(line);
                if (record.getLap() == lap
                        && record.getRap() == rap
                        && record.getRefkey().equalsIgnoreCase(refkey)) {
                    log.info("Найден совпадающий вариант: {}", record);
                    return record;
                }
            } catch (Exception e) {
                log.warn("Ошибка парсинга строки: [{}], причина: {}", line, e.getMessage());
            }
        }

        log.info("Вариант не найден для координат: {}", region);
        return null;
    }

    public List<AnnotationRecord> findAllVariants() throws IOException {
        log.info("==> findAllVariants вызван");

        File file = new File(annotationFilePath);
        if (!file.exists()) {
            log.error("Файл не найден: {}", file.getAbsolutePath());
            return Collections.emptyList();
        }

        List<AnnotationRecord> records = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
             GZIPInputStream gis = new GZIPInputStream(fis);
             BufferedReader br = new BufferedReader(new InputStreamReader(gis))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) continue;
                try {
                    AnnotationRecord rec = AnnotationRecord.fromTsvLine(line);
                    log.debug("Запись прочитана: {}", rec);
                    records.add(rec);
                } catch (Exception ex) {
                    log.warn("Ошибка чтения строки: [{}] — {}", line, ex.getMessage());
                }
            }
        }

        log.info("ИТОГ: всего считано {} записей", records.size());
        return records;
    }
}
