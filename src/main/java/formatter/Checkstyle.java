package formatter;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "checkstyle" )
public class Checkstyle {

  private CheckedFile[] checkedFiles;
  private String version;

  public CheckedFile[] getCheckedFile() {
    return checkedFiles;
  }

  @XmlElement( name = "file" )
  public void setFile(CheckedFile[] file) {
    this.checkedFiles = file;
  }

  public String getVersion() {
    return version;
  }

  @XmlAttribute( name = "version" )
  public void setVersion(String version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return "Checkstyle [file = " + checkedFiles + ", version = " + version + "]";
  }
}
