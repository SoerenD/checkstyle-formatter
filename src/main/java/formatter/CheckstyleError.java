package formatter;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "error" )
public class CheckstyleError {

  private String severity;
  private String line;
  private String column;
  private String errorClass;
  private String message;
  private String content;

  public String getSeverity() {
    return severity;
  }

  @XmlAttribute( name = "severity" )
  public void setSeverity(String severity) {
    this.severity = severity;
  }

  public String getLine() {
    return line;
  }

  @XmlAttribute( name = "line" )
  public void setLine(String line) {
    this.line = line;
  }

  public String getColumn() {
    return column;
  }

  @XmlAttribute( name = "column" )
  public void setColumn(String column) {
    this.column = column;
  }

  public String getErrorClass() {
    return errorClass;
  }

  @XmlAttribute( name = "source" )
  public void setErrorClass(String errorClass) {
    this.errorClass = errorClass;
  }

  public String getMessage() {
    return message;
  }

  @XmlAttribute( name = "message" )
  public void setMessage(String message) {
    this.message = message;
  }

  public String getContent() {
    return content;
  }

  @XmlAttribute( name = "content" )
  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "CheckstyleError [severity = " + severity + ", line = " + line + ", column = " + column + ", source = " + errorClass + ", message = " + message + ", content = " + content + "]";
  }
}
