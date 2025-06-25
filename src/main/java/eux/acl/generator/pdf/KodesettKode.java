package eux.acl.generator.pdf;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record KodesettKode(
        String euKode, String navText, String navKode
) {
}
