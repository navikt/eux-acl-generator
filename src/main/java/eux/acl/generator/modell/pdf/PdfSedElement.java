package eux.acl.generator.modell.pdf;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.NavigableMap;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PdfSedElement {
    private String index;
    private String prompt;
    private String valuetype;
    private NavigableMap<String, String> codework;
    private String enumName;

}
