package eux.acl.generator.modell;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SedVersjonsdiff {
    private List<String> uendredeStierListe;
    private List<String> nyeStierListe;
    private List<String> utgaatteStierListe;
}
