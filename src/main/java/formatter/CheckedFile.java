package formatter;


import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "file" )
public class CheckedFile {

  private String filePath;
  private CheckstyleError[] checkstyleErrors;

  public String getFilePath() {
    return filePath;
  }

  @XmlAttribute( name = "name" )
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  @XmlElement( name = "error" )
  public CheckstyleError[] getCheckstyleErrors() {
    return checkstyleErrors;
  }

  public void setCheckstyleErrors(CheckstyleError[] checkstyleErrors) {
    this.checkstyleErrors = checkstyleErrors;
  }

  @Override
  public String toString() {
    return "CheckstyleFile [name = " + filePath + ", error = " + checkstyleErrors + "]";
  }
}
