package eux.acl.generator.pdf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;

import java.util.TreeMap;

@Builder
@JsonInclude(Include.NON_NULL)
public record MalLinjeDeler(
  String index,
  String prompt,
  String valuetype,
  TreeMap codework
) {

}
