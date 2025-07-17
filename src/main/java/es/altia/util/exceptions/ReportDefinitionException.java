package es.altia.util.exceptions;

import org.jfree.util.StackableException;

public class ReportDefinitionException extends StackableException
{
  public ReportDefinitionException()
  {
  }

  public ReportDefinitionException(final String message, final Exception ex)
  {
    super(message, ex);
  }

  public ReportDefinitionException(final String message)
  {
    super(message);
  }
}
