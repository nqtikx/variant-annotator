package bioinfo.variantannotator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnotationRecord {
    private String rac;
    private int lap;
    private int rap;
    private String refkey;
    private String vcfId;
    private String clnsig;
    private String clnrevstat;
    private String clnvc;

    public static AnnotationRecord fromTsvLine(String line) {
        String[] parts = line.split("\t");
        if (parts.length < 8) throw new IllegalArgumentException("Недостаточно полей: " + line);
        return new AnnotationRecord(
                parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
                parts[3], parts[4], parts[5], parts[6], parts[7]
        );
    }

}
