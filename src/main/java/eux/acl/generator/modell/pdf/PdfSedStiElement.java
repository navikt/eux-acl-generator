package eux.acl.generator.modell.pdf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.NavigableMap;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PdfSedStiElement {

    private NavigableMap<String, String> codework;
}
