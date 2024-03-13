package pt.virtualmarket.userservice.exceptions;


public class HttpException  extends RuntimeException{

  private int statusCode;

  public HttpException(String message, int statusCode) {
    super(message);
    this.statusCode=statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
