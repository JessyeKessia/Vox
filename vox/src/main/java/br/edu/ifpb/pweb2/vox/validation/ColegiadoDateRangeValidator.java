package br.edu.ifpb.pweb2.vox.validation;
import org.springframework.stereotype.Component;

import br.edu.ifpb.pweb2.vox.entity.Colegiado;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ColegiadoDateRangeValidator implements ConstraintValidator<ColegiadoDateRange, Colegiado> {

    @Override
    public boolean isValid(Colegiado colegiado, ConstraintValidatorContext context) {
        if (colegiado == null) {
            return true; // considera null como válido, mas tem um notnull que barra isso
        }
        if (colegiado.getDataInicio() == null || colegiado.getDataFim() == null) {
            return true; // considera null como válido, mas tem um notnull que barra isso
        }
        return colegiado.getDataInicio().isBefore(colegiado.getDataFim());
    }
}
