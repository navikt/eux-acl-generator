package eux.acl.generator.pdf;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Kodesett(
        String kodesettNavnRina,
        String kodesettNavnINav,
        List<KodesettKode> kodesettet) {
}
