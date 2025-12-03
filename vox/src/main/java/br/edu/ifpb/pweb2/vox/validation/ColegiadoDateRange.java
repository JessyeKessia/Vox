package br.edu.ifpb.pweb2.vox.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ColegiadoDateRangeValidator.class)
public @interface ColegiadoDateRange {
    String message() default "A data de início deve ser anterior à data de fim";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
