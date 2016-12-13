package controllers.api.validation;

import models.api.ObjectId;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

import java.util.regex.Pattern;

public class MongoIdCheck extends AbstractAnnotationCheck<MongoId> {

    final static String mes = "validation.mongoid";

    static Pattern phonePattern = Pattern.compile("^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}$");

    @Override
    public void configure(MongoId phone) {
        setMessage(phone.message());
    }

    @Override
    public boolean isSatisfied(Object validatedObject, Object value, OValContext context, Validator validator)
            throws OValException {
        if (value == null || value.toString().length() == 0) {
            return true;
        }
        return ObjectId.isValid(value.toString());
    }

}
