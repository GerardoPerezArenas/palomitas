package es.altia.util.struts;

import es.altia.util.commons.BasicValidations;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;


/**
 * It contains common validation/adapter operations for action form classes.
 * Each operation takes the following parameters:
 *
 * <ul> 
 * 
 * <li>An <code>ActionErrors</code>.</li>
 * <li>A property name: the name of a form field.</li>
 * <li>The property value: the value of the form field.</li>
 * <li>A <code>boolean</code> value specifying whether or not the property is 
 * mandatory.</li>
 * <li>A specification of the range of valid values (maybe specified with 
 * several parameters).</li>
 * <li>A <code>Locale</code> object if validation/adapter is
 * locale-dependent.</li>
 *
 * </ul>
 *
 * If the property value is not correct, an <code>ActionError</code> is added
 * to <code>ActionErrors</code> using the property name. The key of the
 * <code>ActionError</code> maybe one of the following values:
 *
 * <ul>
 *
 * <li><code>ErrorMessages.mandatoryField</code>: mandatory field.</li>
 * <li><code>ErrorMessages.incorrectValue</code>: incorrect value.</li>
 * <li><code>ErrorMessages.emailAddress.incorrect</code>: bad email.</li>
 * <li><code>ErrorMessages.ErrorMessages.day.incorrect</code>: bad day.</li>
 * <li><code>ErrorMessages.ErrorMessages.month.incorrect</code>: bad month.</li>
 * <li><code>ErrorMessages.ErrorMessages.year.incorrect</code>: bad year.</li>
 *
 * </ul>
 *
 * <p>
 * Some operations return the value resulting from the adapter. If the
 * adapter is not successful, the returned value is meaningless.
 * <p>
 */
public final class PropertyValidator {
    /*_______Constants______________________________________________*/
	public final static String INCORRECT_VALUE = "ErrorMessages.incorrectValue";
	public final static String MANDATORY_FIELD = "ErrorMessages.mandatoryField";
	public final static String INCORRECT_EMAIL_ADDRESS = "ErrorMessages.emailAddress.incorrect";
	public final static String INVALID_DAY = "ErrorMessages.day.incorrect";
	public final static String INVALID_MONTH = "ErrorMessages.month.incorrect";
	public final static String INVALID_YEAR = "ErrorMessages.year.incorrect";
    public final static String INVALID_DATE = "ErrorMessages.date.incorrect";
    public final static String INVALID_FORMAT = "ErrorMessages.invalidFormat";
    public final static String ERROR_INVALID_EXTENSION = "ErrorMessages.InvalidFileExtension";
    public final static String ERROR_MAXLENGTH = "ErrorMessages.MaxLengthExceeded";
    public final static String DATE_FORMAT_MESSAGE_KEY = "ConfigurationStrings.DateFormat";
    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    private static final Log _log =
            LogFactory.getLog(PropertyValidator.class.getName());

    /*_______Operations_____________________________________________*/
	private PropertyValidator() {}

	public final static long validateLong(ActionErrors errors,
		String propertyName, String propertyValue, boolean mandatory,
		long lowerValidLimit, long upperValidLimit) {
		
		long propertyValueAsLong = 0;
		
		if (validateMandatory(errors, propertyName, propertyValue,
			mandatory)) {
		
			boolean propertyValueIsCorrect = true;
            if (!BasicValidations.isEmpty(propertyValue)) {
                try {
                    propertyValueAsLong = Long.parseLong(propertyValue);
                    if ( (propertyValueAsLong < lowerValidLimit) ||
                         (propertyValueAsLong > upperValidLimit) ) {
                        propertyValueIsCorrect = false;
                    }//if
                } catch (NumberFormatException e) {
                    propertyValueIsCorrect = false;
                }//try-catch
            }//if
			if (!propertyValueIsCorrect) {
				errors.add(propertyName, new ActionError(INCORRECT_VALUE));
			}//if		
		}//if		
		return propertyValueAsLong;		
	}//validateLong
	
	public final static double validateDouble(ActionErrors errors,
		String propertyName, String propertyValue, boolean mandatory,
		double lowerValidLimit, double upperValidLimit, Locale locale) {

        _log.debug("PropertyValidator: validateDouble() BEGIN");
		double propertyValueAsDouble = 0;
		
		if (validateMandatory(errors, propertyName, propertyValue,
			mandatory)) {
		
			boolean propertyValueIsCorrect = StringUtils.containsOnly(propertyValue, new char[]{'0','1','2','3','4','5','6','7','8','9',',','.','+','-','E','e'});
            if (propertyValueIsCorrect) {
                try {
                    final NumberFormat numberFormatter =
                        NumberFormat.getNumberInstance(locale);
                    propertyValueAsDouble =
                        numberFormatter.parse(propertyValue).doubleValue();
                    if ( (propertyValueAsDouble < lowerValidLimit) ||
                         (propertyValueAsDouble > upperValidLimit) ) {
                        propertyValueIsCorrect = false;
                    }//if
                } catch (ParseException e) {
                    propertyValueIsCorrect = false;
                }//try-catch
            }//if
			if (!propertyValueIsCorrect) {
				errors.add(propertyName, new ActionError(INCORRECT_VALUE));
            }//if
		}//if
        if (_log.isDebugEnabled()) _log.debug("PropertyValidator: validateDouble() END returning..."+propertyValueAsDouble);
		return propertyValueAsDouble;
	}//validateDouble
	
	public final static void validateString(ActionErrors errors,
		String propertyName, String propertyValue,
		boolean mandatory, Collection validValues) {
	
		if (validateMandatory(errors, propertyName, propertyValue,
			mandatory)) {
			
			if (!validValues.contains(propertyValue)) {
				errors.add(propertyName, new ActionError(INCORRECT_VALUE));
			}//if			
		}//if	
	}//validateString

	/**
	 * Checks if a mandatory field is present.
	 *
	 * @return <code>false</code> if <code>propertyValue</code> is 
	 *         <code>null</code> or the empty string; <code>true</code>
	 *         otherwise
     */
	public final static boolean validateMandatory(ActionErrors errors,
		String propertyName, String propertyValue) {
		
		if ((propertyValue == null) || (propertyValue.length() == 0)) {
			errors.add(propertyName, new ActionError(MANDATORY_FIELD));
			return false;
		} else {
			return true;
		}//if		
	}//validateMandatory
	
	private final static boolean validateMandatory(ActionErrors errors,
		String propertyName, String propertyValue, boolean mandatory) {
		
		if (mandatory) {
			return validateMandatory(errors, propertyName, propertyValue);
		} else {
			return true;
		}//if		
	}//validateMandatory
	
	
	public final static void validateEmailAddress(ActionErrors errors,
		String propertyName, String propertyValue, boolean mandatory) {

		if (validateMandatory(errors, propertyName,	propertyValue,mandatory)) {
			if ( ! BasicValidations.isEmail(propertyValue) ) {
				 errors.add(propertyName, new ActionError(INCORRECT_EMAIL_ADDRESS));
			}//if
		}//if
	}//validateEmailAddress

    public final static void validateDate(ActionErrors errors,
        String propertyName, String property, String dateFormat) {
        validateDate(errors,propertyName,property,true,dateFormat);
    }//validateDate

    public final static void validateDate(ActionErrors errors,
        String propertyName, String property, boolean mandatory, String dateFormat) {
        validateDateBetween(errors,propertyName,property,mandatory,dateFormat,null,null);
    }//validateDate

/*
    public final static void validateDate(HttpServletRequest request,ActionErrors errors,
        String propertyName, String property, boolean mandatory) {
        validateDateBetween(request,errors,propertyName,property,mandatory,null,null);
    }//validateDate

    public final static void validateDateBetween(HttpServletRequest request,
                                                 ActionErrors errors,
                                                 String propertyName, String property,
                                                 boolean mandatory,
                                                 Calendar lower, Calendar upper) {
        String dateFormat = StrutsUtilOperations.getSimpleMessage(request,DATE_FORMAT_MESSAGE_KEY);
        if (dateFormat==null) dateFormat = DEFAULT_DATE_FORMAT;
        validateDateBetween(errors,propertyName,property,mandatory,dateFormat,lower,upper);
    }//validateDateBetween
*/

    public final static void validateDateBetween(ActionErrors errors,
        String propertyName, String property, boolean mandatory, String dateFormat,
        Calendar lower, Calendar upper) {
        final boolean doDateCheck;
        if (mandatory) {
            doDateCheck = validateMandatory(errors, propertyName, property);
        } else {
            doDateCheck = ((property!=null) && (property.length()>0) );
        }//if
        if (doDateCheck) {
            if (!BasicValidations.isDateBetween(property,dateFormat,lower,upper))
                errors.add(propertyName, new ActionError(INVALID_DATE));
        }//if
    }//validateDateBetween

    public final static void validateFile(HttpServletRequest request,
                                          ActionErrors errors, String propertyName,
                                          FormFile property, boolean mandatory, String[] validExtensions) {
        final boolean doChecks;
        if (mandatory) {
            if ( (property == null) || (BasicValidations.isEmpty(property.getFileName())) ) {
                errors.add(propertyName,new ActionError(MANDATORY_FIELD));
                doChecks = false;
            } else {
                doChecks = true;
            }//if
        } else {
            doChecks = (property != null) && (!BasicValidations.isEmpty(property.getFileName()));
        }//if

        if (doChecks) {
            if (validExtensions!=null) {
                /* Comprobar extensión */
                final String fileName = property.getFileName();
                boolean found = false;
                for (int i = 0; (i < validExtensions.length) && (!found); i++) {
                    if (fileName.trim().toUpperCase().endsWith(validExtensions[i]))
                        found = true;
                }//for
                if (!found)
                    errors.add(propertyName,new ActionError(ERROR_INVALID_EXTENSION));
            }//if

            /* Comprobar tamaño máximo */
            final Boolean maxLengthExceeded = (Boolean)
                    request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
            if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue())) {
                errors = new ActionErrors();
                errors.add(propertyName, new ActionError(ERROR_MAXLENGTH));
            }//if
        }//if
    }//validateFile


}//class
